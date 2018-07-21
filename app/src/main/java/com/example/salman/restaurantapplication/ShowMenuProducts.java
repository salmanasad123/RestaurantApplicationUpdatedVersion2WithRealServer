package com.example.salman.restaurantapplication;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ImageButton;

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

    RecyclerView recyclerViewMenuProducts;
    RecyclerView.LayoutManager layoutManager;

    EditText etSearch;
    SwipeRefreshLayout MenuProductsSwipeRefreshLayout;

    Toolbar myToolbar;
    ImageButton imgButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu_products_updated);
        MenuProductsSwipeRefreshLayout = findViewById(R.id.MenuProductsSwipeRefresh);
        myToolbar = findViewById(R.id.menuProductsToolbarUpdated);
        setSupportActionBar(myToolbar);
        imgButton = findViewById(R.id.cartImageButton);

        etSearch = findViewById(R.id.EtMenuProductsSearch);

        recyclerViewMenuProducts = findViewById(R.id.MenuProductsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewMenuProducts.setLayoutManager(layoutManager);


        getCategoryiD = getIntent().getIntExtra("myCategoryID", 0);
        EventBus.getDefault().register(this);


        /**
         * Retrofit Client
         */
        Retrofit retrofit = RetrofitClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<GetMenuProducts>> listCall = apiInterface.getMenuProducts(getCategoryiD, RestaurantIDfromEventBus);
        listCall.enqueue(new Callback<List<GetMenuProducts>>() {

            @Override
            public void onResponse(Call<List<GetMenuProducts>> call, Response<List<GetMenuProducts>> response) {

                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                getMenuProducts = response.body();

                productsAdapter = new ProductsAdapter(ShowMenuProducts.this, getMenuProducts);
                recyclerViewMenuProducts.setAdapter(productsAdapter);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<GetMenuProducts>> call, Throwable t) {

                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });


        MenuProductsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerViewMenuProducts.setAdapter(productsAdapter);
                MenuProductsSwipeRefreshLayout.setRefreshing(false);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
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


        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowMenuProducts.this, CartActivity.class);
                startActivity(intent);
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
