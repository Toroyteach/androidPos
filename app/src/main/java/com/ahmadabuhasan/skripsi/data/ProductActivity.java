package com.ahmadabuhasan.skripsi.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
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
import com.ahmadabuhasan.skripsi.BackgroundRefreshDataTaskHandler;
import com.ahmadabuhasan.skripsi.CashierDashboard;
import com.ahmadabuhasan.skripsi.DashboardActivity;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.WarehouseDashboard;
import com.ahmadabuhasan.skripsi.adapter.ProductAdapter;
import com.ahmadabuhasan.skripsi.connection.ApiRequestHandler;
import com.ahmadabuhasan.skripsi.connection.models.CategoryModel;
import com.ahmadabuhasan.skripsi.connection.models.CustomerModel;
import com.ahmadabuhasan.skripsi.connection.models.OrderModel;
import com.ahmadabuhasan.skripsi.connection.models.ProductModel;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

import static com.ahmadabuhasan.skripsi.Auth.LoginActivity.item;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class ProductActivity extends AppCompatActivity {

    private BackgroundRefreshDataTaskHandler backgroundRefreshDataTaskHandler;
    private List<ProductModel> productList = new ArrayList<>();
    private List<ProductModel> filteredProductSearchList = new ArrayList<>();
    private ProductSearchFunction productSearchFunction;

    private static final String TAG = "";
    EditText editText_Search;
    FloatingActionButton floatingActionButton_fabAdd;
    ImageView imgNoProduct;
    ProgressDialog loading;
    ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    ProgressBar progressBar;

    private ApiRequestHandler apiRequestHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_product);

        this.floatingActionButton_fabAdd = findViewById(R.id.fab_add);
        this.editText_Search = findViewById(R.id.et_search);
        this.recyclerView = findViewById(R.id.product_recyclerview);
        this.imgNoProduct = findViewById(R.id.image_no_product);

        this.progressBar = findViewById(R.id.productsListProgressBar);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        this.floatingActionButton_fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProductActivity.this.startActivity(new Intent(ProductActivity.this, AddProductActivity.class));
                fetchProducts();
            }
        });

        apiRequestHandler = new ApiRequestHandler(this);
        fetchProducts();

        this.editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ProductSearchFunction.search(productList, s.toString(), new ProductSearchFunction.SearchListener() {
                    @Override
                    public void onSearch(List<ProductModel> resultList) {
                        if (resultList.size() <= 0) {
                            ProductActivity.this.recyclerView.setVisibility(View.GONE);
                            ProductActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
                            ProductActivity.this.imgNoProduct.setImageResource(R.drawable.no_data);
                            return;
                        }
                        ProductActivity.this.recyclerView.setVisibility(View.VISIBLE);
                        ProductActivity.this.imgNoProduct.setVisibility(View.GONE);
                        ProductActivity productActivity = ProductActivity.this;
                        productActivity.productAdapter = new ProductAdapter(productActivity, resultList);
                        ProductActivity.this.recyclerView.setAdapter(ProductActivity.this.productAdapter);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void fetchProducts() {
        progressBar.setVisibility(View.VISIBLE);
        this.productList.clear();
        apiRequestHandler.getProducts(
                products -> {
                    // Handle the list of products here
                    if(products != null) {

                        this.imgNoProduct.setVisibility(View.GONE);
                        this.productList = products;
                        this.filteredProductSearchList.addAll(products);
                        ProductAdapter productAdapter1 = new ProductAdapter(this, products);
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
                    Log.e(TAG, "Error fetching Products: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
        );
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_product_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId != R.id.menu_export) {
            return super.onOptionsItemSelected(item);
        } else {
            folderChooser();
            return true;
        }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startBackgroundTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopBackgroundTask();
    }

    public void folderChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(true, false, new String[0]).withChosenListener(new ChooserDialog.Result() {
            @Override
            public void onChoosePath(String path, File pathFile) {
                ProductActivity.this.onExport(path);
                Log.d("path", path);
            }
        }).build().show();
    }

    public void onExport(String path) {
        String directory_path = path;
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        new SQLiteToExcel(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, directory_path).exportSingleTable("products", "products.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
                ProductActivity.this.loading = new ProgressDialog(ProductActivity.this);
                ProductActivity.this.loading.setMessage(ProductActivity.this.getString(R.string.data_exporting_please_wait));
                ProductActivity.this.loading.setCancelable(false);
                ProductActivity.this.loading.show();
            }

            @Override
            public void onCompleted(String filePath) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ProductActivity.this.loading.dismiss();
                        Toasty.success(ProductActivity.this, (int) R.string.data_successfully_exported, Toasty.LENGTH_SHORT).show();
                    }
                }, 5000);
            }

            @Override
            public void onError(Exception e) {
                ProductActivity.this.loading.dismiss();
                Toasty.error(ProductActivity.this, (int) R.string.data_export_fail, Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void startBackgroundTask() {
        backgroundRefreshDataTaskHandler.startTask(apiRequestRunnable);
    }

    private void stopBackgroundTask() {
        backgroundRefreshDataTaskHandler.stopTask();
    }

    private Runnable apiRequestRunnable = new Runnable() {
        @Override
        public void run() {
            // Call your method for making API requests here
            makeApiRequest();

            // Reschedule the task to run again after the delay
            startBackgroundTask();
        }
    };

    private void makeApiRequest() {
        // Implement your logic for making API requests here
        // This method will be called every 5 minutes if the activity is on the screen and there has been no activity
    }
}