package com.example.homework9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.GridView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity  {


    TabLayout mTablayout;
    TabItem today;
    TabItem weekly;
    TabItem photos;
    ViewPager mPager;
    GridView gridView;
    PagerController mPagerController;
    WebView webView;
    String address;
    String[] googleImages;
    private RequestQueue requestQueue;
    String temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Toolbar toolbar = findViewById(R.id.actionBar);
        //System.out.println(extras.getString("address").split(",")[0]);
        toolbar.setTitle(extras.getString("address"));
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert getSupportActionBar() != null;
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);



        mTablayout = findViewById(R.id.tabLayout);
        today = findViewById(R.id.todayTab);
        weekly = findViewById(R.id.weeklyTab);
        photos = findViewById(R.id.photosTab);
        mPager = findViewById(R.id.viewPager);


        address = extras.getString("address");
        String city = address.split(",")[0];
        System.out.println(city);
        temperature = extras.getString("temperature");

        //getGooglePhotos(city,extras);

        //System.out.println(photosBundle);
        setFragments(extras);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setFragments(Bundle extras) {
        mPagerController = new PagerController(getSupportFragmentManager(), mTablayout.getTabCount(), extras);
        mPager.setAdapter(mPagerController);


        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //mTablayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.twitter_icon) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/intent/tweet?hashtags=CSCI571WeatherSearch&text=Check%20Out%20"+address+"'s%20Weather!%20It%20is "+temperature+"°F!"));
            startActivity(browserIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
