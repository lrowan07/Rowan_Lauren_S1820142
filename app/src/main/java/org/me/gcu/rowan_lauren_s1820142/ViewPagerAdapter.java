package org.me.gcu.rowan_lauren_s1820142;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

//Lauren Rowan S1820142

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitleList = new ArrayList<>();

    //Default generated constructor
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    //Default generated method
    @NonNull
    @Override
    public Fragment getItem(int position) {
        //Get the fragment from the specified index position in the array
        return fragmentArrayList.get(position);
    }

    //Default generated method
    @Override
    public int getCount() {
        //Return number of items in fragment array list
        return fragmentArrayList.size();
    }


    //This method adds a fragment to the list
    public void addFragment(Fragment fragment, String fragmentTitle){
        fragmentArrayList.add(fragment);
        fragmentTitleList.add(fragmentTitle);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
