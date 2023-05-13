package com.newsoft.nscustom;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PaperAdapter extends FragmentPagerAdapter {
    private List<Fragment> listTab;
    private List<String> titleTab;

    public PaperAdapter(FragmentManager fm) {
        super(fm);
        listTab = new ArrayList<>();
        titleTab = new ArrayList<>();
    }

    public void addTab(Fragment fragment, String title) {
        listTab.add(fragment);
        titleTab.add(title);
    }

    public void addTab(Fragment fragment) {
        listTab.add(fragment);
        titleTab.add("");
    }

    public void setTitleTab(int pos, String title) {
        titleTab.set(pos, title);
    }

    public void clear() {
        listTab.clear();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleTab.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return listTab.get(position);
    }

    @Override
    public int getCount() {
        return listTab.size();
    }
}
