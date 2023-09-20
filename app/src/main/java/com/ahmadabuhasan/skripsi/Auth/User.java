package com.ahmadabuhasan.skripsi.Auth;

public class User {

    private int id;
    private String username, email, phone, token, role, photoUrl, tenantUrl;
    private boolean isActive;

    public String getTenantUrl() {
        return tenantUrl;
    }

    public void setTenantUrl(String tenantUrl) {
        this.tenantUrl = tenantUrl;
    }

    public User(String username, String email, String phone, String token, String role, boolean isActive, String photoUrl, String tenantUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.isActive = isActive;
        this.photoUrl = photoUrl;
        this.role = role;
        this.tenantUrl = tenantUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
