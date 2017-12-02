package com.example.mervat.mervat_udacity_01;

/**
 * Created by Mervat on 11/30/2017.
 */


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MoviesArrayAdapter extends ArrayAdapter  {

    private List<MovieInfo> MovieInfoList;

    private int resource;

    private Context context;


    public MoviesArrayAdapter(Context context, int resource, List<MovieInfo> MovieInfo) {
        super(context, resource, MovieInfo);
        this.context = context;
        this.MovieInfoList = MovieInfo;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieInfo details = MovieInfoList.get(position);

        View view = LayoutInflater.from(context).inflate(resource,parent,false);

        TextView movieName = (TextView) view.findViewById(R.id.textView);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);

        movieName.setText(details.getOriginal_title());

        Glide.with(context).load("https://image.tmdb.org/t/p/w500/"+ details.getPoster_path()).into(image);

        return  view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return MovieInfoList.get(position);
    }
}
