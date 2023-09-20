package com.ahmadabuhasan.skripsi.cashier.model;

import java.util.List;

public class Order {
    private String customer_name;
    private int customer_id;
    private double tax_percentage;
    private double discount_percentage;
    private double shipping_amount;
    private double paid_amount;
    private double total_amount;
    private String payment_method;
    private String note;
    private double tax_amount;
    private double discount_amount;
    private List<OrderDetails> items;

    public Order(String customer_name, int customer_id, double tax_percentage, double discount_percentage, double shipping_amount, double paid_amount, double total_amount, String payment_method, String note, double tax_amount, double discount_amount, List<OrderDetails> items) {
        this.customer_name = customer_name;
        this.customer_id = customer_id;
        this.tax_percentage = tax_percentage;
        this.discount_percentage = discount_percentage;
        this.shipping_amount = shipping_amount;
        this.paid_amount = paid_amount;
        this.total_amount = total_amount;
        this.payment_method = payment_method;
        this.note = note;
        this.tax_amount = tax_amount;
        this.discount_amount = discount_amount;
        this.items = items;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public double getTax_percentage() {
        return tax_percentage;
    }

    public void setTax_percentage(double tax_percentage) {
        this.tax_percentage = tax_percentage;
    }

    public double getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(double discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public double getShipping_amount() {
        return shipping_amount;
    }

    public void setShipping_amount(double shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public double getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(double paid_amount) {
        this.paid_amount = paid_amount;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
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

    public double getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(double tax_amount) {
        this.tax_amount = tax_amount;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public List<OrderDetails> getItems() {
        return items;
    }

    public void setItems(List<OrderDetails> items) {
        this.items = items;
    }
}
