package com.voldy.blackrock.sms;

public class SmsResponse {
    String transactionType;
    String amount;

    public SmsResponse() {
        transactionType = "";
        amount = "0";
    }

    public SmsResponse(String transactionType, String amount) {
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
