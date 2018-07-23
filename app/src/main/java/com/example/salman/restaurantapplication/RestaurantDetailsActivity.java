package com.example.salman.restaurantapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RestaurantDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MTAG";

    Gson gson;
    String target;
    Restaurant restaurant;
    int RestaurantIDfromIntent;

    TextView restaurantName;
    TextView restaurantAddress;
    TextView restaurantPhone;
    RatingBar restaurantRatingBar;
    Button OrderFood;
    Button GiveFeedback;
    ImageButton restaurantFb;

    SwipeRefreshLayout mySwipeRefreshLayout;

    double d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        mySwipeRefreshLayout = findViewById(R.id.swipeRefreshRestaurantDetails);

        restaurantName = findViewById(R.id.tvRestaurantName);
        restaurantAddress = findViewById(R.id.tvRestaurantAddress);
        restaurantPhone = findViewById(R.id.tvRestaurantPhone);
        restaurantRatingBar = findViewById(R.id.RestaurantratingBar);
        OrderFood = findViewById(R.id.Orderbutton);
        restaurantFb = findViewById(R.id.btnFacebook);
        GiveFeedback = findViewById(R.id.btnGiveFeedback);


        RestaurantIDfromIntent = getIntent().getIntExtra("MyObjectID", 0);


        /**
         *
         * Getting Object from other activity and converting it back to Gson
         */
        gson = new Gson();
        target = getIntent().getStringExtra("MyObjString");
        restaurant = gson.fromJson(target, Restaurant.class);

        /**
         * Setting Values of TextViews, Rating Bars etc
         */
        restaurantName.setText(restaurant.getRestaurantName());
        restaurantAddress.setText(restaurant.getRestaurantAddress());
        restaurantPhone.setText(restaurant.getRestaurantPhone().toString());
        RestaurantAverageRating();


        OrderFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RestaurantDetailsActivity.this, GetRestaurantMenuCategories.class);
                startActivity(intent);
            }
        });
        restaurantFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Gourmet-Restaurant-Sweets-470988696345213/"));
                startActivity(browserIntent);
            }
        });

        GiveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailsActivity.this, ReviewsActivity.class);
                intent.putExtra("MyObjectID", RestaurantIDfromIntent);
                startActivity(intent);
            }
        });

    }

    private void RestaurantAverageRating() {

        Retrofit retrofit = RetrofitClient.getClient();
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Double> doubleCall = apiInterface.AvgRating(RestaurantIDfromIntent);
        doubleCall.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                AverageRating averageRating = new AverageRating();
                averageRating.setRating(response.body());
                d = averageRating.getRating();

                restaurantRatingBar.setRating((float) d);


            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }


}
