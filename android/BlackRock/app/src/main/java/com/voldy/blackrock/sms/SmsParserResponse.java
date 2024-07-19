package com.voldy.blackrock.sms;

public class SmsParserResponse {
    int amount;
    String transactionType;
    String merchantName;

    public SmsParserResponse() {
        this.amount = 0;
        this.transactionType = "";
        this.merchantName = "";
    }

    public SmsParserResponse(int amount, String transactionType, String merchantName) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.merchantName = merchantName;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
