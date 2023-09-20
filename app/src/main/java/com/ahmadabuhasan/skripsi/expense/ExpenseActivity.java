package com.ahmadabuhasan.skripsi.expense;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.Auth.SharedPrefManager;
import com.ahmadabuhasan.skripsi.Auth.User;
import com.ahmadabuhasan.skripsi.CashierDashboard;
import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.WarehouseDashboard;
import com.ahmadabuhasan.skripsi.adapter.ExpenseAdapter;
import com.ahmadabuhasan.skripsi.adapter.ProductAdapter;
import com.ahmadabuhasan.skripsi.connection.ApiRequestHandler;
import com.ahmadabuhasan.skripsi.connection.models.ExpenseModel;
import com.ahmadabuhasan.skripsi.connection.models.OrderDetailsModel;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.ahmadabuhasan.skripsi.Auth.LoginActivity.item;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class ExpenseActivity extends AppCompatActivity {

    private static final String TAG = "";
    EditText editText_Search;
    FloatingActionButton floatingActionButton_fabAdd;
    ImageView imgNoExpense;
    private RecyclerView recyclerView;
    ExpenseAdapter productAdapter;

    ProgressBar progressBar;

    private ApiRequestHandler apiRequestHandler;
    private List<ExpenseModel> expensesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_expense);

        apiRequestHandler = new ApiRequestHandler(this);

        this.progressBar = findViewById(R.id.expenseDetailsListProgressBar);

        this.editText_Search = findViewById(R.id.et_expense_search);
        this.floatingActionButton_fabAdd = findViewById(R.id.fab_add);
        this.imgNoExpense = findViewById(R.id.image_no_expense);
        this.recyclerView = findViewById(R.id.expense_recyclerview);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        this.floatingActionButton_fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseActivity.this.startActivity(new Intent(ExpenseActivity.this, AddExpenseActivity.class));
            }
        });

        getExpensesList();
//        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
//        databaseAccess.open();
//        List<HashMap<String, String>> productData = databaseAccess.getAllExpense();
//        Log.d("data", "" + productData.size());
//        if (productData.size() <= 0) {
//            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
//            this.imgNoExpense.setImageResource(R.drawable.no_data);
//        } else {
//            this.imgNoExpense.setVisibility(View.GONE);
//            ExpenseAdapter expenseAdapter = new ExpenseAdapter(this, productData);
//            this.productAdapter = expenseAdapter;
//            this.recyclerView.setAdapter(expenseAdapter);
//        }

        this.editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ExpenseActivity.this);
//                databaseAccess.open();
//                List<HashMap<String, String>> searchExpenseList = databaseAccess.searchExpense(s.toString());
//                if (searchExpenseList.size() <= 0) {
//                    ExpenseActivity.this.recyclerView.setVisibility(View.GONE);
//                    ExpenseActivity.this.imgNoExpense.setVisibility(View.VISIBLE);
//                    ExpenseActivity.this.imgNoExpense.setImageResource(R.drawable.no_data);
//                    return;
//                }
//                ExpenseActivity.this.recyclerView.setVisibility(View.VISIBLE);
//                ExpenseActivity.this.imgNoExpense.setVisibility(View.GONE);
//                ExpenseActivity expenseActivity = ExpenseActivity.this;
//                expenseActivity.productAdapter = new ExpenseAdapter(expenseActivity, searchExpenseList);
//                ExpenseActivity.this.recyclerView.setAdapter(ExpenseActivity.this.productAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getExpensesList(){
        progressBar.setVisibility(View.VISIBLE);
        this.expensesList.clear();
        apiRequestHandler.getEpenses(
                expenses -> {
                    // Handle the list of products here
                    if(expenses != null) {

                        this.imgNoExpense.setVisibility(View.GONE);
                        this.expensesList = expenses;
                        ExpenseAdapter productAdapter1 = new ExpenseAdapter(this, expenses);
                        this.productAdapter = productAdapter1;
                        this.recyclerView.setAdapter(productAdapter1);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        Toasty.info(this, (int) R.string.no_expense_found, Toasty.LENGTH_SHORT).show();
                        this.imgNoExpense.setImageResource(R.drawable.no_data);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    // Handle error
                    Log.e(TAG, "Error fetching Products: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
        );
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

        if(role.equals("Super Admin") || role.equals("Admin")){
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        } else if (role.equals("Cashier")) {
            startActivity(new Intent(this, CashierDashboard.class));
        } else if (role.equals("Warehouse")) {
            startActivity(new Intent(this, WarehouseDashboard.class));
        }
        super.onBackPressed();

        //super.onBackPressed();
    }
}