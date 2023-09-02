package com.example.myapplication;

public class TransactionHistoryModel {
    String transactionHistoryDate;
    String transactionHistoryStatus;
    String transactionHistoryCSName;
    String transactionHistoryCSLocation;
    String transactionHistoryAmount;


    public TransactionHistoryModel(String transactionHistoryDate, String transactionHistoryStatus,
                                   String transactionHistoryCSName, String transactionHistoryCSLocation,
                                   String transactionHistoryAmount) {
        this.transactionHistoryDate = transactionHistoryDate;
        this.transactionHistoryStatus = transactionHistoryStatus;
        this.transactionHistoryCSName = transactionHistoryCSName;
        this.transactionHistoryCSLocation = transactionHistoryCSLocation;
        this.transactionHistoryAmount = transactionHistoryAmount;
    }

    public String getTransactionHistoryDate() {
        return transactionHistoryDate;
    }

    public String getTransactionHistoryStatus() {
        return transactionHistoryStatus;
    }

    public String getTransactionHistoryCSName() {
        return transactionHistoryCSName;
    }

    public String getTransactionHistoryCSLocation() {
        return transactionHistoryCSLocation;
    }

    public String getTransactionHistoryAmount() {
        return transactionHistoryAmount;
    }
}
