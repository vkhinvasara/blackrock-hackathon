package com.voldy.blackrock.sms;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.UUID;

public class SmsModel implements Serializable {

    String smsID;
    String smsCategory;
    int smsAmount;
    String smsMerchantName;
    String smsTransactionType;
    long smsTimeStamp;

    public SmsModel() {
        this.smsID = UUID.randomUUID().toString();
        this.smsCategory = "Miscellaneous";
        this.smsAmount = 0;
        this.smsMerchantName = "";
        this.smsTransactionType = "";
        this.smsTimeStamp = 0;
    }

    public SmsModel(String smsID, String smsCategory, int smsAmount, String smsMerchantName, String smsTransactionType, long smsTimeStamp) {
        this.smsID = smsID;
        this.smsCategory = smsCategory;
        this.smsAmount = smsAmount;
        this.smsMerchantName = smsMerchantName;
        this.smsTransactionType = smsTransactionType;
        this.smsTimeStamp = smsTimeStamp;
    }

    public String getSmsID() {
        return smsID;
    }

    public void setSmsID(String smsID) {
        this.smsID = smsID;
    }

    public String getSmsCategory() {
        return smsCategory;
    }

    public void setSmsCategory(String smsCategory) {
        this.smsCategory = smsCategory;
    }

    public int getSmsAmount() {
        return smsAmount;
    }

    public void setSmsAmount(int smsAmount) {
        this.smsAmount = smsAmount;
    }

    public String getSmsMerchantName() {
        return smsMerchantName;
    }

    public void setSmsMerchantName(String smsMerchantName) {
        this.smsMerchantName = smsMerchantName;
    }

    public String getSmsTransactionType() {
        return smsTransactionType;
    }

    public void setSmsTransactionType(String smsTransactionType) {
        this.smsTransactionType = smsTransactionType;
    }

    public long getSmsTimeStamp() {
        return smsTimeStamp;
    }

    public void setSmsTimeStamp(long smsTimeStamp) {
        this.smsTimeStamp = smsTimeStamp;
    }

    @Override
    public String toString() {
        return "SmsModel{" +
                "smsID='" + smsID + '\'' +
                ", smsCategory='" + smsCategory + '\'' +
                ", smsAmount=" + smsAmount +
                ", smsMerchantName='" + smsMerchantName + '\'' +
                ", smsTransactionType='" + smsTransactionType + '\'' +
                ", smsTimeStamp=" + smsTimeStamp +
                '}';
    }
}
