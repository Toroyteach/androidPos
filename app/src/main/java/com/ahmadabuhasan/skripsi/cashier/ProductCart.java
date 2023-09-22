package com.ahmadabuhasan.skripsi.cashier;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.Auth.SharedPrefManager;
import com.ahmadabuhasan.skripsi.Auth.User;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.adapter.CartAdapter;
import com.ahmadabuhasan.skripsi.adapter.CustomerAdapter;
import com.ahmadabuhasan.skripsi.connection.ApiRequestHandler;
import com.ahmadabuhasan.skripsi.connection.models.CustomerModel;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.print.OrdersActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class ProductCart extends AppCompatActivity {

    private static final String TAG = "";
    CartAdapter productCartAdapter;
    LinearLayout linearLayout;

    //ArrayAdapter<String> customerAdapter;
    List<CustomerModel> customerNames;
    ArrayAdapter<String> orderTypeAdapter;
    List<String> orderTypeNames;
    ArrayAdapter<String> paymentMethodAdapter;
    List<String> paymentMethodNames;

    ImageView imgNoProduct;
    TextView textView_no_product;
    TextView textView_total_price;
    Button button_SubmitOrder;

    private CustomerAdapter customerAdapter;
    List<CustomerModel> customerModelList;
    ProgressBar progressBar;
    private ApiRequestHandler apiRequestHandler;
    int customerId;
    String discountType = "Fixed";
    int discount_percentage = 0;
    int totalAmount = 0;
    double discountAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_cart);

        this.progressBar = findViewById(R.id.submitProductProgressBar);

        RecyclerView recyclerView = findViewById(R.id.cart_recyclerview);
        this.imgNoProduct = findViewById(R.id.image_no_product);
        this.textView_no_product = findViewById(R.id.tv_no_product);
        this.textView_total_price = findViewById(R.id.tv_total_price);
        this.button_SubmitOrder = findViewById(R.id.btn_submit_order);
        this.linearLayout = findViewById(R.id.linear_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        this.textView_no_product.setVisibility(View.GONE);

        getCustomers();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<HashMap<String, String>> cartProductList = databaseAccess.getCartProduct();
        if (cartProductList.isEmpty()) {
            this.imgNoProduct.setImageResource(R.drawable.empty_cart);
            this.imgNoProduct.setVisibility(View.VISIBLE);
            this.textView_no_product.setVisibility(View.VISIBLE);
            this.button_SubmitOrder.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            this.linearLayout.setVisibility(View.GONE);
            this.textView_total_price.setVisibility(View.GONE);
        } else {
            this.imgNoProduct.setVisibility(View.GONE);
            CartAdapter cartAdapter = new CartAdapter(this, cartProductList, this.textView_total_price, this.button_SubmitOrder, this.imgNoProduct, this.textView_no_product);
            this.productCartAdapter = cartAdapter;
            recyclerView.setAdapter(cartAdapter);
        }

        this.button_SubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCart.this.dialog();
            }
        });
    }

    public void getCustomers(){

        apiRequestHandler = new ApiRequestHandler(this);
        apiRequestHandler.getCustomers(
                customers -> {
                    // Handle the list of products here
                    if(customers != null) {

                        //this.customerNames = customers;
                        customerModelList = new ArrayList<>();

                        // Add sample customer data (replace this with your actual customer data)
                        customerModelList.addAll(customers);

                        // Create and set the custom adapter
                        customerAdapter = new CustomerAdapter(this, customerModelList);

                    } else {

                        Toasty.info(this, (int) R.string.no_customer_found, Toasty.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    Log.e(TAG, "Error fetching Customers: " + error.getMessage());
                }
        );
    }

    @SuppressLint("SetTextI18n")
    public void dialog() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> shopData = databaseAccess.getShopInformation();
        final String shop_currency = shopData.get(0).get(DatabaseOpenHelper.SHOP_CURRENCY);

        String tax = shopData.get(0).get(DatabaseOpenHelper.SHOP_TAX);
        double getTax = Double.parseDouble(tax);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payment, (ViewGroup) null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        ImageButton dialog_btn_close = dialogView.findViewById(R.id.btn_close);
        final Button dialog_btn_submit = dialogView.findViewById(R.id.btn_submit);

        final TextView dialog_customer = dialogView.findViewById(R.id.dialog_customer);
        ImageButton dialog_img_customer = dialogView.findViewById(R.id.img_select_customer);
        final TextView dialog_order_type = dialogView.findViewById(R.id.dialog_order_type);
        ImageButton dialog_img_order_type = dialogView.findViewById(R.id.img_order_type);
        final TextView dialog_order_payment_method = dialogView.findViewById(R.id.dialog_order_status);
        ImageButton dialog_img_order_payment_method = dialogView.findViewById(R.id.img_order_payment_method);

        TextView dialog_text_sub_total = dialogView.findViewById(R.id.dialog_text_sub_total);
        TextView dialog_text_total_tax = dialogView.findViewById(R.id.dialog_text_total_tax);
        final EditText dialog_et_discount = dialogView.findViewById(R.id.et_dialog_discount);
        final TextView dialog_text_total_cost = dialogView.findViewById(R.id.dialog_text_total_cost);

        ((TextView) dialogView.findViewById(R.id.dialog_level_tax)).setText(getString(R.string.total_tax) + " (" + tax + "%) : ");
        final double total_cost = CartAdapter.total_price;
        String sb = shop_currency +
                " " +
                NumberFormat.getInstance(Locale.getDefault()).format(total_cost);
        dialog_text_sub_total.setText(sb);

        final double calculated_tax = (total_cost * getTax) / 100.0d;
        dialog_text_total_tax.setText(shop_currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(calculated_tax));
        double calculated_total_cost = (total_cost + calculated_tax) - Utils.DOUBLE_EPSILON;
        dialog_text_total_cost.setText(shop_currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(calculated_total_cost));

        dialog_et_discount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        //dialog_et_discount.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        dialog_et_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String get_discount = s.toString();

                if(!get_discount.isEmpty()){
                    if(discountType == "Fixed"){
                        if (!get_discount.isEmpty()) {
                            double discount = Double.parseDouble(get_discount);
                            if (discount > total_cost + calculated_tax) {
                                dialog_et_discount.setError(ProductCart.this.getString(R.string.discount_cant_be_greater_than_total_price));
                                dialog_et_discount.requestFocus();
                                dialog_btn_submit.setVisibility(View.INVISIBLE);
                                return;
                            }
                            dialog_btn_submit.setVisibility(View.VISIBLE);
                            dialog_text_total_cost.setText(shop_currency + " " + NumberFormat.getInstance(Locale.getDefault()).format((total_cost + calculated_tax) - discount));
                            discount_percentage = 0;
                            totalAmount = (int) ((total_cost + calculated_tax) - discount);
                            return;
                        }
                        double calculated_total_cost = (total_cost + calculated_tax) - Utils.DOUBLE_EPSILON;
                        dialog_text_total_cost.setText(shop_currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(calculated_total_cost));
                    } else {

                        int discount = Integer.parseInt(get_discount);

                            if (discount <= 0) {
                                dialog_et_discount.setError(ProductCart.this.getString(R.string.discount_percentage_cant_be_greater_than_total_price));
                                dialog_et_discount.requestFocus();
                                dialog_btn_submit.setVisibility(View.INVISIBLE);
                                return;
                            }
                            double disPer = Double.parseDouble(get_discount);
                            double calculated_total_cost = total_cost * (disPer / 100.0);
                            double finalAmount = total_cost - calculated_total_cost;
                            discount_percentage = discount;
                            totalAmount = (int) finalAmount;
                            dialog_text_total_cost.setText(shop_currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(finalAmount));
                    }
                } else {
                    dialog_text_total_cost.setText(shop_currency + " " + NumberFormat.getInstance(Locale.getDefault()).format((total_cost)));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        this.customerNames = new ArrayList();
//        databaseAccess.open();
//        List<HashMap<String, String>> customer = databaseAccess.getCustomers();
//        for (int i = 0; i < customer.size(); i++) {
//            this.customerNames.add(customer.get(i).get(DatabaseOpenHelper.CUSTOMER_NAME));
//        }

        //int customerId = 0;
        dialog_img_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //ProductCart.this.customerAdapter = new ArrayAdapter<>(ProductCart.this, android.R.layout.simple_list_item_1);
//
//                List<String> customerName = new ArrayList<>();
//
//                for (CustomerModel customerModel : customerNames) {
//                    customerName.add(customerModel.getCustomer_name());
//                }
//
//                //ProductCart.this.customerAdapter.set

                AlertDialog.Builder dialog = new AlertDialog.Builder(ProductCart.this);
                View dialogView = ProductCart.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.select_customer);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(ProductCart.this.customerAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ProductCart.this.customerAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                final AlertDialog alertDialog = dialog.create();
                ((Button) dialogView.findViewById(R.id.dialog_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialog.dismiss();
                        dialog_customer.setText(ProductCart.this.customerAdapter.getItem(position).getCustomer_name());
                        customerId = ProductCart.this.customerAdapter.getItem(position).getId();
                    }
                });
            }
        });

        this.orderTypeNames = new ArrayList();
        databaseAccess.open();
        List<HashMap<String, String>> order_type = databaseAccess.getOrderType();
        for (int i1 = 0; i1 < order_type.size(); i1++) {
            this.orderTypeNames.add(order_type.get(i1).get(DatabaseOpenHelper.ORDER_TYPE_NAME));
        }

        dialog_img_order_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCart.this.orderTypeAdapter = new ArrayAdapter<>(ProductCart.this, android.R.layout.simple_list_item_1);
                ProductCart.this.orderTypeAdapter.addAll(ProductCart.this.orderTypeNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProductCart.this);
                View dialogView = ProductCart.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.select_order_type);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(ProductCart.this.orderTypeAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ProductCart.this.orderTypeAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                final AlertDialog alertDialog = dialog.create();
                ((Button) dialogView.findViewById(R.id.dialog_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialog.dismiss();
                        dialog_order_type.setText(ProductCart.this.orderTypeAdapter.getItem(position));
                    }
                });
            }
        });

//        this.paymentMethodNames = new ArrayList();
//        databaseAccess.open();
//        List<HashMap<String, String>> payment_method = databaseAccess.getPaymentMethod();
//        for (int i2 = 0; i2 < payment_method.size(); i2++) {
//            this.paymentMethodNames.add(payment_method.get(i2).get(DatabaseOpenHelper.PAYMENT_METHOD_NAME));
//        }

        dialog_img_order_payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCart.this.paymentMethodAdapter = new ArrayAdapter<>(ProductCart.this, android.R.layout.simple_list_item_1);
                ProductCart.this.paymentMethodAdapter.add("Fixed");
                ProductCart.this.paymentMethodAdapter.add("Percentage");
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProductCart.this);
                View dialogView = ProductCart.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.select_payment_method);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(ProductCart.this.paymentMethodAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        ProductCart.this.paymentMethodAdapter.getFilter().filter(charSequence);
                    }

                    public void afterTextChanged(Editable s) {

                    }
                });
                final AlertDialog alertDialog = dialog.create();
                ((Button) dialogView.findViewById(R.id.dialog_button)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        alertDialog.dismiss();
                        dialog_order_payment_method.setText(ProductCart.this.paymentMethodAdapter.getItem(position));
                        discountType = ProductCart.this.paymentMethodAdapter.getItem(position);
                    }
                });
            }
        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        dialog_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        dialog_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discount;
                String order_type = dialog_order_type.getText().toString().trim();
                String order_payment_method = "Cash";
                String customer_name = dialog_customer.getText().toString().trim();
                String discount1 = dialog_et_discount.getText().toString().trim();
                String disctype = "Fixed";

                if(discountType != "Fixed"){
                    disctype = discountType;
                }

                int customer_id = customerId;

                if(customer_id == 0){
                    Toast.makeText(getApplicationContext(), "Please Select Customer", Toast.LENGTH_SHORT).show();
                    return;
                }

                double shipping_amount = 0.0;
                String discountType = dialog_order_payment_method.getText().toString().trim();
                int tax_percentage = Integer.parseInt(tax); //important//important

                if (discount1.isEmpty()) {
                    discount = "0.00";
                } else {
                    discount = discount1;
                }

                double total_amount = calculated_total_cost;

                ProductCart.this.proceedOrder(order_type, order_payment_method, customer_id, customer_name, calculated_tax, shipping_amount, totalAmount, discount, disctype, discount_percentage, tax_percentage, discount_percentage);
                alertDialog.dismiss();
            }
        });
    }

    public void proceedOrder(String type, String payment_method, int customer_id, String customer_name,
                             double calculated_tax, double shipping_amount, double total_amount,
                             String discount, String discountType, int discountPercentage,
                             int tax_percentage, int discount_percenage) {
        JSONException e;
        String productId = DatabaseOpenHelper.PRODUCT_ID;
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        if (databaseAccess.getCartItemCount() > 0) {
            databaseAccess.open();
            List<HashMap<String, String>> lines = databaseAccess.getCartProduct();
            if (lines.isEmpty()) {
                Toasty.error(this, (int) R.string.no_product_found, Toasty.LENGTH_SHORT).show();
                return;
            }
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date());
            String timeStamp = new SimpleDateFormat("yyMMdd-HHmmss", Locale.getDefault()).format(new Date()); // NoInvoice
            Log.d("Time", timeStamp);
            JSONObject obj = new JSONObject();
            try {
                obj.put(DatabaseOpenHelper.ORDER_LIST_DATE, currentDate);
                obj.put(DatabaseOpenHelper.ORDER_LIST_TIME, currentTime);
                obj.put(DatabaseOpenHelper.ORDER_LIST_TYPE, type);
                try {
                    obj.put("payment_method", payment_method);
                    obj.put(DatabaseOpenHelper.ORDER_LIST_CUSTOMER_NAME, customer_name);
                    obj.put("customer_id", customer_id);
                    try {
                        obj.put("tax_amount", calculated_tax);
                        obj.put("discount_amount", discount);
                        obj.put("discount_percentage", discountPercentage);
                        obj.put("tax_percentage", tax_percentage);
                        obj.put("total_amount", total_amount);
                        obj.put("paid_amount", 0);
                        obj.put("shipping_amount", shipping_amount);
                        obj.put("discount_type", discountType);
                        JSONArray array = new JSONArray();
                        int i = 0;
                        while (i < lines.size()) {
                            databaseAccess.open();
                            String product_id = lines.get(i).get(productId);
                            databaseAccess.open();
                            String product_name = lines.get(i).get(DatabaseOpenHelper.CART_PRODUCT_NAME);
                            databaseAccess.open();
                            String weight_unit = databaseAccess.getWeightUnitName(lines.get(i).get(DatabaseOpenHelper.CART_PRODUCT_WEIGHT_UNIT));
                            JSONObject objp = new JSONObject();
                            try {
                                objp.put("product_id", product_id);
                                objp.put("product_name", product_name);
                                objp.put("quantity", lines.get(i).get(DatabaseOpenHelper.CART_PRODUCT_QTY));
                                objp.put("price", lines.get(i).get(DatabaseOpenHelper.CART_PRODUCT_PRICE));
                                objp.put("unit_price", lines.get(i).get(DatabaseOpenHelper.CART_PRODUCT_UNIT_PRICE));
                                objp.put("sub_total", "0.0");
                                array.put(objp);
                                i++;
                            } catch (JSONException e2) {
                                e = e2;
                                e.printStackTrace();
                                saveOrderInOfflineDb(obj);
                                return;
                            }
                        }
                        obj.put("items", array);
                    } catch (JSONException e3) {
                        e = e3;
                        e.printStackTrace();
                        saveOrderInOfflineDb(obj);
                        return;
                    }
                } catch (JSONException e4) {
                    e = e4;
                    e.printStackTrace();
                    saveOrderInOfflineDb(obj);
                    return;
                }
            } catch (JSONException e5) {
                e = e5;
                e.printStackTrace();
                saveOrderInOfflineDb(obj);
                return;
            }
            saveOrderInOfflineDb(obj);
            return;
        }
        Toasty.error(this, (int) R.string.no_product_in_cart, Toasty.LENGTH_SHORT).show();
    }

    private void saveOrderInOfflineDb(JSONObject obj) {


//        String timeStamp = new SimpleDateFormat("yyMMdd-HHmmss", Locale.getDefault()).format(new Date());
//        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
//        databaseAccess.open();
//        databaseAccess.insertOrder(timeStamp, obj);
//        Toasty.success(this, (int) R.string.order_done_successful, Toasty.LENGTH_SHORT).show();
//        startActivity(new Intent(this, OrdersActivity.class));
//        finish();
        System.out.println(obj);
        sendOrderData(obj);
    }

    private void sendOrderData(JSONObject orderJson) {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token = user.getToken();
        String tenantUrl = user.getTenantUrl();

        String BASE_URL = "https://"+tenantUrl+".megapos.co.ke/api/orders/create";

        // Create the request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL, orderJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            // Read the response JSON
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            // Show a toast based on the response
                            if (success) {
                                // Order created successfully
                                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                                databaseAccess.open();
                                databaseAccess.clearProductCart();

                                Toast.makeText(com.ahmadabuhasan.skripsi.cashier.ProductCart.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                                finish();

                            } else {
                                // Some error occurred (optional)
                                Toast.makeText(com.ahmadabuhasan.skripsi.cashier.ProductCart.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response if needed
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(com.ahmadabuhasan.skripsi.cashier.ProductCart.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent(this, PosActivity.class));
        finish();
        return true;
    }

    public class CustomerAdapter extends ArrayAdapter<CustomerModel> {
        private List<CustomerModel> customerModels;

        public CustomerAdapter(Context context, List<CustomerModel> customerModels) {
            super(context, 0, customerModels);
            this.customerModels = customerModels;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            // Get the current customer model
            CustomerModel customerModel = customerModels.get(position);

            // Set the customer name as the text for the list item
            TextView textView = itemView.findViewById(android.R.id.text1);
            textView.setText(customerModel.getCustomer_name());

            return itemView;
        }
    }

}