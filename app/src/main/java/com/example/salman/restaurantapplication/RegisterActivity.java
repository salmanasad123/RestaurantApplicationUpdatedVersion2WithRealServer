package com.example.salman.restaurantapplication;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "MTAG";


    EditText etName;
    EditText etEmail;
    EditText etPhone;
    EditText etAddress;
    EditText etCity;
    EditText etPassword;
    Button btnRegister;
    android.support.v7.widget.Toolbar toolbar;

    //Variables Passed to Register Form via Post Request

    String CustomerName;
    String CustomerEmail;
    Editable CustomerPhone;
    String CustomerAddress;
    String CustomerCity;
    String CustomerPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = findViewById(R.id.registerActivitToolbar);


        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);

        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().trim().equals("")) {
                    etName.setError("Name is Required");
                } else {
                    CustomerName = etName.getText().toString();
                }

                if (etEmail.getText().toString().trim().equals("")) {
                    etEmail.setError("Email is Required");
                } else {

                    Boolean valid = isEmailValid(etEmail.getText().toString());
                    if (valid == true) {
                        CustomerEmail = etEmail.getText().toString();
                    } else {
                        etEmail.setError("Email is not Valid");
                    }

                }
                if (etPhone.getText().toString().equals("")) {
                    etPhone.setError("Phone is Required");

                } else if (etPhone.getText().length() != 11) {
                    etPhone.setError("Phone Number should be 11 digits");

                } else if (!etPhone.getText().toString().startsWith("03")) {

                    etPhone.setError("Phone Number Should Start with 03");

                } else {
                    CustomerPhone = etPhone.getText();
                }

                if (etAddress.getText().toString().trim().equals("")) {
                    etAddress.setError("Address is Required");
                } else {
                    CustomerAddress = etAddress.getText().toString();
                }
                if (etCity.getText().toString().trim().equals("")) {
                    etCity.setError("City is Required");
                } else {
                    CustomerCity = etCity.getText().toString();
                }
                if (etPassword.getText().toString().trim().equals("")) {
                    etPassword.setError("Password is Required");
                } else if (etPassword.getText().length() < 6) {
                    etPassword.setError("Password should be 6 Characters long");
                } else {
                    CustomerPassword = etPassword.getText().toString();
                }


                final Retrofit retrofit = RetrofitClient.getClient();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);


                Call<Customer> customerCall = apiInterface.registerCustomer(CustomerName, CustomerEmail,
                        (CustomerPhone), CustomerAddress, CustomerCity, CustomerPassword);

                customerCall.enqueue(new Callback<Customer>() {
                    @Override
                    public void onResponse(Call<Customer> call, Response<Customer> response) {
                        Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");


                        //Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                        if (response.isSuccessful()) {

                            Snackbar.make(findViewById(android.R.id.content), "Successfully Registered", Snackbar.LENGTH_SHORT)
                                    .setCallback(new Snackbar.Callback() {
                                        @Override
                                        public void onDismissed(Snackbar transientBottomBar, int event) {
                                            super.onDismissed(transientBottomBar, event);
                                            moveToLoginActivity();
                                        }
                                    })
                                    .show();


                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Failed To Register, Email or Phone Number May Already Exist", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Customer> call, Throwable t) {
                        Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");

                    }
                });


            }
        });


    }

    public void moveToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }


}

