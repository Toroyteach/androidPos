package com.ahmadabuhasan.skripsi.connection.models;

public class CustomerModel {
    private int id;
    private String customer_name;
    private String customer_email;
    private String customer_phone;
    private String customer_krapin;
    private String city;
    private String country;
    private String address;
    private String created_at;
    private String updated_at;

    public CustomerModel(int id, String customer_name, String customer_email, String customer_phone, String customer_krapin, String city, String country, String address, String created_at, String updated_at) {
        this.id = id;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_phone = customer_phone;
        this.customer_krapin = customer_krapin;
        this.city = city;
        this.country = country;
        this.address = address;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_krapin() {
        return customer_krapin;
    }

    public void setCustomer_krapin(String customer_krapin) {
        this.customer_krapin = customer_krapin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
