package com.ahmadabuhasan.skripsi.connection.models;

public class ProductModel {
    private int id;
    private int category_id;
    private String product_name;
    private String product_code;
    private String product_barcode_symbology;
    private int product_quantity;
    private double product_cost;
    private double product_price;
    private String product_unit;
    private int product_stock_alert;
    private String product_order_tax;
    private String product_tax_type;
    private String product_note;
    private String created_at;
    private String updated_at;
    private String media;

    public ProductModel(int id, int category_id, String product_name, String product_code, String product_barcode_symbology, int product_quantity, double product_cost, double product_price, String product_unit, int product_stock_alert, String product_order_tax, String product_tax_type, String product_note, String created_at, String updated_at, String media) {
        this.id = id;
        this.category_id = category_id;
        this.product_name = product_name;
        this.product_code = product_code;
        this.product_barcode_symbology = product_barcode_symbology;
        this.product_quantity = product_quantity;
        this.product_cost = product_cost;
        this.product_price = product_price;
        this.product_unit = product_unit;
        this.product_stock_alert = product_stock_alert;
        this.product_order_tax = product_order_tax;
        this.product_tax_type = product_tax_type;
        this.product_note = product_note;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.media = media;
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

    public String getProduct_barcode_symbology() {
        return product_barcode_symbology;
    }

    public void setProduct_barcode_symbology(String product_barcode_symbology) {
        this.product_barcode_symbology = product_barcode_symbology;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public double getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(double product_cost) {
        this.product_cost = product_cost;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_unit() {
        return product_unit;
    }

    public void setProduct_unit(String product_unit) {
        this.product_unit = product_unit;
    }

    public int getProduct_stock_alert() {
        return product_stock_alert;
    }

    public void setProduct_stock_alert(int product_stock_alert) {
        this.product_stock_alert = product_stock_alert;
    }

    public String getProduct_order_tax() {
        return product_order_tax;
    }

    public void setProduct_order_tax(String product_order_tax) {
        this.product_order_tax = product_order_tax;
    }

    public String getProduct_tax_type() {
        return product_tax_type;
    }

    public void setProduct_tax_type(String product_tax_type) {
        this.product_tax_type = product_tax_type;
    }

    public String getProduct_note() {
        return product_note;
    }

    public void setProduct_note(String product_note) {
        this.product_note = product_note;
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

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
