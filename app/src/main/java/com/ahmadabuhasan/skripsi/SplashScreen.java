package com.ahmadabuhasan.skripsi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.skripsi.Auth.LoginActivity;
import com.ahmadabuhasan.skripsi.Auth.SharedPrefManager;
import com.ahmadabuhasan.skripsi.Auth.User;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/*
 * Created by Ahmad Abu Hasan on 04/10/2021
 */

public class SplashScreen extends AppCompatActivity {

    public static int splashTimeOut = CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        User user =  SharedPrefManager.getInstance(this).getUser();
        final String role = user.getRole();

        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (SharedPrefManager.getInstance(SplashScreen.this).isLoggedIn()) {

                    if(Objects.equals(role, "Super Admin") || Objects.equals(role, "Admin")){

                        finish();
                        startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                    } else if (Objects.equals(role, "Cashier")) {

                        finish();
                        startActivity(new Intent(SplashScreen.this, CashierDashboard.class));
                    } else if (Objects.equals(role, "Warehouse")) {

                        finish();
                        startActivity(new Intent(SplashScreen.this, WarehouseDashboard.class));
                    }

                } else {
                    finish();
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                }
            }
        }, (long) splashTimeOut);
    }
}