package com.example.storetodoor;

import com.google.firebase.firestore.GeoPoint;

public class OrdersLocationPOJO {

    public OrdersLocationPOJO(String messenger_name, String quantity, String p_name, GeoPoint geoPoint) {
        this.messenger_name = messenger_name;
        this.quantity = quantity;
        this.p_name = p_name;
        this.geoPoint = geoPoint;
    }

    public String getMessenger_name() {
        return messenger_name;
    }

    public void setMessenger_name(String messenger_name) {
        this.messenger_name = messenger_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String messenger_name, quantity, p_name;
    public GeoPoint geoPoint;


}
