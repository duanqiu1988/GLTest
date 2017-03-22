package com.duanqiu.gltest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.fragment.ItemFactory.FragmentItem;

import java.util.List;

public class BaseFragment extends Fragment {

    private static final String PKG = "pkg";
    private String pkg;
    private OnListFragmentInteractionListener mListener;

    public BaseFragment() {
    }

    public static BaseFragment newInstance(String pkg) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        args.putString(PKG, pkg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            pkg = getArguments().getString(PKG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_item_list, container, false);

        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(new RecyclerViewAdapter(ItemFactory.createFragmentItems(mListener.getActivityClasses(pkg))
                , mListener));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FragmentItem item);

        List<Class> getActivityClasses(String pkg);
    }
}
