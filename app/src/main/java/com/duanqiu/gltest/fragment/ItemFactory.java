package com.duanqiu.gltest.fragment;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {


    public static List<FragmentItem> createFragmentItems(List<Class> classes) {
        List<FragmentItem> list = new ArrayList<>();
        for (Class clazz : classes) {
            list.add(new FragmentItem(clazz.getSimpleName(), clazz));
        }

        return list;
    }

    public static class FragmentItem {
        public final String title;
        public final Class clazz;

        public FragmentItem(String title, Class clazz) {
            this.title = title;
            this.clazz = clazz;
        }

        public Class getActivityClass() {
            return this.clazz;
        }
    }
}
