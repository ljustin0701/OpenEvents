package com.example.ljust.openevents;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ljust on 03/26/2016.
 */

public class Event implements Serializable {
    private Venue venue;
    private String name;
    private long time;

    public Event() {}

    static class Venue implements Serializable {
        private String city;
        private String country;
        private String address_1;
        private String name;

        public String getVenueCity() {
            return city;
        }

        public String getVenueCountry() {
            return country;
        }

        public String getVenueAddress() {
            return address_1;
        }

        public String getVenueName() {
            return name;
        }


    }
    public String getEventName() {
        return name;
    }

    public long getTimeinMillis() {
        return time;
    }

    public String getEventTime() {
        Date date = new Date(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return cal.getTime().toString();
    }

    public String getVenueLocation() {
        return venue.address_1 + "\n" + venue.city;
    }

    public String toString() {
        return venue.city + " " +
                venue.country + " " +
                venue.address_1 + " " +
                venue.name + " " +
                name + " " +
                time;
    }
}

