package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.net.Uri;

public class Vehicle extends RouteElement {

    public long id;
    public String title;
    public Uri icon;

    public Vehicle(String title, Uri icon) {
        this.title = title;
        this.icon = icon;
    }
}
