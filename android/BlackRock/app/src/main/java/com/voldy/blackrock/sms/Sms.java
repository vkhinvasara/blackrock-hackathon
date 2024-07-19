package com.voldy.blackrock.sms;

public class Sms {
    private String address;
    private String body;
    private String date;

    public Sms(String address, String body, String date) {
        this.address = address;
        this.body = body;
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }
}
