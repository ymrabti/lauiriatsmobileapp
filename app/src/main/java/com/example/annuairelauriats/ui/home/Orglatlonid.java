package com.example.annuairelauriats.ui.home;

import com.google.android.gms.maps.model.LatLng;

public class Orglatlonid {
    private LatLng latLng;long id;
    Orglatlonid(LatLng latlon,long iden){
        this.latLng=latlon;this.id=iden;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public long getIden() {
        return id;
    }
}
