package com.example.homework9;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PagerController extends FragmentPagerAdapter {

    int tabCounts;
    int[] iconsArray;
    String[] textArray;
    ArrayList<String> valuesArray;
    String weekSummary;
    String weekIcon;

    float[] minTemps;
    float[] maxTemps;

    String[] googleImages;


    public PagerController(FragmentManager fm, int tabCounts) {

        super(fm);
        this.tabCounts = tabCounts;
    }

    public PagerController(FragmentManager fm, int tabCounts, Bundle extras){
        super(fm);
        this.tabCounts = tabCounts;
        iconsArray = extras.getIntArray("icons");
        textArray = extras.getStringArray("text");
        valuesArray = extras.getStringArrayList("values");

        minTemps = extras.getFloatArray("minTemps");
        maxTemps = extras.getFloatArray("maxTemps");

        weekSummary = extras.getString("weekSummary");
        weekIcon = extras.getString("iconName");

        googleImages  = extras.getStringArray("googleImages");
        //System.out.println(googleImages[0]);


    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Bundle bundle = new Bundle();
                bundle.putIntArray("params1",iconsArray);
                bundle.putStringArray("params2",textArray);
                bundle.putStringArrayList("params3",valuesArray);
                TodayFragment tObj =  new TodayFragment();
                tObj.setArguments(bundle);

                return tObj;
            case 1:
                Bundle bundles = new Bundle();
                bundles.putFloatArray("minTemps",minTemps);
                bundles.putFloatArray("maxTemps",maxTemps);
                bundles.putString("weekSummary",weekSummary);
                bundles.putString("weekIcon",weekIcon);

                WeeklyFragment wObj = new WeeklyFragment();
                wObj.setArguments(bundles);
                return wObj;
            case 2:
                Bundle bundle1 = new Bundle();
                bundle1.putStringArray("googleImages",this.googleImages);
                PhotosFragment pObj = new PhotosFragment();
                pObj.setArguments(bundle1);
                return pObj;
            default:
                return null;

        }



    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
