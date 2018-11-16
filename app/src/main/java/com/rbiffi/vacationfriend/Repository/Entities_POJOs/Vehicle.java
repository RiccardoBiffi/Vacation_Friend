package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.net.Uri;

public class Vehicle extends RouteElement {

    public long id;
    private final String title;
    private final Uri icon;

    public Vehicle(String title, Uri icon) {
        this.title = title;
        this.icon = icon;
    }
}
