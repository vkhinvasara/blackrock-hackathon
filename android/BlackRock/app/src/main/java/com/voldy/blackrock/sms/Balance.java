package com.voldy.blackrock.sms;

public class Balance {
    private String available;
    private String outstanding;

    public Balance() {
    }

    public Balance(String available, String outstanding) {
        this.available = available;
        this.outstanding = outstanding;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    // Getters and setters
}
