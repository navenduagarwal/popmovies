package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * data structure for trailer with parcelable
 */
public class Trailer implements Parcelable{
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>(){
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };

    private String name;
    private String key;
    private String site;

    public Trailer(String name, String key, String site) {
        this.name = name;
        this.key = key;
        this.site = site;
    }

    public Trailer() {
        super();
    }

    public Trailer(Parcel p){
        name = p.readString();
        key = p.readString();
        site = p.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(key);
        parcel.writeString(site);
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }
}
