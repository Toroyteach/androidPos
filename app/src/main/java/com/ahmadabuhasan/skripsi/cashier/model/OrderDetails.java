package com.ahmadabuhasan.skripsi.cashier.model;

public class OrderDetails {
    private int product_id;
    private String product_name;
    private String product_code;
    private int quantity;
    private double price;
    private double unit_price;
    private double sub_total;
    private double product_discount_amount;
    private String product_discount_type;
    private double product_tax_amount;

    public OrderDetails(int product_id, String product_name, String product_code, int quantity, double price, double unit_price, double sub_total, double product_discount_amount, String product_discount_type, double product_tax_amount) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_code = product_code;
        this.quantity = quantity;
        this.price = price;
        this.unit_price = unit_price;
        this.sub_total = sub_total;
        this.product_discount_amount = product_discount_amount;
        this.product_discount_type = product_discount_type;
        this.product_tax_amount = product_tax_amount;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    public double getProduct_discount_amount() {
        return product_discount_amount;
    }

    public void setProduct_discount_amount(double product_discount_amount) {
        this.product_discount_amount = product_discount_amount;
    }

    public String getProduct_discount_type() {
        return product_discount_type;
    }

    public void setProduct_discount_type(String product_discount_type) {
        this.product_discount_type = product_discount_type;
    }

    public double getProduct_tax_amount() {
        return product_tax_amount;
    }

    public void setProduct_tax_amount(double product_tax_amount) {
        this.product_tax_amount = product_tax_amount;
    }
}
