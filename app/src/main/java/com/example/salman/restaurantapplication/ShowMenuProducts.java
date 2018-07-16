package com.example.salman.restaurantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowMenuProducts extends AppCompatActivity {

    private static final String TAG = "MTAG";
    public int getCategoryiD;
    public int RestaurantIDfromEventBus;
    List<GetMenuProducts> getMenuProducts;
    ProductsAdapter productsAdapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button viewCart;
    Toolbar menuProductsToolbar;
    EditText editText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ShowMenuProducts.this, CartActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu_products);

        viewCart = findViewById(R.id.ViewCart);
        editText = findViewById(R.id.SearchProductsEditText);

        recyclerView = findViewById(R.id.showProducts);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        menuProductsToolbar = findViewById(R.id.menuProductsToolbar);
        setTitle("Food Items");
        setSupportActionBar(menuProductsToolbar);

        getCategoryiD = getIntent().getIntExtra("myCategoryID", 0);

        EventBus.getDefault().register(this);

        Retrofit retrofit = RetrofitClient.getClient();


        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<GetMenuProducts>> listCall = apiInterface.getMenuProducts(getCategoryiD, RestaurantIDfromEventBus);
        listCall.enqueue(new Callback<List<GetMenuProducts>>() {

            @Override
            public void onResponse(Call<List<GetMenuProducts>> call, Response<List<GetMenuProducts>> response) {

                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                getMenuProducts = response.body();

                productsAdapter = new ProductsAdapter(ShowMenuProducts.this, getMenuProducts);
                recyclerView.setAdapter(productsAdapter);
            }

            @Override
            public void onFailure(Call<List<GetMenuProducts>> call, Throwable t) {

                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowMenuProducts.this, CartActivity.class);
                startActivity(intent);
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
            }
        });
    }

    public void filter(String text) {
        ArrayList<GetMenuProducts> filteredList = new ArrayList<>();
        for (GetMenuProducts menuProducts : getMenuProducts) {
            if (menuProducts.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(menuProducts);
            }
        }
        productsAdapter.filterList(filteredList);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(GetRestaurantIDEvent getRestaurantIDEvent) {

        RestaurantIDfromEventBus = getRestaurantIDEvent.getValue();

    }
}
