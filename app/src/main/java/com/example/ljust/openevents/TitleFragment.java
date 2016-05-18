package com.example.ljust.openevents;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by ljust on 3/27/2016.
 */

public class TitleFragment extends Fragment {

    private ImageView mTitleImage;
    private AppCompatActivity parentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        parentActivity = ((AppCompatActivity)getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_title, parent, false);
        mTitleImage = (ImageView)v.findViewById(R.id.title_image);
        mTitleImage.setImageResource(R.drawable.meetupicon);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        parentActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FD2C2C")));
        parentActivity.getSupportActionBar().setTitle(R.string.app_name);
    }
}
