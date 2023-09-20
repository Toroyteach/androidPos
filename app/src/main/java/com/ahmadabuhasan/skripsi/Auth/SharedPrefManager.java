package com.ahmadabuhasan.skripsi.Auth;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "MegaPos";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_IS_ACTIVE = "key_is_active";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_ROLE = "key_role";
    private static final String KEY_PHOTO_URL = "key_photo_url";
    private static final String KEY_IS_LOGGED_IN = "false";
    private static final String KEY_TENANT_URLL = "tenant";

    private SharedPreferences sharedPreferences;
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putBoolean(KEY_IS_ACTIVE, user.isActive());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putString(KEY_PHOTO_URL, user.getPhotoUrl());
        editor.putString(KEY_TENANT_URLL, user.getTenantUrl());
        editor.apply();
    }

    public User getUser() {
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        boolean isActive = sharedPreferences.getBoolean(KEY_IS_ACTIVE, false);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        String role = sharedPreferences.getString(KEY_ROLE, null);
        String phone = "not set";
        String photoUrl = sharedPreferences.getString(KEY_PHOTO_URL, null);
        String tenantUrl = sharedPreferences.getString(KEY_TENANT_URLL, null);

        return new User(username, email, phone, token, role, isActive, photoUrl, tenantUrl);
    }

    public void clearUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_IS_ACTIVE);
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_ROLE);
        editor.remove(KEY_PHOTO_URL);
        editor.remove(KEY_TENANT_URLL);
        editor.apply();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    // Retrieve the login state (returns true if user is logged in, false otherwise)
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

}

