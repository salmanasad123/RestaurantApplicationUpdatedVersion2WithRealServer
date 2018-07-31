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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileUpdateActivity extends AppCompatActivity {

    private static final String TAG = "MTAG";

    EditText updatePhone;
    EditText updateAddress;
    EditText updateEmail;
    EditText updatePassword;
    Button profileUpdate;

    Integer customerIDfromEventBus;
    String phoneNumber;
    String Address;
    String Email;
    String Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        EventBus.getDefault().register(this);
        updatePhone = findViewById(R.id.updatePhone);
        updateAddress = findViewById(R.id.updateAddress);
        updateEmail = findViewById(R.id.updateEmail);
        updatePassword = findViewById(R.id.updatePassword);
        profileUpdate = findViewById(R.id.btnProfileUpdate);


        profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                phoneNumber = updatePhone.getText().toString();
                Address = updateAddress.getText().toString();
                Email = updateEmail.getText().toString();
                Password = updatePassword.getText().toString();


                if (updateEmail.getText().toString().trim().equals("")) {
                    updateEmail.setError("Email is Required");
                } else {

                    Boolean valid = isEmailValid(updateEmail.getText().toString());
                    if (valid == true) {
                        Email = updateEmail.getText().toString();
                    } else {
                        updateEmail.setError("Email is not Valid");
                    }

                }
                if (updatePhone.getText().toString().equals("")) {
                    updatePhone.setError("Phone is Required");

                } else if (updatePhone.getText().length() != 11) {
                    updatePhone.setError("Phone Number should be 11 digits");

                } else if (!updatePhone.getText().toString().startsWith("03")) {

                    updatePhone.setError("Phone Number Should Start with 03");

                } else {
                    phoneNumber = String.valueOf(updatePhone.getText());
                }

                if (updateAddress.getText().toString().trim().equals("")) {
                    updateAddress.setError("Address is Required");
                } else {
                    Address = updateAddress.getText().toString();
                }

                if (updatePassword.getText().toString().trim().equals("")) {
                    updatePassword.setError("Password is Required");
                } else if (updatePassword.getText().length() < 6) {
                    updatePassword.setError("Password should be 6 Characters long");
                } else {
                    Password = updatePassword.getText().toString();
                }


                Customer customer = new Customer(phoneNumber, Address, Email, Password);


                if (!phoneNumber.equals("") && phoneNumber.length() == 11 && !Address.equals("") && !Email.equals("") && isEmailValid(Email) && !Password.equals("") && Password.length() > 6) {
                    Retrofit retrofit = RetrofitClient.getClient();
                    ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                    Call<Customer> customerCall = apiInterface.updateProfile(customerIDfromEventBus, customer);

                    customerCall.enqueue(new Callback<Customer>() {
                        @Override
                        public void onResponse(Call<Customer> call, Response<Customer> response) {
                            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");


                            if (response.code() == 200) {
                                Snackbar.make(findViewById(android.R.id.content), "Profile Updated", Snackbar.LENGTH_SHORT)
                                        .setCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                                super.onDismissed(transientBottomBar, event);
                                                moveToLoginActivity();
                                            }
                                        })
                                        .show();

                            } else {
                                Snackbar.make(findViewById(android.R.id.content), "Fail to Update", Snackbar.LENGTH_LONG)
                                        .show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Customer> call, Throwable t) {
                            Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                        }
                    });

                }
            }
        });

    }


    public void moveToLoginActivity() {
        Intent intent = new Intent(ProfileUpdateActivity.this, Main2Activity.class);
        startActivity(intent);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(AccountIDEvent accountIDEvent) {
        customerIDfromEventBus = accountIDEvent.getId();
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
