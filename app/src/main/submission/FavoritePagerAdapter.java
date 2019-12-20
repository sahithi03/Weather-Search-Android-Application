package com.example.homework9;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoritePagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    String forecastJSON;
    String[] googleImages;
    String address;
    List<List<String>> favsList;
    Bundle bundle;
    ArrayList<Fragment> favFragments ;
    private long baseId = 0;

    public FavoritePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public FavoritePagerAdapter(FragmentManager fm, int tabCount, List<List<String>>  favsList){
        super(fm);
        this.tabCount = tabCount;
        this.favsList = favsList;

    }

    @Override
    public Fragment getItem(int position) {
        favFragments  =  new ArrayList<>();

        for(int i =0; i<favsList.size(); i++){
            bundle  = new Bundle();
            if(i>0){
                bundle.putString("currLoc","false");
            }
            else{
                bundle.putString("currLoc","true");
            }
            bundle.putInt("position",position);

            bundle.putString("forecastJSON",favsList.get(i).get(0));
            bundle.putString("address",favsList.get(i).get(1));
            Favorite favorite = new Favorite();
            favorite.setArguments(bundle);
            this.favFragments.add(favorite);

        }

        return this.favFragments.get(position);

    }

    @Override
    public int getCount() {
        return favsList.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }


    public void removeTabPage(int position) {
        notifyDataSetChanged();

    }






    }






