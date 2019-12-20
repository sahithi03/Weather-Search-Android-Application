package com.example.homework9;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WeeklyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private List<Entry> minTemps = new ArrayList<>();
    private List<Entry> maxTemps = new ArrayList<>();

    float[] mins;
    float[] maxs;
    String weekSummary;
    String weekIcon;

    final HashMap<String, String> hash_map = new HashMap<String, String>();


    //private OnFragmentInteractionListener mListener;

    public WeeklyFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        hash_map.put("clear-day","clear_day");
        hash_map.put("clear-night","clear_night");
        hash_map.put("rain","rain");
        hash_map.put("sleet","sleet");
        hash_map.put("snow","snow");
        hash_map.put("wind","wind");
        hash_map.put("fog","fog");
        hash_map.put("cloudy","cloudy");
        hash_map.put("partly-cloudy-night","partly_cloudy_night");
        hash_map.put("partly-cloudy-day","partly_cloudy_day");
        if (getArguments() != null) {

            mins = getArguments().getFloatArray("minTemps");
            maxs = getArguments().getFloatArray("maxTemps");
            weekSummary = getArguments().getString("weekSummary");
            weekIcon = getArguments().getString("weekIcon");


            for (int i = 0; i < mins.length; i++) {
                minTemps.add(new Entry(i, mins[i]));
                maxTemps.add(new Entry(i, maxs[i]));
            }

        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_weekly, container, false);

        LineChart chart = view.findViewById(R.id.linechart);
        TextView txt = view.findViewById(R.id.weeklySummary);
        txt.setText(weekSummary);
        ImageView img = view.findViewById(R.id.weeklyIcon);

        String icon = hash_map.get(weekIcon);
        img.setImageResource(getResources().getIdentifier("com.example.homework9:drawable/"+ icon, null, null));

            LineDataSet lineDataSet1 = new LineDataSet(minTemps, "Minimum Temperature");
            lineDataSet1.setColor(Color.parseColor("#BB86FC"));

            LineDataSet lineDataSet2 = new LineDataSet(maxTemps, "Maximum Temperature");
            lineDataSet2.setColor(Color.parseColor("#FAAB1A"));

            Legend legend = chart.getLegend();
            legend.setTextColor(Color.WHITE);
            legend.setTextSize(15f);

            XAxis xAxis = chart.getXAxis();
            xAxis.setTextColor(Color.WHITE);
            xAxis.setGranularity(1f);
            xAxis.setDrawGridLines(false);


            YAxis yAxisRight = chart.getAxisRight();
            yAxisRight.setEnabled(true);
            yAxisRight.setDrawGridLines(false);
            yAxisRight.setTextColor(Color.WHITE);
            YAxis yAxisLeft = chart.getAxisLeft();
            yAxisLeft.setGranularity(1f);
            yAxisLeft.setTextColor(Color.WHITE);
            yAxisLeft.setDrawGridLines(false);

            List<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet1);
            dataSets.add(lineDataSet2);

            LineData data = new LineData(dataSets);
            chart.setData(data);

            chart.invalidate();

            return view;
        }


    }

