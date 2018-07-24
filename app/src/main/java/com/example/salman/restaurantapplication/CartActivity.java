package com.example.salman.restaurantapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CartActivity extends AppCompatActivity {

    private static final String TAG = "MTAG";

    public int RestaurantIDFromEventBus;
    double Total;
    RecyclerView cartRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<Cart> cartList;
    TextView cartTotalAmount;
    TextView cartSubTotalAmount;
    TextView cartTaxAmount;
    Button placeOrder;
    Toolbar toolbar;
    CartAdapter cartAdapter;

    List<Cart> OrderDetailsList;

    SharedPreferences preferences;
    Integer CustomerIDfromSharedPreference;

    AlertDialog alertDialog;
    RadioGroup radioGroupOrderType;
    RadioGroup paymentTypeRadioGroup;

    public String orderType;
    public String paymentType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        preferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        CustomerIDfromSharedPreference = preferences.getInt("customerID", 0);


        cartRecyclerView = findViewById(R.id.CartRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        cartRecyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration = new DividerItemDecoration(cartRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        cartRecyclerView.addItemDecoration(dividerItemDecoration);


        cartSubTotalAmount = findViewById(R.id.CartSubTotalAmount);
        cartTaxAmount = findViewById(R.id.CartTaxAmount);
        cartTotalAmount = findViewById(R.id.CartTotalAmount);
        placeOrder = findViewById(R.id.btnPlaceOrder);

        toolbar = findViewById(R.id.cartActivityToolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);

        Retrofit retrofit = RetrofitClient.getClient();


        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Cart>> listCall = apiInterface.getCart(CustomerIDfromSharedPreference, RestaurantIDFromEventBus);

        listCall.enqueue(new Callback<List<Cart>>() {

            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                cartList = response.body();
                cartAdapter = new CartAdapter(CartActivity.this, cartList);
                cartRecyclerView.setAdapter(cartAdapter);


            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {

                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");

            }
        });


        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clear();

                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                View placeOrder = LayoutInflater.from(CartActivity.this).inflate(R.layout.cart_alert_dialog, null);
                radioGroupOrderType = placeOrder.findViewById(R.id.RadioGroupOrderType);
                paymentTypeRadioGroup = placeOrder.findViewById(R.id.RadioGroupPaymentType);

                builder.setView(placeOrder);
                alertDialog = builder.create();
                alertDialog.show();

                RadioButton radioButton = radioGroupOrderType.findViewById(R.id.radioBtnCashOnDelivery);
                RadioButton radioButton1 = radioGroupOrderType.findViewById(R.id.radioBtnDineIn);
                RadioButton radioButton2 = radioGroupOrderType.findViewById(R.id.radioBtnTakeAway);

                RadioButton radioButton3 = paymentTypeRadioGroup.findViewById(R.id.Paypal);
                RadioButton radioButton4 = paymentTypeRadioGroup.findViewById(R.id.Cash);


                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            orderType = "Cash on Delivery";

                        }
                    }

                });

                radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            orderType = "Dine In";

                        }
                    }
                });

                radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            orderType = "Take Away";

                        }
                    }

                });

                radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            paymentType = "Paypal/CreditCard";
                            postSelection();
                        }
                    }
                });

                radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        paymentType = "Cash";
                        postSelection();
                    }
                });
            }

            public void postSelection() {

                Retrofit retrofit = RetrofitClient.getClient();
                final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<List<Cart>> listCall = apiInterface.getCart(CustomerIDfromSharedPreference, RestaurantIDFromEventBus);

                listCall.enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {

                        Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");

                        OrderDetailsList = response.body();


                        for (int i = 0; i < OrderDetailsList.size(); i++) {


                            Retrofit retrofit2 = RetrofitClient.getClient();
                            ApiInterface apiInterface2 = retrofit2.create(ApiInterface.class);
                            Call<DetailsOrder> orderCall = apiInterface2.PostOrderDetails(OrderDetailsList.get(i).getProductName(),
                                    OrderDetailsList.get(i).getQuantity(), orderType, (int) Total, CustomerIDfromSharedPreference,
                                    RestaurantIDFromEventBus, OrderDetailsList.get(i).getShoppingCartID());

                            orderCall.enqueue(new Callback<DetailsOrder>() {
                                @Override
                                public void onResponse(Call<DetailsOrder> call, Response<DetailsOrder> response) {

                                    Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");


                                    /**
                                     *  Clear cart for specific customer after place order button is pressed
                                     */
                                    Retrofit retrofit1 = RetrofitClient.getClient();
                                    ApiInterface apiInterface1 = retrofit1.create(ApiInterface.class);
                                    Call<Cart> call1 = apiInterface1.EmptyCart(OrderDetailsList.get(0).getShoppingCartID());
                                    call1.enqueue(new Callback<Cart>() {
                                        @Override
                                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                                            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");


                                            if (paymentType == "Cash") {
                                                Intent intent = new Intent(CartActivity.this, PlaceOrderThankYouActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(CartActivity.this, PaypalActivity.class);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Cart> call, Throwable t) {
                                            Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                                        }
                                    });

                                }

                                @Override
                                public void onFailure(Call<DetailsOrder> call, Throwable t) {

                                    Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                                }

                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<List<Cart>> call, Throwable t) {

                        Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                    }
                });

            }
        });
    }

   

    /**
     * After user click on the place order button this will be reset to zero
     * to allow user to add items to cart again, add button becomes enabled again
     * if (customerIDfromEventBus != customerIDfromshoppingcartstable) in the products adapter
     */
    public static void clear() {
        ProductsAdapter productsAdapter = null;
        productsAdapter.shoppingcartid = 0;
        productsAdapter.customerIDfromshoppingcartstable = 0;

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(GetRestaurantIDEvent event) {
        RestaurantIDFromEventBus = event.getValue();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onevent(CartTotalEvent cartTotalEvent) {

        Total = cartTotalEvent.getTotal();

        if (Total >= 0) {
            cartSubTotalAmount.setText(Total + "");
            cartTaxAmount.setText("17%");
            Total += Total * 0.17;
            cartTotalAmount.setText(Total + "");
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();

    }
}

