package com.example.testapplication.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabViewPagerAdapter extends FragmentPagerAdapter {

   private List<Fragment> fragmentList = new ArrayList<>();
   private List<String> titleList;

   public TabViewPagerAdapter(@NonNull FragmentManager fm,
                              List<String> titleList) {
      super(fm);
      this.titleList = titleList;
   }

   public void setFragmentList(List<Fragment> fragmentList){
      this.fragmentList.clear();
      this.fragmentList.addAll(fragmentList);
      notifyDataSetChanged();
   }

   @NonNull
   @Override
   public Fragment getItem(int position) {
      return fragmentList.get(position);
   }

   @Override
   public int getCount() {
      return fragmentList.size();
   }

   @Nullable
   @Override
   public CharSequence getPageTitle(int position) {
      return titleList.get(position);
   }
}
