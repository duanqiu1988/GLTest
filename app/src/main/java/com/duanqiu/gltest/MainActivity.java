package com.duanqiu.gltest;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.duanqiu.gltest.fragment.BaseFragment;
import com.duanqiu.gltest.fragment.ItemFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BaseFragment.OnListFragmentInteractionListener {
    private static final String TAG = "MainActivity";
    private Toolbar mToolbar;
    private Map<String, List<Class>> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(getPackageName());
        List<ResolveInfo> infos = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : infos) {
            String name = info.activityInfo.name;
            String pkg = name.substring(name.lastIndexOf(".", name.lastIndexOf(".") - 1) + 1, name.lastIndexOf("."));
            try {
                if (map.containsKey(pkg)) {
                    map.get(pkg).add(Class.forName(name));
                } else {
                    List<Class> list = new ArrayList<>();
                    list.add(Class.forName(name));
                    map.put(pkg, list);
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        navFragment("start");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String title = String.valueOf(item.getTitle());
        navFragment(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navFragment(String title) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, BaseFragment.newInstance(title)).commit();
        mToolbar.setTitle(title.toUpperCase());
    }

    @Override
    public void onListFragmentInteraction(ItemFactory.FragmentItem item) {
        startActivity(new Intent(this, item.getActivityClass()));
    }

    @Override
    public List<Class> getActivityClasses(String pkg) {
        return map.get(pkg);
    }
}
