package com.ahmadabuhasan.skripsi.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.ahmadabuhasan.skripsi.CashierDashboard;
import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.PosProductAdapter;
import com.ahmadabuhasan.skripsi.adapter.ProductCategoryAdapter;
import com.ahmadabuhasan.skripsi.connection.ApiRequestHandler;
import com.ahmadabuhasan.skripsi.connection.models.CategoryModel;
import com.ahmadabuhasan.skripsi.connection.models.ProductModel;
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

    Button refreshData;

    TextView textView_NoProducts;
    TextView textView_Reset;

    private RecyclerView recyclerView;
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
        RecyclerView categoryRecyclerView = findViewById(R.id.category_recyclerview);

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

        this.imgBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseAccess.open();
        int count = databaseAccess.getCartItemCount();
        if (count == 0) {
            textView_Count.setVisibility(View.INVISIBLE);
        } else {
            textView_Count.setVisibility(View.VISIBLE);
            textView_Count.setText(String.valueOf(count));
        }

        this.imgCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PosActivity.this.startActivity(new Intent(PosActivity.this, ProductCart.class));
            }
        });

        this.imgScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PosActivity.this.startActivity(new Intent(PosActivity.this, ScannerActivity.class));
            }
        });

        apiRequestHandler = new ApiRequestHandler(this);
        getDataItems();

        this.refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataItems();
            }
        });

        editText_Search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                databaseAccess.open();
//                List<HashMap<String, String>> searchProductList = databaseAccess.getSearchProducts(s.toString());
//                if (searchProductList.size() <= 0) {
//                    PosActivity.this.recyclerView.setVisibility(View.GONE);
//                    PosActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
//                    PosActivity.this.imgNoProduct.setImageResource(R.drawable.not_found);
//                    PosActivity.this.textView_NoProducts.setVisibility(View.VISIBLE);
//                    return;
//                }
//                PosActivity.this.recyclerView.setVisibility(View.VISIBLE);
//                PosActivity.this.imgNoProduct.setVisibility(View.GONE);
//                PosActivity.this.textView_NoProducts.setVisibility(View.GONE);
//                PosActivity posActivity = PosActivity.this;
//                posActivity.productAdapter = new PosProductAdapter(posActivity, searchProductList);
//                PosActivity.this.recyclerView.setAdapter(PosActivity.this.productAdapter);
            }

            public void afterTextChanged(Editable s) {

            }
        });

        this.textView_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseAccess.open();
//                List<HashMap<String, String>> productList = databaseAccess.getProducts();
//                if (productList.isEmpty()) {
//                    PosActivity.this.recyclerView.setVisibility(View.GONE);
//                    PosActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
//                    PosActivity.this.imgNoProduct.setImageResource(R.drawable.not_found);
//                    PosActivity.this.textView_NoProducts.setVisibility(View.VISIBLE);
//                    return;
//                }
//                PosActivity.this.recyclerView.setVisibility(View.VISIBLE);
//                PosActivity.this.imgNoProduct.setVisibility(View.GONE);
//                PosActivity.this.textView_NoProducts.setVisibility(View.GONE);
//                PosActivity posActivity = PosActivity.this;
//                posActivity.productAdapter = new PosProductAdapter(posActivity, productList);
//                PosActivity.this.recyclerView.setAdapter(PosActivity.this.productAdapter);
            }
        });

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        categoryRecyclerView.setHasFixedSize(true);

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
//
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
        this.categoryList.clear();
        this.productList.clear();
        progressBar.setVisibility(View.VISIBLE);
        apiRequestHandler.getCategories(
                categories -> {
                    // Handle the list of products here
                    if(categories != null) {

                        this.imgNoProduct.setVisibility(View.GONE);
                        this.categoryList = categories;
                        ProductCategoryAdapter categoryAdapter1 = new ProductCategoryAdapter(this, categories, this.recyclerView, this.imgNoProduct, this.textView_NoProducts, this::onCatClick);
                        this.categoryAdapter = categoryAdapter1;
                        this.recyclerView.setAdapter(categoryAdapter1);
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
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    // Handle error
                    Log.e(TAG, "Error Fetching Products: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
        );
        this.refreshData.setVisibility(View.VISIBLE);
    }

    public void onCatClick(int position) {

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
        startActivity(new Intent(this, DashboardActivity.class));
        super.onBackPressed();
    }
}