package com.ahmadabuhasan.skripsi.connection.models;

public class ExpenseModel {
    private int id;
    private int category_id;
    private String date;
    private String reference;
    private String details;
    private double amount;
    private String created_at;
    private String updated_at;
    private String categoryName;

    public ExpenseModel(int id, int category_id, String categoryName, String date, String reference, String details, double amount, String created_at, String updated_at) {
        this.id = id;
        this.category_id = category_id;
        this.categoryName = categoryName;
        this.date = date;
        this.reference = reference;
        this.details = details;
        this.amount = amount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAmount() {
        return String.valueOf(amount);
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
