package com.example.salman.restaurantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Retrofit;

public class PlaceOrderThankYouActivity extends AppCompatActivity {

    Button btnThankyou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_thank_you);

        btnThankyou = findViewById(R.id.btnThankYouActivity);
        btnThankyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceOrderThankYouActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
