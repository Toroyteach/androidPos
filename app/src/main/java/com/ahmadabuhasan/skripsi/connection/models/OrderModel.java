package com.ahmadabuhasan.skripsi.connection.models;

public class OrderModel {
    private int id;
    private String date;
    private String reference;
    private int customer_id;
    private String customer_name;
    private double tax_percentage;
    private double tax_amount;
    private double discount_percentage;
    private double discount_amount;
    private double shipping_amount;
    private double total_amount;
    private double paid_amount;
    private double due_amount;
    private String status;
    private String payment_status;
    private String payment_method;
    private String note;
    private String created_at;
    private String updated_at;

    public OrderModel(int id, String date, String reference, int customer_id, String customer_name, double tax_percentage, double tax_amount, double discount_percentage, double discount_amount, double shipping_amount, double total_amount, double paid_amount, double due_amount, String status, String payment_status, String payment_method, String note, String created_at, String updated_at) {
        this.id = id;
        this.date = date;
        this.reference = reference;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.tax_percentage = tax_percentage;
        this.tax_amount = tax_amount;
        this.discount_percentage = discount_percentage;
        this.discount_amount = discount_amount;
        this.shipping_amount = shipping_amount;
        this.total_amount = total_amount;
        this.paid_amount = paid_amount;
        this.due_amount = due_amount;
        this.status = status;
        this.payment_status = payment_status;
        this.payment_method = payment_method;
        this.note = note;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public double getTax_percentage() {
        return tax_percentage;
    }

    public void setTax_percentage(double tax_percentage) {
        this.tax_percentage = tax_percentage;
    }

    public double getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(double tax_amount) {
        this.tax_amount = tax_amount;
    }

    public double getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(double discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public double getShipping_amount() {
        return shipping_amount;
    }

    public void setShipping_amount(double shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(double paid_amount) {
        this.paid_amount = paid_amount;
    }

    public double getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(double due_amount) {
        this.due_amount = due_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
