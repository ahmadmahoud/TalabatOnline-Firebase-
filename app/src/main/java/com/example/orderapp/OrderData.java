package com.example.orderapp;

import java.io.Serializable;

public class OrderData implements Serializable {
    private String orderId, userId, description, phone,
            firstLocation, lastLocation,
            date, time,
            providerId,state;
    private boolean accept,finished;

    public OrderData(String orderId, String userId, String description, String phone, String firstLocation, String lastLocation, String date, String time, String providerId, String state, boolean accept, boolean finished) {
        this.orderId = orderId;
        this.userId = userId;
        this.description = description;
        this.phone = phone;
        this.firstLocation = firstLocation;
        this.lastLocation = lastLocation;
        this.date = date;
        this.time = time;
        this.providerId = providerId;
        this.state = state;
        this.accept = accept;
        this.finished = finished;
    }

    public OrderData() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(String firstLocation) {
        this.firstLocation = firstLocation;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}

