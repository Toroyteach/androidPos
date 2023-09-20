package com.ahmadabuhasan.skripsi.connection.models;

public class CategoryModel {
    private int id;
    private String category_code;
    private String category_name;
    private String created_at;
    private String updated_at;

    public CategoryModel(int id, String category_code, String category_name, String created_at, String updated_at) {
        this.id = id;
        this.category_code = category_code;
        this.category_name = category_name;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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
