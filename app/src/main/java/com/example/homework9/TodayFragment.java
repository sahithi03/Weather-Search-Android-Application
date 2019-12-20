package com.example.homework9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TodayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static int[] icons;
    static String[] texts;
    static ArrayList<String> values;

    GridView gridView;




    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            icons = getArguments().getIntArray("params1");
            texts = getArguments().getStringArray("params2");
            values = getArguments().getStringArrayList("params3");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        gridView = rootView.findViewById(R.id.gridview);
        CustomGridAdapter gridAdapter = new CustomGridAdapter();
        gridView.setAdapter(gridAdapter);
        return rootView;
    }

    public class CustomGridAdapter extends BaseAdapter {

        int[] iconsArray = TodayFragment.icons;
        String[] textsArray = TodayFragment.texts;
        ArrayList<String> valuesArray = TodayFragment.values;



        View view;
//        LayoutInflater layoutInflater;
        Context context;



        @Override
        public int getCount() {
            return iconsArray.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

                View view = getLayoutInflater().inflate(R.layout.single_grid,null);
                ImageView imageView = view.findViewById(R.id.grid_image_view);
                TextView textView1 = view.findViewById(R.id.grid_text_view1);
                TextView textView2 = view.findViewById(R.id.grid_text_view2);
                imageView.setImageResource(iconsArray[position]);
                if(position == 4){
                    android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.width = 350;
                    layoutParams.height = 350;
                    imageView.setLayoutParams(layoutParams);
                }
                textView1.setText(textsArray[position]);
                textView2.setText(valuesArray.get(position));


            return view;
        }
    }



}
