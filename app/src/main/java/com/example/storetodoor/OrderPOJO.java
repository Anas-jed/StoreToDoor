package com.example.storetodoor;

import com.google.firebase.Timestamp;

public class OrderPOJO {
    String c_uid,complete_address,p_docid,quantity,status,area;
    Timestamp o_date;

    public OrderPOJO() {
    }

    public OrderPOJO(String c_uid, String complete_address, String p_docid, String quantity, String status, String area, Timestamp timestamp) {
        this.c_uid = c_uid;
        this.complete_address = complete_address;
        this.p_docid = p_docid;
        this.quantity = quantity;
        this.status = status;
        this.area = area;
        this.o_date = timestamp;
    }

    public String getC_uid() {
        return c_uid;
    }

    public void setC_uid(String c_uid) {
        this.c_uid = c_uid;
    }

    public String getComplete_address() {
        return complete_address;
    }

    public void setComplete_address(String complete_address) {
        this.complete_address = complete_address;
    }

    public String getP_docid() {
        return p_docid;
    }

    public void setP_docid(String p_docid) {
        this.p_docid = p_docid;
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
