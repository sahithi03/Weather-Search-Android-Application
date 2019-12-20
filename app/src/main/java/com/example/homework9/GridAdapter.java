package com.example.homework9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    Context context;
    private final String[] values;
    private final Integer[] images;
    View view;
    LayoutInflater layoutInflater;

    public GridAdapter(Context context, String[] values, Integer[] images) {
        this.context = context;
        this.values = values;
        this.images = images;
    }


    @Override
    public int getCount() {
        return values.length;
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
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){

            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_grid,null);

            ImageView imageView = view.findViewById(R.id.grid_image_view);
            TextView textView = view.findViewById(R.id.grid_text_view1);
            imageView.setImageResource(images[position]);
            textView.setText(values[position]);

        }
        return view;
    }
}
