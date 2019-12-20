package com.example.homework9;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Favorite extends Fragment {

    private RequestQueue requestQueue;
    private ImageView summaryIcon;
    private TextView temperatureText;
    private TextView summaryText;
    private TextView locationText;
    private ImageView humidityIcon;
    private TextView humidityText;
    private ImageView windIcon;
    private TextView windText;
    private ImageView visibilityIcon;
    private TextView visibilityText;
    private ImageView pressureIcon;
    private TextView pressureText;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    JSONObject response = null;

    ListView lst;
    String[] dates;
    Integer[] dateIcons;
    String[] minTemps;
    String[] maxTemps;
    String[] suggestions;
    int[] TodayIcons = {R.drawable.wind2, R.drawable.gauge2, R.drawable.precipitation2, R.drawable.thermometer2, R.drawable.clear_day, R.drawable.humidity2, R.drawable.visibility2, R.drawable.cloudcover2, R.drawable.ozone2};
    List<String> TodayValues = new ArrayList<String>();
    String[] TodayText = {"Wind Speed", "Pressure", "Precipitation", "Temperature", "Summary", "Humidity", "Visibility", "Cloud Cover", "Ozone"};

    float[] MinTemps;
    float[] MaxTemps;

    String iconName;
    String weekSummary;
    String[]  googleImages;
    String address;
    String city;

    String currLoc;
    String forecastJSON;
    List<List<String>> favsList;
    TabLayout tabLayout;
    int position;
    String temperature;

    FavoritePagerAdapter favoritePagerAdapter;
    ViewPager mPager;

    public Favorite() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());

        if(getArguments() != null){

            try {

                response = new JSONObject(getArguments().getString("forecastJSON"));
//                googleImages = getArguments().getStringArray("googleImages");

                address = getArguments().getString("address");
                city = address.split(",")[0];
                currLoc = getArguments().getString("currLoc");
                position = getArguments().getInt("position");

             } catch (JSONException e) {
                e.printStackTrace();
            }

            getGooglePhotos();

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_favorite, container, false);
        mPreferences = getActivity().getSharedPreferences("prefs",MODE_PRIVATE);
        mEditor = mPreferences.edit();
        tabLayout = view.findViewById(R.id.tabDots);
        mPager = view.findViewById(R.id.mainViewPager);


        if(currLoc.equalsIgnoreCase("false")){

            FloatingActionButton favButton = view.findViewById(R.id.favButton);
            favButton.show();
            if(mPreferences.contains(address.split(",")[0]) ){
                favButton.setImageResource(R.drawable.map_marker_minus);
            }
            else{
                favButton.setImageResource(R.drawable.ic_map_marker_plus);
            }
        }

        summaryIcon = view.findViewById(R.id.summaryIconID);
        temperatureText = view.findViewById(R.id.temperatureTextID);
        summaryText = view.findViewById(R.id.summaryTextID);
        locationText = view.findViewById(R.id.locationID);
        humidityIcon = view.findViewById(R.id.humidityIcon);
        humidityText = view.findViewById(R.id.humidityText);
        windIcon = view.findViewById(R.id.windIcon);
        windText = view.findViewById(R.id.windText);
        visibilityIcon = view.findViewById(R.id.visibitlyIcon);
        visibilityText = view.findViewById(R.id.visibilityText);
        pressureIcon = view.findViewById(R.id.pressureIcon);
        pressureText = view.findViewById(R.id.pressureText);

        final HashMap<String, String> hash_map = new HashMap<String, String>();
        hash_map.put("clear-day", "clear_day");
        hash_map.put("clear-night", "clear_night");
        hash_map.put("rain", "rain");
        hash_map.put("sleet", "sleet");
        hash_map.put("snow", "snow");
        hash_map.put("wind", "wind");
        hash_map.put("fog", "fog");
        hash_map.put("cloudy", "cloudy");
        hash_map.put("partly-cloudy-night", "partly_cloudy_night");
        hash_map.put("partly-cloudy-day", "partly_cloudy_day");

        try {
        String sumIcon = response.getJSONObject("currently").getString("icon");
        String iconToDisplay = hash_map.get(sumIcon);
        int summaryId = getResources().getIdentifier("com.example.homework9:drawable/" + iconToDisplay, null, null);
        summaryIcon.setImageResource(summaryId);
        temperature =  Integer.toString(Math.round(Float.parseFloat(response.getJSONObject("currently").getString("temperature"))));
        temperatureText.setText((Integer.toString(Math.round(Float.parseFloat(response.getJSONObject("currently").getString("temperature"))))) + "°F");
        summaryText.setText(response.getJSONObject("currently").getString("summary"));
        locationText.setText(address);
        //-------------Data for second card-----------------

        int humId = getResources().getIdentifier("com.example.homework9:drawable/" + "water_percent", null, null);
        humidityIcon.setImageResource(humId);
        humidityText.setText(String.valueOf(Math.round(response.getJSONObject("currently").getDouble("humidity") * 100)) + " %");
        windIcon.setImageResource(R.drawable.weather_windy);
        windText.setText(String.valueOf(Math.round(response.getJSONObject("currently").getDouble("windSpeed") * 100.0) / 100.0) + " mph");
        visibilityIcon.setImageResource(R.drawable.eye_outline);
        visibilityText.setText(String.valueOf(Math.round(response.getJSONObject("currently").getDouble("visibility") * 100.0) / 100.0) + " km");
        pressureIcon.setImageResource(R.drawable.gauge);
        pressureText.setText(String.valueOf(Math.round(response.getJSONObject("currently").getDouble("pressure") * 100.0) / 100.0) + " mb");
        //------------Data for third card------------

        dates = new String[8];
        dateIcons = new Integer[8];
        minTemps = new String[8];
        maxTemps = new String[8];

        for (int i = 0; i < 8; i++) {
            long time = Long.parseLong(response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getString("time"));
            Date date = new Date(time*1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
            dates[i] = sdf.format(date);
            String icon = hash_map.get(response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getString("icon"));
            dateIcons[i] = getResources().getIdentifier("com.example.homework9:drawable/" + icon, null, null);
            minTemps[i] = String.valueOf(Math.round(Float.parseFloat(response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getString("temperatureLow"))));
            maxTemps[i] = String.valueOf(Math.round(Float.parseFloat(response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getString("temperatureHigh"))));
        }

        lst = view.findViewById(R.id.listView);
        Favorite.CustomAdaptor customAdaptor = new Favorite.CustomAdaptor();
        lst.setAdapter(customAdaptor);

        //---------Data for second screen------

        String icon = hash_map.get(response.getJSONObject("currently").getString("icon"));
        TodayIcons[4] = getResources().getIdentifier("com.example.homework9:drawable/" + icon, null, null);
        TodayText[4] = response.getJSONObject("currently").getString("icon");
        String[] temp = TodayText[4].split("-");
        if(temp.length == 2){

            TodayText[4] = temp[0]+" "+ temp[1];
        }
        if(temp.length == 3){
            TodayText[4] = temp[1] +" "+ temp[2];
        }
        if(response.getJSONObject("currently").getString("windSpeed") != null){
            TodayValues.add(Math.round(response.getJSONObject("currently").getDouble("windSpeed")*100.0)/100.0+" mph");
        }
        if(response.getJSONObject("currently").getString("pressure") != null){
            TodayValues.add(Math.round(response.getJSONObject("currently").getDouble("pressure")*100.0)/100.0+" mb");
        }

        if(response.getJSONObject("currently").getString("precipIntensity") != null){
            if(Math.round(response.getJSONObject("currently").getDouble("precipIntensity")*100.0)/100.0  == 0.0){
                TodayValues.add(0+" mmph");
            }
            else{
                TodayValues.add(Math.round(response.getJSONObject("currently").getDouble("precipIntensity")*100.0)/100.0+" mmph");
            }
        }
        if(response.getJSONObject("currently").getString("temperature") != null){
            TodayValues.add(Math.round(response.getJSONObject("currently").getDouble("temperature"))+" °F");
        }
        TodayValues.add("");
        if(response.getJSONObject("currently").getString("humidity") != null){
            TodayValues.add(Math.round(response.getJSONObject("currently").getDouble("humidity") * 100)+"%");
        }
        if(response.getJSONObject("currently").getString("visibility") != null){
            TodayValues.add(Math.round((response.getJSONObject("currently").getDouble("visibility")) * 100.0)/100.0+" km");
        }
        if(response.getJSONObject("currently").getString("cloudCover") != null){
            TodayValues.add(Math.round(response.getJSONObject("currently").getDouble("cloudCover") * 100)+"%");
        }
        if(response.getJSONObject("currently").getString("ozone") != null){
            TodayValues.add(String.valueOf(Math.round(response.getJSONObject("currently").getDouble("ozone")*100.0)/100.0)+" DU");

        }

        MinTemps = new float[8];
        MaxTemps = new float[8];

        iconName = response.getJSONObject("daily").getString("icon");
        weekSummary = response.getJSONObject("daily").getString("summary");

        for (int i = 0; i < response.getJSONObject("daily").getJSONArray("data").length(); i++) {
            float fMin = (float) response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureLow");
            float fMax = (float) response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureHigh");
            MinTemps[i] = fMin;
            MaxTemps[i] = fMax;

        }
    }
            catch(JSONException e){
                e.printStackTrace();
        }

    CardView details = view.findViewById(R.id.detailsCard);
    details.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle extras = new Bundle();
            extras.putIntArray("icons", TodayIcons);
            extras.putStringArrayList("values", (ArrayList<String>) TodayValues);
            extras.putStringArray("text", TodayText);
            extras.putString("temperature",temperature);

            extras.putFloatArray("minTemps", MinTemps);
            extras.putFloatArray("maxTemps", MaxTemps);
            extras.putString("iconName", iconName);
            extras.putString("weekSummary", weekSummary);
            //extras.putStringArray("googleImages", googleImages);
            extras.putString("address",address);

            extras.putStringArray("googleImages", googleImages);

            Intent intent = new Intent(getActivity(), SecondActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    });

    FloatingActionButton favButton = view.findViewById(R.id.favButton);
    favButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(mPreferences.contains(city)){
                mEditor.remove(city).apply();
                Toast.makeText(getActivity(),address + " was removed from favorites", Toast.LENGTH_SHORT).show();
                FloatingActionButton fav = v.findViewById(R.id.favButton);
                fav.setImageResource(R.drawable.ic_map_marker_plus);
                MainActivity.tabLayout.removeTabAt(position);
                MainActivity ma = new MainActivity();
                //ma.updateFragments(position);
                //getFragmentManager().beginTransaction().remove(Favorite.this).commit();
                ma.updateFragments(position);


              }
            else{
                FloatingActionButton fav = view.findViewById(R.id.favButton);
                fav.setImageResource(R.drawable.map_marker_minus);

            }
        }
    });
            return view;
    }


    private void getGooglePhotos() {


        String url2 = "http://weathersearchcsci571.us-east-2.elasticbeanstalk.com/cityphotos/customsearch/v1?q="+address;
        JsonObjectRequest imageJSON = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    googleImages = new String[8];
                    for (int i = 0; i < response.getJSONArray("items").length(); i++) {
                        googleImages[i] = response.getJSONArray("items").getJSONObject(i).getString("link");
                    }

//
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("something went wrong in custom search");

            }
        });
        requestQueue.add(imageJSON);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    class CustomAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return dates.length;
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

            View v = getLayoutInflater().inflate(R.layout.week_list, null);

            TextView date = v.findViewById(R.id.dateID);
            ImageView dateIcon = v.findViewById(R.id.dateIcon);
            TextView minTemp = v.findViewById(R.id.minTempID);
            TextView maxTemp = v.findViewById(R.id.maxTempID);

            dateIcon.setImageResource(dateIcons[position]);
            date.setText(dates[position]);
            minTemp.setText(minTemps[position]);
            maxTemp.setText(maxTemps[position]);
            return v;
        }
    }

}
