package com.example.ljust.openevents;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljust on 03/26/2016.
 */

public class MeetupFragment extends Fragment {

    private RecyclerView mEventsRecyclerView;
    private List<Event> mEvents = new ArrayList<>();
    private List<Event> mSavedEvents = new ArrayList<>();
    private AppCompatActivity parentActivity;
    private String topic;
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION = "notification";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = ((AppCompatActivity)getContext());
        topic = getArguments().getString("topic");
        new FetchEventsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meetup, parent, false);
        mEventsRecyclerView = (RecyclerView)v.findViewById(R.id.meetup_recycler_view);
        mEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEventsRecyclerView.setAdapter(new EventAdapter(mEvents));
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        parentActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4CFC9E")));
        switch(topic) {
            case "technology":
                parentActivity.getSupportActionBar().setTitle(R.string.technology);
                break;
            case "games":
                parentActivity.getSupportActionBar().setTitle(R.string.games);
                break;
            case "music":
                parentActivity.getSupportActionBar().setTitle(R.string.music);
                break;
            case "food-and-drink":
                parentActivity.getSupportActionBar().setTitle(R.string.food_drink);
                break;
            case "photo":
                parentActivity.getSupportActionBar().setTitle(R.string.photography);
                break;
            default :
                parentActivity.getSupportActionBar().setTitle(R.string.app_name);
                break;
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mEventName;
        private TextView mVenue1;
        private TextView mEventTime;
        private Event mEvent;

        public EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mEventName = (TextView)itemView.findViewById(R.id.event_name);
            mVenue1 = (TextView)itemView.findViewById(R.id.venue_address);
            mEventTime = (TextView)itemView.findViewById(R.id.event_time);
        }

        public void bindEvent(Event event) {
            mEvent = event;
            mEventName.setText(mEvent.getEventName());
            mVenue1.setText(mEvent.getVenueLocation());
            mEventTime.setText(mEvent.getEventTime());
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Set an alarm for this event?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = EventActivity.newIntent(getActivity(), mEvent);
                            int eventRequestCode = (("event" + System.currentTimeMillis()).hashCode());
                            PendingIntent pi =  PendingIntent.getActivity(getContext(), eventRequestCode, i, 0);
                            Notification notif = new NotificationCompat.Builder(getContext())
                                    .setTicker(getResources().getString(R.string.meetup_notif))
                                    .setContentTitle(getResources().getString(R.string.meetup_notif))
                                    .setContentText(getResources().getString(R.string.upcoming_event))
                                    .setAutoCancel(true)
                                    .setContentIntent(pi)
                                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                                    .build();
                            scheduleNotification(notif, mEvent.getTimeinMillis());
                            Toast.makeText(getContext(), R.string.alarm_set, Toast.LENGTH_SHORT).show();
                            mSavedEvents.add(mEvent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void scheduleNotification(Notification notif, long eventTime) {
        Intent notifIntent = new Intent(getActivity(), NotificationReceiver.class);
        int requestCode = (("notif" + System.currentTimeMillis()).hashCode());
        notifIntent.putExtra(NOTIFICATION_ID, requestCode);
        notifIntent.putExtra(NOTIFICATION, notif);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, notifIntent, 0);

//        long futureInMillis = eventTime - SystemClock.elapsedRealtime() + 43200000; 12 hours
        long futureInMillis = SystemClock.elapsedRealtime() + 7000; //set to 7 seconds for testing purposes
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {

        private List<Event> mEvents;

        public EventAdapter(List<Event> events) {
            mEvents = events;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_event, parent, false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            holder.bindEvent(mEvents.get(position));
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }

    private class FetchEventsTask extends AsyncTask<Void, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(Void... params) {
            Log.d("EF", topic);
            return new EventsFetcher().downloadEvents(topic);
        }

        @Override
        protected void onPostExecute(List<Event> events){
            mEvents = events;
            mEventsRecyclerView.setAdapter(new EventAdapter(mEvents));
        }
    }

}
