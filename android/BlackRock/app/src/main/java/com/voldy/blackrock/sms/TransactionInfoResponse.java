package com.voldy.blackrock.sms;

import com.google.gson.annotations.SerializedName;

public class TransactionInfoResponse {
    @SerializedName("account")
    private Account account;

    @SerializedName("balance")
    private Balance balance;

    @SerializedName("transaction")
    private Transaction transaction;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    // Add getters for account, balance, and transaction
}
