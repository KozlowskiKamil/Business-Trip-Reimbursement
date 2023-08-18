package com.example.demo;

public class Receipt {
    private String type;
    private double amount;

    public Receipt(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}