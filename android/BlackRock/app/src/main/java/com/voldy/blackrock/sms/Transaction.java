package com.voldy.blackrock.sms;

public class Transaction {
    private String atype;
    private String ttype;
    private String amount;
    private String balance;

    public Transaction() {
    }

    public Transaction(String atype, String ttype, String amount, String balance) {
        this.atype = atype;
        this.ttype = ttype;
        this.amount = amount;
        this.balance = balance;
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
