package com.example.salman.restaurantapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MTAG";


    Integer CustomerIDfromSharedPreference;
    List<OrderHistory> orderHistoryList;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    OrderDetailsAdapter orderDetailsAdapter;

    SharedPreferences preferences;

    SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        refreshLayout = findViewById(R.id.swipeRefreshOrderDetails);
        preferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        CustomerIDfromSharedPreference = preferences.getInt("customerID", 0);

        recyclerView = findViewById(R.id.orderDetailsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final Retrofit retrofit = RetrofitClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        final Call<List<OrderHistory>> listCall = apiInterface.getOrderHistory(CustomerIDfromSharedPreference);


        listCall.enqueue(new Callback<List<OrderHistory>>() {
            @Override
            public void onResponse(Call<List<OrderHistory>> call, Response<List<OrderHistory>> response) {

                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                orderHistoryList = response.body();
                orderDetailsAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderHistoryList);
                recyclerView.setAdapter(orderDetailsAdapter);


            }

            @Override
            public void onFailure(Call<List<OrderHistory>> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Retrofit retrofit1 = RetrofitClient.getClient();
                ApiInterface apiInterface1 = retrofit1.create(ApiInterface.class);
                Call<List<OrderHistory>> call = apiInterface1.getOrderHistory(CustomerIDfromSharedPreference);

                call.enqueue(new Callback<List<OrderHistory>>() {
                    @Override
                    public void onResponse(Call<List<OrderHistory>> call, Response<List<OrderHistory>> response) {
                        Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                        orderHistoryList = response.body();
                        orderDetailsAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderHistoryList);
                        recyclerView.setAdapter(orderDetailsAdapter);


                    }

                    @Override
                    public void onFailure(Call<List<OrderHistory>> call, Throwable t) {
                        Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                    }
                });

                refreshLayout.setRefreshing(false);
            }
        });
    }

}

