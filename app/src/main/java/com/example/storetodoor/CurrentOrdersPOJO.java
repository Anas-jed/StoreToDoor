package com.example.storetodoor;

import com.google.firebase.Timestamp;

public class CurrentOrdersPOJO {
    String c_name,complete_address,p_name,quantity,status,area;
    Timestamp o_date;

    public CurrentOrdersPOJO(String c_name, String complete_address, String p_name, String quantity, String status, String area, Timestamp o_date) {
        this.c_name = c_name;
        this.complete_address = complete_address;
        this.p_name = p_name;
        this.quantity = quantity;
        this.status = status;
        this.area = area;
        this.o_date = o_date;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getComplete_address() {
        return complete_address;
    }

    public void setComplete_address(String complete_address) {
        this.complete_address = complete_address;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Timestamp getO_date() {
        return o_date;
    }

    public void setO_date(Timestamp o_date) {
        this.o_date = o_date;
    }





}
