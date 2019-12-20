package com.example.homework9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private RequestQueue requestQueue;
    private String lat;
    private String lon;
    private String city;
    private String state;
    private String country;
    private String address;
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
    private TextView searchResult;
    static SharedPreferences mPreferences;
    static SharedPreferences.Editor mEditor;


    JSONObject forecastResponse;
    static FavoritePagerAdapter favoritePagerAdapter;
    AutoSuggestAdapter autoSuggestAdapter;
    Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;


    ViewPager mPager;

    ListView lst;
    String[] dates;
    Integer[] dateIcons;
    String[] minTemps;
    String[] maxTemps;
    ArrayList<String> suggestions;
    int[] TodayIcons = {R.drawable.wind2, R.drawable.gauge2, R.drawable.precipitation2, R.drawable.thermometer2, R.drawable.clear_day, R.drawable.humidity2, R.drawable.visibility2, R.drawable.cloudcover2, R.drawable.ozone2};
    List<String> TodayValues = new ArrayList<String>();
    String[] TodayText = {"Wind Speed", "Pressure", "Precipitation", "Temperature", "Summary", "Humidity", "Visibility", "Cloud Cover", "Ozone"};

    float[] MinTemps;
    float[] MaxTemps;

    String iconName;
    String weekSummary;

    String[] googleImages;

    SearchView searchView;
    String flag = "option1";
    Bundle bundle = new Bundle();
    static List<List<String>> favsList = new ArrayList<List<String>>();
    static TabLayout tabLayout;
    String temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        //setContentView(R.layout.activity_main);

        if (extras != null) {
            flag = extras.getString("flag");
            //System.out.println(flag);
        }
        if (flag == "option1"){
            //System.out.println("hello from option1");
            setContentView(R.layout.activity_main);
            mPager = findViewById(R.id.mainViewPager);
            //mPager.setOffscreenPageLimit(10);
            tabLayout = findViewById(R.id.tabDots);
            tabLayout.setupWithViewPager(mPager,true);

        }
        if(flag.equalsIgnoreCase("option2")){
            //System.out.println("hello from opton2 view");
            setContentView(R.layout.search);

        }
        requestQueue = Volley.newRequestQueue(this);

        mPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        mEditor = mPreferences.edit();

        if (flag == "option1") {
            Toolbar toolbar = findViewById(R.id.actionBar);
            toolbar.setTitle("WeatherApp");
            toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
            setSupportActionBar(toolbar);


            String url = "http://ip-api.com/json";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        lat = response.getString("lat");
                        lon = response.getString("lon");
                        city = response.getString("city");
                        state = response.getString("region");
                        country = response.getString("countryCode");
                        address = city + "," + state + "," + country;
                        //System.out.println(response);
                        getForecast(lat,lon);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("something went  wrong in ip-api");
                    error.printStackTrace();
                }
            }
            );
            requestQueue.add(jsonObjectRequest);


        }

        if (flag.equalsIgnoreCase("option2")) {

            //System.out.println("inside option 2");
            String[] text = extras.getString("address").split(",");
            address = extras.getString("address");
            city = address.split(",")[0];

            getGoogleGeocode(city);

            Toolbar toolbar = findViewById(R.id.actionBar);
            toolbar.setTitle(address);
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);

            FloatingActionButton favButton = findViewById(R.id.favButton);
            favButton.show();
            if(mPreferences.contains(city) ){
                favButton.setImageResource(R.drawable.map_marker_minus);
            }
            else{
                favButton.setImageResource(R.drawable.ic_map_marker_plus);
            }

        }
    }


    private void getGoogleGeocode(String city){
        String url = "http://weathersearchcsci571.us-east-2.elasticbeanstalk.com/api/googleGeocode?address=" + city;
        JsonObjectRequest geocodeJSON = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    lat = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                    lon = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                    getForecast(lat, lon);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("something went wrong in google geocode");
            }
        });
        requestQueue.add(geocodeJSON);


    }


    private void getForecast(String lat, String lon) {
        summaryIcon = findViewById(R.id.summaryIconID);
        temperatureText = findViewById(R.id.temperatureTextID);
        summaryText = findViewById(R.id.summaryTextID);
        locationText = findViewById(R.id.locationID);
        humidityIcon = findViewById(R.id.humidityIcon);
        humidityText = findViewById(R.id.humidityText);
        windIcon = findViewById(R.id.windIcon);
        windText = findViewById(R.id.windText);
        visibilityIcon = findViewById(R.id.visibitlyIcon);
        visibilityText = findViewById(R.id.visibilityText);
        pressureIcon = findViewById(R.id.pressureIcon);
        pressureText = findViewById(R.id.pressureText);

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

        getGooglePhotos();

        String url = "http://weathersearchcsci571.us-east-2.elasticbeanstalk.com/forecast/45aa47c3d7fb9ce5fe6cbe0bc841aab0/" + lat + "/" + lon;
        JsonObjectRequest forecastJSON = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    forecastResponse = response;

                    findViewById(R.id.progressBarLinear).setVisibility(View.GONE);

                    if(flag.equalsIgnoreCase("option1")){
                        favsList = new ArrayList<List<String>>();
                        ArrayList<String> fav = new ArrayList<String>();
                        fav.add(forecastResponse.toString());
                        fav.add(address);
                        favsList.add(fav);
                        SharedPreferences prefA  = getSharedPreferences("prefs",MODE_PRIVATE);
                        Map<String, ?> allEntries = prefA.getAll();
                        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//                        System.out.println("inside option2 of get forecast");
//                        System.out.println("map values"+ entry.getKey() + ": " + entry.getValue().toString());
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<String>>() {}.getType();
                        String json = entry.getValue().toString();
                        ArrayList<String> fetch  = gson.fromJson(json,type);
                        ArrayList<String> newFav = new ArrayList<String>();
                        newFav.add(fetch.get(0));
                        newFav.add(fetch.get(1));
                        //System.out.println("==============================================");
                        //System.out.println(fetch.get(1));

                        favsList.add(newFav);

                    }
                        favoritePagerAdapter = new FavoritePagerAdapter(getSupportFragmentManager(),getSharedPreferences("prefs", Context.MODE_PRIVATE).getAll().size()+1,favsList);
                        mPager.setAdapter(favoritePagerAdapter);
                    }
                    else {
                        //System.out.println("hello"+ getSharedPreferences("prefs",Context.MODE_PRIVATE).getAll().size());

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

                        String timestamp = response.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getString("time");


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

                        lst = findViewById(R.id.listView);
                        CustomAdaptor customAdaptor = new CustomAdaptor();
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
                        //System.out.println(response.getJSONObject("daily").getJSONArray("data").length());
                        for (int i = 0; i < response.getJSONObject("daily").getJSONArray("data").length(); i++) {
                            float fMin = (float) response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureLow");
                            float fMax = (float) response.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureHigh");
                            MinTemps[i] = fMin;
                            MaxTemps[i] = fMax;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("something went wrong in forecast details");
                error.printStackTrace();
            }
        }
        );
        requestQueue.add(forecastJSON);


        //----------------Google Images------------------------------------------------------
    }
    private void getGooglePhotos(){
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



    public void secondScreen(View view) {
        //Toast.makeText(this,"you have clicked ", Toast.LENGTH_LONG).show();

        Bundle extras = new Bundle();
        extras.putIntArray("icons", TodayIcons);
        extras.putStringArrayList("values", (ArrayList<String>) TodayValues);
        extras.putStringArray("text", TodayText);
        extras.putString("temperature", temperature);

        extras.putFloatArray("minTemps", MinTemps);
        extras.putFloatArray("maxTemps", MaxTemps);
        extras.putString("iconName", iconName);
        extras.putString("weekSummary", weekSummary);
        extras.putString("address", address);

        extras.putStringArray("googleImages", googleImages);



        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtras(extras);


        startActivity(intent);


    }

    public void favorite(View view){

        if(mPreferences.contains(city)){
            mEditor.remove(city).apply();
            Toast.makeText(MainActivity.this,address + " was removed from favorites", Toast.LENGTH_SHORT).show();
            FloatingActionButton fav = findViewById(R.id.favButton);
            fav.setImageResource(R.drawable.ic_map_marker_plus);

            //System.out.println(forecastResponse);
            //favoritePagerAdapter.notifyDataSetChanged();
        }
        else{
            Gson gson = new Gson();
            ArrayList<String> tempList = new ArrayList();
            tempList.add(forecastResponse.toString());
            tempList.add(address);

            //String[] tempList = new String[]{forecastResponse.toString(),address};
            String json = gson.toJson(tempList);
            mEditor.putString(city,json).apply();
            Toast.makeText(MainActivity.this,address + " was added to favorites", Toast.LENGTH_SHORT).show();
            FloatingActionButton fav = findViewById(R.id.favButton);
            fav.setImageResource(R.drawable.map_marker_minus);

            //System.out.println(forecastResponse);


        }

    }
    public void updateFragments(int position) {

        for(int i =0 ;i<favsList.size();i++){
            List temp = favsList.get(i);

            System.out.println(temp.get(1));
        }
        favsList.remove(position);
        System.out.println("after");
        for(int i =0 ;i<favsList.size();i++){
            List temp = favsList.get(i);

            System.out.println(temp.get(1));
        }

        favoritePagerAdapter.removeTabPage(position);
    }

    //---to show the search view on the action bar---------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (flag == "option1") {
            getMenuInflater().inflate(R.menu.menu_main, menu); //inflates the xml file

            //MenuItem searchItem = menu.findItem(R.id.action_bar_search);
            searchView = (SearchView) menu.findItem(R.id.action_bar_search).getActionView();
            searchView.setQueryHint("Search...");

             //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);

            final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
            searchAutoComplete.setTextColor(Color.parseColor("#ffffff"));
            searchAutoComplete.setHintTextColor(Color.parseColor("#929193"));
            searchAutoComplete.setBackgroundColor(Color.parseColor("#1E1C1E"));

            searchAutoComplete.setDropDownBackgroundResource(android.R.color.background_light);
            autoSuggestAdapter = new AutoSuggestAdapter(getApplicationContext(),android.R.layout.simple_dropdown_item_1line);
            searchAutoComplete.setAdapter(autoSuggestAdapter);
            searchAutoComplete.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String queryString = (String) parent.getItemAtPosition(position);
                            searchAutoComplete.setText(queryString);
                        }
                    }
            );
            searchAutoComplete.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                    handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                            AUTO_COMPLETE_DELAY);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }



            });

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                            makeApiCall(searchAutoComplete.getText().toString());
                        }
                    }
                    return false;
                }
            });



            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);  //your class
                    i.putExtra("flag", "option2");
                    i.putExtra("address", query);

                    startActivity(i);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

            });

        }
        return super.onCreateOptionsMenu(menu);
    }

    private void makeApiCall(String toString) {

        String url = "http://weathersearchcsci571.us-east-2.elasticbeanstalk.com/api/autocomplete?input=" + toString;

        JsonObjectRequest autocompleteJSON = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    suggestions = new ArrayList<>();

                    if (response.getString("status") != "ZERO_RESULTS") {
                        for (int i = 0; i < response.getJSONArray("predictions").length(); i++) {
                            suggestions.add(response.getJSONArray("predictions").getJSONObject(i).getString("description"));
                        }
                        autoSuggestAdapter.setData(suggestions);
                        autoSuggestAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("something went wrong in autocomplete api");
            }
        });
        requestQueue.add(autocompleteJSON);

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
    public class AutoSuggestAdapter extends ArrayAdapter<String> implements Filterable{

        List<String> mlistData;

        public AutoSuggestAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            mlistData = new ArrayList<>();

        }
        public void setData(List<String> list){
            mlistData.clear();
            mlistData.addAll(list);

        }

        @Override
        public int getCount() {
            return mlistData.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return mlistData.get(position);
        }

        public String getObject(int position){
            return mlistData.get(position);

        }

        @NonNull
        @Override
        public Filter getFilter() {
            Filter dataFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        filterResults.values = mlistData;
                        filterResults.count = mlistData.size();
                    }
                    return filterResults;

                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && (results.count > 0)) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return dataFilter;
        }
    }


}
