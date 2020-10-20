package com.example.storetodoor;

public class AreaPOJO {

    public String uid,area_name;

    public AreaPOJO(String uid, String area_name) {
        this.uid = uid;
        this.area_name = area_name;
    }

    public AreaPOJO() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
}
