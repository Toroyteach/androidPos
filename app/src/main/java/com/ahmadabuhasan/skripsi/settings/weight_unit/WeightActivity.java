package com.ahmadabuhasan.skripsi.settings.weight_unit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ahmadabuhasan.skripsi.Auth.SharedPrefManager;
import com.ahmadabuhasan.skripsi.Auth.User;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.WeightAdapter;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.settings.SettingsActivity;
import com.ahmadabuhasan.skripsi.settings.SettingsWarehouse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.ahmadabuhasan.skripsi.Auth.LoginActivity.item;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class WeightActivity extends AppCompatActivity {

    EditText editText_Search;
    FloatingActionButton floatingActionButton_fabAdd;
    ImageView imgNoProduct;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.weight_unit);

        this.editText_Search = findViewById(R.id.et_weight_search);
        this.floatingActionButton_fabAdd = findViewById(R.id.fab_add);
        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.recyclerView = findViewById(R.id.weight_recyclerview);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> weightData = databaseAccess.getWeightUnit();
        Log.d("data", "" + weightData.size());

        if (weightData.size() <= 0) {
            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.imgNoProduct.setImageResource(R.drawable.no_data);
        } else {
            this.imgNoProduct.setVisibility(View.GONE);
            this.recyclerView.setAdapter(new WeightAdapter(this, weightData));
        }

        this.floatingActionButton_fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeightActivity.this.startActivity(new Intent(WeightActivity.this, AddWeightActivity.class));
            }
        });

        this.editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(WeightActivity.this);
                databaseAccess.open();
                List<HashMap<String, String>> searchWeightList = databaseAccess.searchProductWeight(s.toString());
                if (searchWeightList.size() <= 0) {
                    WeightActivity.this.recyclerView.setVisibility(View.GONE);
                    WeightActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
                    WeightActivity.this.imgNoProduct.setImageResource(R.drawable.no_data);
                    return;
                }
                WeightActivity.this.recyclerView.setVisibility(View.VISIBLE);
                WeightActivity.this.imgNoProduct.setVisibility(View.GONE);
                WeightActivity.this.recyclerView.setAdapter(new WeightAdapter(WeightActivity.this, searchWeightList));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String role = user.getRole();

        if (role.equals("Warehouse")) {
            startActivity(new Intent(this, SettingsWarehouse.class));
        } else {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        super.onBackPressed();
    }
}