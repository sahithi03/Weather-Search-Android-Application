package com.example.homework9;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeekListView extends ArrayAdapter<String> {


    private String[] dates;
    private Integer[] dateIcons;
    private String[] minTemps;
    private String[] maxTemps;

    private Activity context;
    public WeekListView(Activity context,  String[] dates,  Integer[] dateIcons, String[] minTemps, String[] maxTemps ) {
        super(context, R.layout.week_list,dates);

        this.context = context;
        this.dates = dates;
        this.dateIcons = dateIcons;
        this.minTemps = minTemps;
        this.maxTemps = maxTemps;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.week_list,parent,false);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) r.getTag();


        }

        viewHolder.dateIcon.setImageResource(dateIcons[position]);
        viewHolder.date.setText(dates[position]);
        viewHolder.minTemp.setText(minTemps[position]);
        viewHolder.maxTemp.setText(maxTemps[position]);
        return r;
    }

    class ViewHolder{

        TextView date;
        ImageView dateIcon;
        TextView minTemp;
        TextView maxTemp;

        public ViewHolder(View v) {
            date = v.findViewById(R.id.dateID);
            dateIcon = v.findViewById(R.id.dateIcon);
            minTemp = v.findViewById(R.id.minTempID);
            maxTemp = v.findViewById(R.id.maxTempID);
        }
    }
}
