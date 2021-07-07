package com.example.annuairelauriats.ui.home;

import com.google.android.gms.maps.model.LatLng;

public class Orglatlonid {
    private LatLng latLng;private long id;int count;
    Orglatlonid(LatLng latlon,long iden,int count){
        this.latLng=latlon;this.id=iden;this.count=count;
    }

    public int getCount() {
        return count;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public long getIden() {
        return id;
    }


}
