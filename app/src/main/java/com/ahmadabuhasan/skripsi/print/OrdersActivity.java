package com.ahmadabuhasan.skripsi.print;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahmadabuhasan.skripsi.Auth.LoginActivity;
import com.ahmadabuhasan.skripsi.Auth.SharedPrefManager;
import com.ahmadabuhasan.skripsi.Auth.User;
import com.ahmadabuhasan.skripsi.CashierDashboard;
import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.WarehouseDashboard;
import com.ahmadabuhasan.skripsi.adapter.OrderAdapter;
import com.ahmadabuhasan.skripsi.adapter.PosProductAdapter;
import com.ahmadabuhasan.skripsi.adapter.ProductCategoryAdapter;
import com.ahmadabuhasan.skripsi.connection.ApiRequestHandler;
import com.ahmadabuhasan.skripsi.connection.models.CategoryModel;
import com.ahmadabuhasan.skripsi.connection.models.OrderModel;
import com.ahmadabuhasan.skripsi.connection.models.ProductModel;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.ahmadabuhasan.skripsi.Auth.LoginActivity.item;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class OrdersActivity extends AppCompatActivity {

    private static final String TAG = "";
    EditText editText_Search;
    TextView textView_NoProducts;
    ImageView imgNoProduct;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    ProgressBar progressBar;
    private ApiRequestHandler apiRequestHandler;
    private List<OrderModel> orderModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order_history);

        this.editText_Search = findViewById(R.id.et_search_order);
        this.textView_NoProducts = findViewById(R.id.tv_no_order);
        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.recyclerView = findViewById(R.id.order_recyclerview);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.recyclerView.setHasFixedSize(true);

        this.textView_NoProducts.setVisibility(View.GONE);
        this.imgNoProduct.setVisibility(View.GONE);

        this.progressBar = findViewById(R.id.ordersListProgressBar);

        apiRequestHandler = new ApiRequestHandler(this);
        getDataItems();

//        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
//        databaseAccess.open();
//        List<HashMap<String, String>> orderList = databaseAccess.getOrderList();
//        if (orderList.size() <= 0) {
//            Toasty.info(this, (int) R.string.no_order_found, Toasty.LENGTH_SHORT).show();
//            this.recyclerView.setVisibility(View.GONE);
//            this.imgNoProduct.setVisibility(View.VISIBLE);
//            this.imgNoProduct.setImageResource(R.drawable.not_found);
//            this.textView_NoProducts.setVisibility(View.VISIBLE);
//        } else {
//            OrderAdapter orderAdapter1 = new OrderAdapter(this, orderList);
//            this.orderAdapter = orderAdapter1;
//            this.recyclerView.setAdapter(orderAdapter1);
//        }

        this.editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(OrdersActivity.this);
//                databaseAccess.open();
//                List<HashMap<String, String>> searchOrder = databaseAccess.searchOrderList(s.toString());
//                if (searchOrder.size() <= 0) {
//                    OrdersActivity.this.recyclerView.setVisibility(View.GONE);
//                    OrdersActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
//                    OrdersActivity.this.imgNoProduct.setImageResource(R.drawable.no_data);
//                    return;
//                }
//                OrdersActivity.this.recyclerView.setVisibility(View.VISIBLE);
//                OrdersActivity.this.imgNoProduct.setVisibility(View.GONE);
//                OrdersActivity.this.recyclerView.setAdapter(new OrderAdapter(OrdersActivity.this, searchOrder));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getDataItems(){
        //Get Categories and products
        progressBar.setVisibility(View.VISIBLE);
        apiRequestHandler.getOrders(
                orders -> {
                    // Handle the list of products here
                    if(orders != null) {

                        this.imgNoProduct.setVisibility(View.GONE);
                        this.orderModelList = orders;
                        OrderAdapter orderAdapter1 = new OrderAdapter(this, orders);
                        this.orderAdapter = orderAdapter1;
                        this.recyclerView.setAdapter(orderAdapter1);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        Toasty.info(this, "No Orders Found", Toasty.LENGTH_SHORT).show();
                        this.imgNoProduct.setImageResource(R.drawable.no_data);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    // Handle error
                    Log.e(TAG, "Error Fetching Orders: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
        );
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {

        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String role = user.getRole();

        if(role.equals("Super Admin") || role.equals("Admin")){
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        } else if (role.equals("Cashier")) {
            Intent i = new Intent(this, CashierDashboard.class);
            startActivity(i);
        } else if (role.equals("Warehouse")) {
            Intent i = new Intent(this, WarehouseDashboard.class);
            startActivity(i);
        }

        super.onBackPressed();
    }
}