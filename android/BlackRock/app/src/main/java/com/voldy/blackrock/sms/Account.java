package com.voldy.blackrock.sms;

public class Account {
    private String type;
    private String name;
    private String number;

    public Account() {
    }

    public Account(String type, String name, String number) {
        this.type = type;
        this.name = name;
        this.number = number;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
