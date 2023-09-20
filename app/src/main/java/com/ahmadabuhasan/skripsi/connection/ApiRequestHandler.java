package com.ahmadabuhasan.skripsi.connection;

import android.content.Context;

import com.ahmadabuhasan.skripsi.Auth.SharedPrefManager;
import com.ahmadabuhasan.skripsi.Auth.User;
import com.ahmadabuhasan.skripsi.cashier.model.Order;
import com.ahmadabuhasan.skripsi.connection.models.CategoryModel;
import com.ahmadabuhasan.skripsi.connection.models.CustomerModel;
import com.ahmadabuhasan.skripsi.connection.models.ExpenseCategoryModel;
import com.ahmadabuhasan.skripsi.connection.models.ExpenseModel;
import com.ahmadabuhasan.skripsi.connection.models.OrderDetailsModel;
import com.ahmadabuhasan.skripsi.connection.models.OrderModel;
import com.ahmadabuhasan.skripsi.connection.models.ProductModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiRequestHandler {
    private String BASE_URL;
    private Context context;
    private final RequestQueue requestQueue;

    public ApiRequestHandler(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);

        User user =  SharedPrefManager.getInstance(context).getUser();
        String tenantUrl = user.getTenantUrl();

        this.BASE_URL = "https://"+tenantUrl+".megapos.co.ke/api";
    }

    private Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        User user =  SharedPrefManager.getInstance(context.getApplicationContext()).getUser();
        String token = user.getToken();

        headers.put("Authorization", "Bearer " + token);
        return headers;
    }

    public void getOrderDetails(){

    }


    public void submitOrder(Response.Listener<JSONObject> order, Response.ErrorListener errorListener){

        String url = BASE_URL + "/orders/create";
        JSONObject orderJson = new JSONObject();

        // Create the request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, orderJson,
                response -> {
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

    // GET Request for products
    public void getProducts(Response.Listener<List<ProductModel>> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/products";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<ProductModel> products = parseProductResponse(response);
                    successListener.onResponse(products);
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // GET Request for categories
    public void getCategories(Response.Listener<List<CategoryModel>> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/categories";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<CategoryModel> categories = parseCategoryResponse(response);
                    successListener.onResponse(categories);
                },
                errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // GET Request for orders
    public void getOrders(Response.Listener<List<OrderModel>> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/orders";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<OrderModel> orders = parseOrderResponse(response);
                    successListener.onResponse(orders);
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // GET Order details of the Order
    public void getOrderDetails(Response.Listener<List<OrderDetailsModel>> successListener, Response.ErrorListener errorListener, int id){
        String url = BASE_URL + "/orders/details/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<OrderDetailsModel> orders = parseOrderDetailsResponse(response);
                    successListener.onResponse(orders);
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // POST the order data to the application
    public void getEpenses(Response.Listener<List<ExpenseModel>> successListener, Response.ErrorListener errorListener){
        String url = BASE_URL + "/expenses";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<ExpenseModel> customers = parseExpensesResponse(response);
                    successListener.onResponse(customers);
                },
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    //GET Expeses categor
    public void getEpensesCategory(Response.Listener<List<ExpenseCategoryModel>> successListener, Response.ErrorListener errorListener){
        String url = BASE_URL + "/expenses/category";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<ExpenseCategoryModel> customers = parseExpensesCategoryResponse(response);
                    successListener.onResponse(customers);
                },
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // GET Request for customers
    public void getCustomers(Response.Listener<List<CustomerModel>> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/customers";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<CustomerModel> customers = parseCustomerResponse(response);
                    successListener.onResponse(customers);
                },
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // POST Request for customers
    public void createCustomer(Map<String, String> parameters, Response.Listener<CustomerModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/customers";

        JSONObject jsonParams = new JSONObject(parameters);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                response -> {
                    boolean createdCustomer = parseCustomerCreateResponse(response);
                    //successListener.onResponse(createdCustomer);
                },
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // POST Request for orders
    public void createOrder(Map<String, String> parameters, Response.Listener<OrderModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/orders";

        JSONObject jsonParams = new JSONObject(parameters);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                response -> {
                    //OrderModel createdOrder = (OrderModel) parseOrderResponse(response);
                    //successListener.onResponse(createdOrder);
                },
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // PATCH Request for customers
    public void updateCustomer(int customerId, Map<String, String> parameters, Response.Listener<CustomerModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/customers/" + customerId;

        JSONObject jsonParams = new JSONObject(parameters);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, jsonParams,
                response -> {
                    boolean updatedCustomer = parseCustomerCreateResponse(response);
                    //successListener.onResponse(updatedCustomer);
                },
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // PATCH Request for expenses
    public void updateExpense(int expenseId, Map<String, String> parameters, Response.Listener<ExpenseModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/expenses/" + expenseId;

        JSONObject jsonParams = new JSONObject(parameters);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, jsonParams,
                response -> {
                    boolean updatedExpense = parseExpenseResponse(response);
                    //successListener.onResponse(updatedExpense);
                },
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getAuthHeaders();
            }
        };

        int initialTimeoutMs = 3000; // Initial timeout in milliseconds
        int maxNumRetries = 3; // Maximum number of retries
        float backoffMultiplier = 2.0f; // Exponential backoff multiplier

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                initialTimeoutMs,
                maxNumRetries,
                backoffMultiplier
        ));

        requestQueue.add(request);
    }

    // Add more methods for other API endpoints as needed.

    // Helper methods to parse JSON responses and convert Java objects to JSON.
    // Implement these methods based on the structure of your JSON responses.
    private List<ProductModel> parseProductResponse(JSONObject jsonObject) {
        List<ProductModel> products = new ArrayList<>();
        try {
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                JSONArray data = jsonObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject productObj = data.getJSONObject(i);
                    int id = productObj.getInt("id");
                    int category_id = productObj.getInt("category_id");
                    String product_name = productObj.getString("product_name");
                    String product_code = productObj.getString("product_code");
                    String product_barcode_symbology = productObj.getString("product_barcode_symbology");
                    int product_quantity = productObj.getInt("product_quantity");
                    double product_cost = productObj.getDouble("product_cost");
                    double product_price = productObj.getDouble("product_price");
                    String product_unit = productObj.getString("product_unit");
                    int product_stock_alert = productObj.getInt("product_stock_alert");
                    String product_order_tax = productObj.getString("product_order_tax");
                    String product_tax_type = productObj.getString("product_tax_type");
                    String product_note = productObj.getString("product_note");
                    String created_at = productObj.getString("created_at");
                    String updated_at = productObj.getString("updated_at");
                    String media = productObj.getString("imageUrl");

                    // Create a new ProductModel object and add it to the list
                    ProductModel product = new ProductModel(id, category_id, product_name, product_code, product_barcode_symbology,
                            product_quantity, product_cost, product_price, product_unit, product_stock_alert, product_order_tax,
                            product_tax_type, product_note, created_at, updated_at, media);
                    products.add(product);
                }
            }  else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return products;
    }

    private List<CategoryModel> parseCategoryResponse(JSONObject jsonArray) {
        List<CategoryModel> categories = new ArrayList<>();
        try {
            boolean success = jsonArray.getBoolean("success");
            if (success) {
                JSONArray data = jsonArray.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String category_code = jsonObject.getString("category_code");
                    String category_name = jsonObject.getString("category_name");
                    String created_at = jsonObject.getString("created_at");
                    String updated_at = jsonObject.getString("updated_at");
                    // Parse other properties as needed...

                    // Create a new CategoryModel object and add it to the list
                    CategoryModel category = new CategoryModel(id, category_code, category_name, created_at, updated_at);
                    categories.add(category);
                }
            }  else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }

    private List<OrderModel> parseOrderResponse(JSONObject jsonObject) {
        List<OrderModel> orders = new ArrayList<>();
        try {
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    //JSONObject orderObj = data.getJSONObject(i);
                    JSONObject orderObj = data.getJSONObject(i);
                int id = orderObj.getInt("id");
                String date = orderObj.getString("date");
                String reference = orderObj.getString("reference");
                int customer_id = orderObj.getInt("customer_id");
                String customer_name = orderObj.getString("customer_name");
                double tax_percentage = orderObj.getDouble("tax_percentage");
                double tax_amount = orderObj.getDouble("tax_amount");
                double discount_percentage = orderObj.getDouble("discount_percentage");
                double discount_amount = orderObj.getDouble("discount_amount");
                double shipping_amount = orderObj.getDouble("shipping_amount");
                double total_amount = orderObj.getDouble("total_amount");
                double paid_amount = orderObj.getDouble("paid_amount");
                double due_amount = orderObj.getDouble("due_amount");
                String status = orderObj.getString("status");
                String payment_status = orderObj.getString("payment_status");
                String payment_method = orderObj.getString("payment_method");
                String note = orderObj.getString("note");
                String created_at = orderObj.getString("created_at");
                String updated_at = orderObj.getString("updated_at");
                    // Parse other properties as needed...
                    // Create a new OrderModel object and add it to the list
                    OrderModel order = new OrderModel( id,  date,  reference,  customer_id,  customer_name,  tax_percentage,  tax_amount,  discount_percentage,  discount_amount,  shipping_amount,  total_amount,  paid_amount,  due_amount,  status,  payment_status,  payment_method,  note,  created_at,  updated_at);
                    //OrderModel order = parseSingleOrder(orderObj);
                    orders.add(order);
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private OrderModel parseSingleOrder(JSONObject jsonObject) {
        OrderModel order = null;
        try {
            int id = jsonObject.getInt("id");
            String date = jsonObject.getString("date");
            String reference = jsonObject.getString("reference");
            int customer_id = jsonObject.getInt("customer_id");
            String customer_name = jsonObject.getString("customer_name");
            double tax_percentage = jsonObject.getDouble("tax_percentage");
            double tax_amount = jsonObject.getDouble("tax_amount");
            double discount_percentage = jsonObject.getDouble("discount_percentage");
            double discount_amount = jsonObject.getDouble("discount_amount");
            double shipping_amount = jsonObject.getDouble("shipping_amount");
            double total_amount = jsonObject.getDouble("total_amount");
            double paid_amount = jsonObject.getDouble("paid_amount");
            double due_amount = jsonObject.getDouble("due_amount");
            String status = jsonObject.getString("status");
            String payment_status = jsonObject.getString("payment_status");
            String payment_method = jsonObject.getString("payment_method");
            String note = jsonObject.getString("note");
            String created_at = jsonObject.getString("created_at");
            String updated_at = jsonObject.getString("updated_at");

            // Create a new OrderModel object
            order = new OrderModel( id,  date,  reference,  customer_id,  customer_name,  tax_percentage,  tax_amount,  discount_percentage,  discount_amount,  shipping_amount,  total_amount,  paid_amount,  due_amount,  status,  payment_status,  payment_method,  note,  created_at,  updated_at);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return order;
    }

    private List<CustomerModel> parseCustomerResponse(JSONObject jsonArray) {
        List<CustomerModel> customers = new ArrayList<>();
        try {
            boolean success = jsonArray.getBoolean("success");
            if (success) {
                JSONArray data = jsonArray.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String customer_name = jsonObject.getString("customer_name");
                    String customer_email = jsonObject.getString("customer_email");
                    String customer_phone = jsonObject.getString("customer_phone");
                    String customer_krapin = jsonObject.getString("customer_krapin");
                    String city = jsonObject.getString("city");
                    String country = jsonObject.getString("country");
                    String address = jsonObject.getString("address");
                    String created_at = jsonObject.getString("created_at");
                    String updated_at = jsonObject.getString("updated_at");
                    // Parse other properties as needed...

                    // Create a new CustomerModel object and add it to the list
                    CustomerModel customer = new CustomerModel(id, customer_name, customer_email, customer_phone, customer_krapin, city, country, address, created_at, updated_at);
                    customers.add(customer);
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customers;
    }

    private List<ExpenseModel> parseExpensesResponse(JSONObject jsonArray) {
        List<ExpenseModel> customers = new ArrayList<>();
        try {
            boolean success = jsonArray.getBoolean("success");
            if (success) {
                JSONArray data = jsonArray.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    int category_id = jsonObject.getInt("category_id");
                    String date = jsonObject.getString("date");
                    String refrensece = jsonObject.getString("reference");
                    String details = jsonObject.getString("details");
                    double amount = jsonObject.getDouble("amount");
                    String created_at = jsonObject.getString("created_at");
                    String updated_at = jsonObject.getString("updated_at");
                    String categoryName = jsonObject.getString("category_name");
                    // Parse other properties as needed...

                    // Create a new CustomerModel object and add it to the list
                    ExpenseModel customer = new ExpenseModel(id, category_id, categoryName, date, refrensece, details, amount, created_at, updated_at);
                    customers.add(customer);
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customers;
    }

    private List<ExpenseCategoryModel> parseExpensesCategoryResponse(JSONObject jsonArray) {
        List<ExpenseCategoryModel> customers = new ArrayList<>();
        try {
            boolean success = jsonArray.getBoolean("success");
            if (success) {
                JSONArray data = jsonArray.getJSONArray("data");
                customers.add(new ExpenseCategoryModel(1, "Choose a Category", "CatDesc"));
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String categoryName = jsonObject.getString("category_name");
                    String categoryDesc = jsonObject.getString("category_description");
                    // Parse other properties as needed...

                    // Create a new CustomerModel object and add it to the list
                    ExpenseCategoryModel customer = new ExpenseCategoryModel(id, categoryName, categoryDesc);
                    customers.add(customer);
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customers;
    }

    private boolean parseCustomerCreateResponse(JSONObject jsonObject) {
        try {
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                String dataMessage = jsonObject.getString("message");
                //TODO show toast to say success
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean parseExpenseResponse(JSONObject jsonObject) {
        try {
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                String dataMessage = jsonObject.getString("message");
                //TODO show toast to say success
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<OrderDetailsModel> parseOrderDetailsResponse(JSONObject jsonObject){
        List<OrderDetailsModel> orders = new ArrayList<>();
        try {
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    //JSONObject orderObj = data.getJSONObject(i);
                    JSONObject orderObj = data.getJSONObject(i);
                    int id = orderObj.getInt("id");
                    String name = orderObj.getString("product_name");
                    int quantity = orderObj.getInt("quantity");
                    double price = orderObj.getDouble("unit_price");
                    double cost = orderObj.getDouble("price");
                    String image = orderObj.getString("imageUrl");

                    OrderDetailsModel order = new OrderDetailsModel(id, name, quantity, price, cost, image);
                    //OrderModel order = parseSingleOrder(orderObj);
                    orders.add(order);
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orders;
    }
}

