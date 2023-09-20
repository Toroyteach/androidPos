package com.ahmadabuhasan.skripsi.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.skripsi.CashierDashboard;
import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.WarehouseDashboard;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 24/10/2021
 */

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://megapos.co.ke/api";
    private static final String LOGIN_ENDPOINT = "/login";

    EditText etUsername, etPassword, etTenant;
    Button btnLogin;
    Spinner spinner;
    ProgressBar progressBar;

    private static long backPressed;
    public static String item;

    private RequestQueue requestQueue;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login POS");

        etUsername = findViewById(R.id.username);
        etUsername.setText("newton@kitalepos.com");
        etPassword = findViewById(R.id.password);
        etPassword.setText("12345678");
        etTenant = findViewById(R.id.tenantUrl);
        btnLogin = findViewById(R.id.login);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.loginProgressBar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        sharedPrefManager = SharedPrefManager.getInstance(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                item = spinner.getSelectedItem().toString();
//                if (etUsername.getText().toString().equals("Admin") && etPassword.getText().toString().equals("123456") && item.equals("Admin")) {
//                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
//                    startActivity(i);
//                } else if (etUsername.getText().toString().equals("Kasir") && etPassword.getText().toString().equals("123456") && item.equals("Cashier")) {
//                    Intent i = new Intent(LoginActivity.this, CashierDashboard.class);
//                    startActivity(i);
//                } else if (etUsername.getText().toString().equals("Gudang") && etPassword.getText().toString().equals("123456") && item.equals("Warehouse")) {
//                    Intent i = new Intent(LoginActivity.this, WarehouseDashboard.class);
//                    startActivity(i);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
//                }

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String tenantUrl = etTenant.getText().toString();

                login(username, password, tenantUrl);
            }
        });
    }

    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toasty.info((Context) this, (int) R.string.press_once_again_to_exit, Toasty.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    private void login(String username, String password, String tenantUrl) {
        String url = "https://"+tenantUrl+".megapos.co.ke/api"+LOGIN_ENDPOINT;

        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("email", username);
            jsonParams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONObject data = response.getJSONObject("data");
                                String token = data.getString("token");
                                String name = data.getString("name");
                                String role = data.getString("role");
                                String email = data.getString("email");
                                int isActive = data.getInt("is_active");
                                String phone = "not set";
                                String photoRl = data.getString("photoUrl");
                                boolean ctive = (isActive == 1);

                                // Create a User object with the response data
                                User user = new User(name, email, phone, token, role, ctive, photoRl, tenantUrl);

                                // Save the User object in SharedPrefManager
                                sharedPrefManager.saveUser(user);

                                String message = response.getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                                if(role.equals("Super Admin") || role.equals("Admin")){
                                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(i);
                                } else if (role.equals("Cashier")) {
                                    Intent i = new Intent(LoginActivity.this, CashierDashboard.class);
                                    startActivity(i);
                                } else if (role.equals("Warehouse")) {
                                    Intent i = new Intent(LoginActivity.this, WarehouseDashboard.class);
                                    startActivity(i);
                                }

                            } else {
                                String message = response.getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("LoginError", error.toString());
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }
}