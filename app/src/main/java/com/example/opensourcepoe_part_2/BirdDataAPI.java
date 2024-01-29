package com.example.opensourcepoe_part_2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BirdDataAPI {

    @SerializedName("locId")
    private String locId;

    @SerializedName("locName")
    private String locName;

    @SerializedName("countryCode")
    private String countryCode;

    @SerializedName("subnational1Code")
    private String subnational1Code;

    @SerializedName("subnational2Code")
    private String subnational2Code;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("latestObsDt")
    private String latestObsDt;

    @SerializedName("numSpeciesAllTime")
    private int numSpeciesAllTime;

    // Getters for each field
    public String getLocId() {
        return locId;
    }

    public String getLocName() {
        return locName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getSubnational1Code() {
        return subnational1Code;
    }

    public String getSubnational2Code() {
        return subnational2Code;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getLatestObsDt() {
        return latestObsDt;
    }

    public int getNumSpeciesAllTime() {
        return numSpeciesAllTime;
    }

}


