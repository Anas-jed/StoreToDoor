package com.example.storetodoor;

import com.google.firebase.firestore.GeoPoint;

public class MessengerPOJO {


    public String uid;
    public String name;
    public String email;
    public String password;
    public String area;
    public String address;
    public GeoPoint geoPoint;

    public MessengerPOJO(String uid, String name, String email, String password, String area, String address, GeoPoint geoPoint) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.area = area;
        this.address = address;
        this.geoPoint = geoPoint;
    }

    public MessengerPOJO() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
