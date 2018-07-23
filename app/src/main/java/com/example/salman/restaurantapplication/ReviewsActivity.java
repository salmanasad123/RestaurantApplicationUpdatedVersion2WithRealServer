package com.example.salman.restaurantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewsActivity extends AppCompatActivity {

    List<Feedback> feedbackList;
    FeedbackAdapter feedbackAdapter;
    int RestaurantIDfromIntent;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button givefeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        RestaurantIDfromIntent = getIntent().getIntExtra("MyObjectID", 0);

        givefeedback = findViewById(R.id.giveFeedbackButton);
        recyclerView = findViewById(R.id.ReviewsActivityRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Retrofit retrofit = RetrofitClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Feedback>> listCall = apiInterface.getFeedback(RestaurantIDfromIntent);
        listCall.enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {

                feedbackList = response.body();
                feedbackAdapter = new FeedbackAdapter(ReviewsActivity.this, feedbackList);
                recyclerView.setAdapter(feedbackAdapter);
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {

            }
        });

        givefeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewsActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }
}
