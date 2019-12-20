package com.example.homework9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    static  String[] googleImages;

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private RequestQueue requestQueue;

    //private OnFragmentInteractionListener mListener;

    public PhotosFragment() {

        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());

        if (getArguments() != null) {

             googleImages = getArguments().getStringArray("googleImages");
//        System.out.println(googleImages[7]);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        recyclerView = rootView.findViewById(R.id.photosRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);
        return rootView;
    }
    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{


        String[] photoList = PhotosFragment.googleImages;

        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.photo_item,parent,false);
            return new PhotoViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
            String imageUrl = photoList[position];
            Glide.with(getContext()).load(imageUrl).centerCrop().into(holder.mImageView);

        }

        @Override
        public int getItemCount() {
            return photoList.length;
        }

        public class PhotoViewHolder extends RecyclerView.ViewHolder{
            public ImageView mImageView;

            public PhotoViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.googlePhoto);
            }
        }
    }




}
