package com.example.ljust.openevents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by ljust on 03/26/2016.
 */

public class EventActivity extends AppCompatActivity {

    public static final String PASSED_EVENT = "passed_event";
    private TextView mEventName;
    private TextView mEventLocation;
    private TextView mEventTime;
    private Event mEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Log.d("EF", "in onCreate");
        getSupportActionBar().setTitle(R.string.meetup_notif);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#4CFC9E"));

        mEvent = (Event)getIntent().getSerializableExtra(PASSED_EVENT);

        mEventName = (TextView)findViewById(R.id.details_event_name);
        mEventName.setText(mEvent.getEventName());

        mEventLocation = (TextView)findViewById(R.id.details_event_location);
        String location = mEvent.getVenueLocation().replace("\n", " ");
        mEventLocation.setText(location);

        mEventTime = (TextView)findViewById(R.id.details_event_time);
        mEventTime.setText(mEvent.getEventTime());
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("EF", "in onResume");
    }

    public static Intent newIntent(Context context, Event event) {
        Intent i = new Intent(context, EventActivity.class);
        i.putExtra(PASSED_EVENT, event);
        return i;
    }
}

