package com.ahmadabuhasan.skripsi.expense;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ahmadabuhasan.skripsi.Auth.SharedPrefManager;
import com.ahmadabuhasan.skripsi.Auth.User;
import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.connection.ApiRequestHandler;
import com.ahmadabuhasan.skripsi.connection.models.ExpenseCategoryModel;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.print.OrdersActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 07/10/2021
 */

public class AddExpenseActivity extends AppCompatActivity {

    private static final String TAG = "";
    EditText editText_ExpenseName;
    EditText editText_ExpenseNote;
    EditText editText_ExpenseAmount;
    EditText editText_Date;
    EditText editText_Time;
    TextView textView_Add;
    Spinner editText_categorySpinner;
    ProgressBar progressBar;

    String date_time = "";
    int mMinute;
    int mHour;
    int mDay;
    int mMonth;
    int mYear;

    private CreateExpenseCategoryListAdapter createExpenseCategoryListAdapter;
    private ApiRequestHandler apiRequestHandler;
    private List<ExpenseCategoryModel> expensesCategoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_expense);

        this.progressBar = findViewById(R.id.createExpenseProgressBar);
        this.editText_ExpenseName = findViewById(R.id.et_expense_name);
        this.editText_ExpenseNote = findViewById(R.id.et_expense_note);
        this.editText_ExpenseAmount = findViewById(R.id.et_expense_amount);
        this.editText_Date = findViewById(R.id.et_expense_date);
//        this.editText_Time = findViewById(R.id.et_expense_time);
        this.textView_Add = findViewById(R.id.tv_add_expense);
        this.editText_categorySpinner = findViewById(R.id.create_expense_category_spinner);

        apiRequestHandler = new ApiRequestHandler(this);
        getExpenseCategoryList();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date());
        this.editText_Date.setText(currentDate);
//        this.editText_Time.setText(currentTime);

        this.editText_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseActivity.this.datePicker();
            }
        });

        this.textView_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateFields()){
                    return;
                }

                showConfirmationDialog();
//                String expense_name = AddExpenseActivity.this.editText_ExpenseName.getText().toString();
//                String expense_note = AddExpenseActivity.this.editText_ExpenseNote.getText().toString();
//                String expense_amount = AddExpenseActivity.this.editText_ExpenseAmount.getText().toString();
//                String expense_date = AddExpenseActivity.this.editText_Date.getText().toString();
////                String expense_time = AddExpenseActivity.this.editText_Time.getText().toString();
//                if (expense_name.isEmpty()) {
//                    AddExpenseActivity.this.editText_ExpenseName.setError(AddExpenseActivity.this.getString(R.string.expense_name_cannot_be_empty));
//                    AddExpenseActivity.this.editText_ExpenseName.requestFocus();
//                } else if (expense_amount.isEmpty()) {
//                    AddExpenseActivity.this.editText_ExpenseAmount.setError(AddExpenseActivity.this.getString(R.string.expense_amount_cannot_be_empty));
//                    AddExpenseActivity.this.editText_ExpenseAmount.requestFocus();
//                } else {
//                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddExpenseActivity.this);
//                    databaseAccess.open();
////                    if (databaseAccess.addExpense(expense_name, expense_amount, expense_note, expense_date, expense_time)) {
////                        Toasty.success(AddExpenseActivity.this, (int) R.string.expense_successfully_added, Toasty.LENGTH_SHORT).show();
////                        Intent intent = new Intent(AddExpenseActivity.this, ExpenseActivity.class);
////                        //intent.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
////                        AddExpenseActivity.this.startActivity(intent);
////                        return;
////                    }
//                    Toasty.error(AddExpenseActivity.this, (int) R.string.failed, Toasty.LENGTH_SHORT).show();
//                }
            }
        });

//        this.editText_categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                System.out.println(createExpenseCategoryListAdapter.getItem(i).getCategoryName());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    private void getExpenseCategoryList() {
        this.expensesCategoryList.clear();

        apiRequestHandler.getEpensesCategory(
                expenses -> {
                    // Handle the list of products here
                    if(expenses != null) {

                        // Add sample customer data (replace this with your actual customer data)
                        expensesCategoryList.addAll(expenses);

                        // Create and set the custom adapter
                        createExpenseCategoryListAdapter = new CreateExpenseCategoryListAdapter(this, expenses);

                        createExpenseCategoryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        this.editText_categorySpinner.setVerticalScrollBarEnabled(true);

                        this.editText_categorySpinner.setAdapter(createExpenseCategoryListAdapter);

                    } else {

                        Toasty.info(this, (int) R.string.no_expense_caegory_found, Toasty.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    Log.e(TAG, "Error fetching expense Category: " + error.getMessage());
                }
        );
    }

    private boolean validateFields() {
        if (isEmpty(editText_ExpenseName)) {
            showToast("Expense Name is required.");
            return false;
        }

        if (isEmpty(editText_ExpenseNote)) {
            showToast("Expense Note is required.");
            return false;
        }

        if (isEmpty(editText_ExpenseAmount)) {
            showToast("Expense Amount is required.");
            return false;
        }

        if (isEmpty(editText_Date)) {
            showToast("Expense Date is required.");
            return false;
        }

        if (editText_categorySpinner.equals("Choose a Category") || editText_categorySpinner.getSelectedItemId() == 0) {
            editText_categorySpinner.requestFocus();
            Toast.makeText(this, "Must Choose a Category", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to create this expense?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Call the method to create the expense
                createExpense();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void createExpense() {

        this.progressBar.setVisibility(View.VISIBLE);

        String expense_name = editText_ExpenseName.getText().toString().trim();
        String expense_note = editText_ExpenseNote.getText().toString().trim();
        String expense_amount = editText_ExpenseAmount.getText().toString().trim();
        String expense_date = editText_Date.getText().toString().trim();

        ExpenseCategoryModel selectedCategory = (ExpenseCategoryModel) editText_categorySpinner.getSelectedItem();
        String category = String.valueOf(selectedCategory.getId());

        // Make a POST request using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://megapos.co.ke/api/expenses/create";

        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token = user.getToken();

        JSONObject requestBody = new JSONObject();
        try {
//            requestBody.put("expense_name", expense_name);
            requestBody.put("details", expense_note);
            requestBody.put("amount", expense_amount);
            requestBody.put("date", expense_date);
            requestBody.put("category_id", category);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // Order created successfully
                                String message = response.getString("message");
                                showToast(message);

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ExpenseActivity.class));
                                finish();

                            } else {
                                // Some error occurred (optional)
                                progressBar.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "Error Submitting Request ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error: " + error.toString());
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(request);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();
        this.mYear = calendar.get(Calendar.YEAR);
        this.mMonth = calendar.get(Calendar.MONTH);
        this.mDay = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String fm = "" + month;
                String fd = "" + dayOfMonth;
                if (month < 10) {
                    fm = "0" + month;
                }
                if (dayOfMonth < 10) {
                    fd = "0" + dayOfMonth;
                }
                AddExpenseActivity.this.date_time = year + "-" + fm + "-" + fd;
                //AddExpenseActivity.this.date_time = fd + "-" + fm + "-" + year;
                AddExpenseActivity.this.editText_Date.setText(AddExpenseActivity.this.date_time);
            }
        }, this.mYear, this.mMonth, this.mDay).show();
    }

    private void timePicker() {
        Calendar calendar = Calendar.getInstance();
        this.mHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.mMinute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm;
                AddExpenseActivity.this.mHour = hourOfDay;
                AddExpenseActivity.this.mMinute = minute;
                if (AddExpenseActivity.this.mHour < 12) {
                    am_pm = "AM";
                    AddExpenseActivity.this.mHour = hourOfDay;
                } else {
                    am_pm = "PM";
                    AddExpenseActivity.this.mHour = hourOfDay - 12;
                }
                EditText editText = AddExpenseActivity.this.editText_Time;
                editText.setText(AddExpenseActivity.this.mHour + ":" + minute + " " + am_pm);
            }
        }, this.mHour, this.mMinute, false).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public class CreateExpenseCategoryListAdapter extends ArrayAdapter<ExpenseCategoryModel> {
        private List<ExpenseCategoryModel> customerModels;

        public CreateExpenseCategoryListAdapter(Context context, List<ExpenseCategoryModel> customerModels) {
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
            ExpenseCategoryModel customerModel = customerModels.get(position);

            // Set the customer name as the text for the list item
            TextView textView = itemView.findViewById(android.R.id.text1);
            textView.setText(customerModel.getCategoryName());

            return itemView;
        }
    }
}