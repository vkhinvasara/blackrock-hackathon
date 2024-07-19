package com.voldy.blackrock.sms;

public class ApiResponse {
    private Account account;
    private Balance balance;
    private Transaction transaction;

    public ApiResponse() {
    }

    public ApiResponse(Account account, Balance balance, Transaction transaction) {
        this.account = account;
        this.balance = balance;
        this.transaction = transaction;
    }

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

    // Getters and setters
}
