package com.ahmadabuhasan.skripsi.cashier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.Auth.SharedPrefManager;
import com.ahmadabuhasan.skripsi.Auth.User;
import com.ahmadabuhasan.skripsi.CashierDashboard;
import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.WarehouseDashboard;
import com.ahmadabuhasan.skripsi.adapter.CategoryAdapter;
import com.ahmadabuhasan.skripsi.adapter.PosProductAdapter;
import com.ahmadabuhasan.skripsi.adapter.ProductAdapter;
import com.ahmadabuhasan.skripsi.adapter.ProductCategoryAdapter;
import com.ahmadabuhasan.skripsi.connection.ApiRequestHandler;
import com.ahmadabuhasan.skripsi.connection.models.CategoryModel;
import com.ahmadabuhasan.skripsi.connection.models.ProductModel;
import com.ahmadabuhasan.skripsi.data.ProductActivity;
import com.ahmadabuhasan.skripsi.data.ProductSearchFunction;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class PosActivity extends AppCompatActivity {

    private static final String TAG = "";
    @SuppressLint("StaticFieldLeak")
    public static TextView textView_Count;
    @SuppressLint("StaticFieldLeak")
    public static EditText editText_Search;

    PosProductAdapter productAdapter;
    ProductCategoryAdapter categoryAdapter;

    ImageView imgBack;
    ImageView imgCart;
    ImageView imgScanner;
    ImageView imgNoProduct;

    TextView textView_NoProducts;
    TextView textView_Reset;
    Button refreshData;

    private RecyclerView recyclerView;
    private RecyclerView categoryRecyclerView;
    int spanCount = 2;

    ProgressBar progressBar;
    private ApiRequestHandler apiRequestHandler;
    private List<CategoryModel> categoryList = new ArrayList<>();
    private List<ProductModel> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_product);
        getSupportActionBar().hide();

        textView_Count = findViewById(R.id.tv_count);
        editText_Search = findViewById(R.id.et_search);

        this.progressBar = findViewById(R.id.productsListProgressBar);
        this.refreshData = findViewById(R.id.pos_refresh);

        this.imgBack = findViewById(R.id.img_back);
        this.imgCart = findViewById(R.id.img_cart);
        this.imgScanner = findViewById(R.id.img_scan_code);
        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.textView_NoProducts = findViewById(R.id.tv_no_products);
        this.textView_Reset = findViewById(R.id.tv_reset);
        this.recyclerView = findViewById(R.id.data_recyclerview);
        this.categoryRecyclerView = findViewById(R.id.category_recyclerview);

        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        this.imgNoProduct.setVisibility(View.GONE);
        this.textView_NoProducts.setVisibility(View.GONE);

        this.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        this.recyclerView.setHasFixedSize(true);

        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            this.spanCount = 4;
        } else if ((getResources().getConfiguration().screenLayout & 15) == 2) {
            this.spanCount = 2;
        } else if ((getResources().getConfiguration().screenLayout & 15) == 1) {
            this.spanCount = 2;
        } else {
            this.spanCount = 4;
        }

        this.imgBack.setOnClickListener(v -> onBackPressed());

        databaseAccess.open();
        int count = databaseAccess.getCartItemCount();
        if (count == 0) {
            textView_Count.setVisibility(View.INVISIBLE);
        } else {
            textView_Count.setVisibility(View.VISIBLE);
            textView_Count.setText(String.valueOf(count));
        }

        this.imgCart.setOnClickListener(v -> PosActivity.this.startActivity(new Intent(PosActivity.this, ProductCart.class)));

        this.imgScanner.setOnClickListener(v -> PosActivity.this.startActivity(new Intent(PosActivity.this, ScannerActivity.class)));


        editText_Search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ProductSearchFunction.search(productList, s.toString(), new ProductSearchFunction.SearchListener() {
                    @Override
                    public void onSearch(List<ProductModel> resultList) {
                        if (resultList.size() <= 0) {
                            PosActivity.this.recyclerView.setVisibility(View.GONE);
                            PosActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
                            PosActivity.this.imgNoProduct.setImageResource(R.drawable.no_data);
                            return;
                        }
                        PosActivity.this.recyclerView.setVisibility(View.VISIBLE);
                        PosActivity.this.imgNoProduct.setVisibility(View.GONE);
                        PosActivity productActivity = PosActivity.this;
                        productActivity.productAdapter = new PosProductAdapter(productActivity, resultList);
                        PosActivity.this.recyclerView.setAdapter(PosActivity.this.productAdapter);
                    }
                });
            }

            public void afterTextChanged(Editable s) {

            }
        });

        this.textView_Reset.setOnClickListener(v -> {
            if (productList.isEmpty()) {
                PosActivity.this.recyclerView.setVisibility(View.GONE);
                PosActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
                PosActivity.this.imgNoProduct.setImageResource(R.drawable.not_found);
                PosActivity.this.textView_NoProducts.setVisibility(View.VISIBLE);
                return;
            }
            PosActivity.this.recyclerView.setVisibility(View.VISIBLE);
            PosActivity.this.imgNoProduct.setVisibility(View.GONE);
            PosActivity.this.textView_NoProducts.setVisibility(View.GONE);
            PosActivity posActivity = PosActivity.this;
            posActivity.productAdapter = new PosProductAdapter(posActivity, productList);
            PosActivity.this.recyclerView.setAdapter(PosActivity.this.productAdapter);
        });

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        categoryRecyclerView.setHasFixedSize(true);

        apiRequestHandler = new ApiRequestHandler(this);
        getDataItems();

        this.refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataItems();
            }
        });
//        databaseAccess.open();
//        List<HashMap<String, String>> categoryData = databaseAccess.getProductCategory();
//        Log.d("data", "" + categoryData.size());
//        if (categoryData.isEmpty()) {
//            Toasty.info(this, (int) R.string.no_data_found, Toasty.LENGTH_SHORT).show();
//        } else {
//            ProductCategoryAdapter productCategoryAdapter = new ProductCategoryAdapter(this, categoryData, this.recyclerView, this.imgNoProduct, this.textView_NoProducts);
//            this.categoryAdapter = productCategoryAdapter;
//            categoryRecyclerView.setAdapter(productCategoryAdapter);
//        }

//        databaseAccess.open();
//        List<HashMap<String, String>> productList = databaseAccess.getProducts();
//        if (productList.size() <= 0) {
//            this.recyclerView.setVisibility(View.GONE);
//            this.imgNoProduct.setVisibility(View.VISIBLE);
//            this.imgNoProduct.setImageResource(R.drawable.not_found);
//            this.textView_NoProducts.setVisibility(View.VISIBLE);
//        } else {
//            this.recyclerView.setVisibility(View.VISIBLE);
//            this.imgNoProduct.setVisibility(View.GONE);
//            this.textView_NoProducts.setVisibility(View.GONE);
//            PosProductAdapter posProductAdapter = new PosProductAdapter(this, productList);
//            this.productAdapter = posProductAdapter;
//            this.recyclerView.setAdapter(posProductAdapter);
//        }
    }

    public void getDataItems(){
        //Get Categories and products
        this.refreshData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        apiRequestHandler.getCategories(
                categories -> {
                    // Handle the list of products here
                    if(categories != null) {

                        this.imgNoProduct.setVisibility(View.GONE);
                        this.categoryList = categories;
                        ProductCategoryAdapter categoryAdapter1 = new ProductCategoryAdapter(this, categories, this.recyclerView, this.imgNoProduct, this.textView_NoProducts, this::onCatClick);
                        this.categoryAdapter = categoryAdapter1;
                        this.categoryRecyclerView.setAdapter(categoryAdapter1);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        Toasty.info(this, "No Categories Found", Toasty.LENGTH_SHORT).show();
                        this.imgNoProduct.setImageResource(R.drawable.no_data);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    // Handle error
                    Log.e(TAG, "Error Fetching Categories: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
        );

        apiRequestHandler.getProducts(
                products -> {
                    // Handle the list of products here
                    if(products != null) {

                        this.imgNoProduct.setVisibility(View.GONE);
                        this.productList = products;
                        PosProductAdapter productAdapter1 = new PosProductAdapter(this, products);
                        this.productAdapter = productAdapter1;
                        this.recyclerView.setAdapter(productAdapter1);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        Toasty.info(this, (int) R.string.no_product_found, Toasty.LENGTH_SHORT).show();
                        this.imgNoProduct.setImageResource(R.drawable.no_data);
                    }
                },
                error -> {
                    // Handle error
                    Log.e(TAG, "Error Fetching Products: " + error.getMessage());
                }
        );
        this.refreshData.setVisibility(View.VISIBLE);
    }

    public void onCatClick(int position) {

        CategoryModel catModel = categoryList.get(position);
        int id = catModel.getId();

        List<ProductModel> filteredList = new ArrayList<>();

        for (ProductModel item : productList) {
            if (item.getCategory_id() == id) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            PosActivity.this.recyclerView.setVisibility(View.INVISIBLE);
            PosActivity.this.recyclerView.setVisibility(View.GONE);
            PosActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
            PosActivity.this.imgNoProduct.setImageResource(R.drawable.not_found);
            PosActivity.this.textView_NoProducts.setVisibility(View.VISIBLE);
            return;
        }
        PosActivity.this.recyclerView.setVisibility(View.VISIBLE);
        PosActivity.this.imgNoProduct.setVisibility(View.GONE);
        PosActivity.this.textView_NoProducts.setVisibility(View.GONE);
        PosActivity.this.recyclerView.setAdapter(new PosProductAdapter(PosActivity.this, filteredList));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() != R.id.menu_cart_button) {
            return super.onOptionsItemSelected(item);
        } else {
            startActivity(new Intent(this, ProductCart.class));
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(this, CashierDashboard.class));

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

    }
}