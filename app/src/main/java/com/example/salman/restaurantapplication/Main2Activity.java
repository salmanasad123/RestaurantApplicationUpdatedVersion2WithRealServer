package com.example.salman.restaurantapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LocationManager locationManager;


    TextView username;
    View mHeaderView;
    String Username;
    ImageButton filterButton;

    private static final String TAG = "MTAG";
    List<Restaurant> restaurants;
    Gson gson;
    EditText editText;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RestaurantAdapter restaurantAdapter;
    SharedPreferences sharedPreferences;
    Integer CustomerIDfromSharedPreferences;

    SwipeRefreshLayout mySwipeRefreshLayout;

    double UserLongitude;
    double UserLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        EventBus.getDefault().register(this);

        //// to prevent keyboard from popping up when activity is loaded ////
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Restaurants");
        setSupportActionBar(toolbar);

        mySwipeRefreshLayout = findViewById(R.id.Main2ActivitySwipeRefresh);

        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        Username = sharedPreferences.getString("username", "");
        CustomerIDfromSharedPreferences = sharedPreferences.getInt("customerID", 0);

        EventBus.getDefault().postSticky(new AccountIDEvent(CustomerIDfromSharedPreferences));

        recyclerView = findViewById(R.id.showRestaurants);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


        ImageView imageView = findViewById(R.id.mainImage);
        editText = findViewById(R.id.SearchEditText);
        filterButton = findViewById(R.id.btnFilterRestaurant);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView = navigationView.getHeaderView(0);
        username = mHeaderView.findViewById(R.id.usernameTextView);
        username.setText(Username);
        navigationView.getBackground().setAlpha(180);     // to make transparent
        navigationView.setNavigationItemSelectedListener(this);


        if (UserLongitude == 0.0 && UserLatitude == 0.0) {

            Retrofit retrofit = RetrofitClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            final Call<List<Restaurant>> restaurantList = apiInterface.getRestaurants();
            restaurantList.enqueue(new Callback<List<Restaurant>>() {

                @Override
                public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {

                    Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                    restaurants = response.body();


                /*
                Collections.sort(restaurants, new Comparator<Restaurant>() {
                    @Override
                    public int compare(Restaurant restaurant, Restaurant t1) {
                        return restaurant.getRating() > t1.getRating() ? -1 : (restaurant.getRating() < t1.getRating()) ? 1 : 0;
                    }
                });

                */
                    restaurantAdapter = new RestaurantAdapter(Main2Activity.this, restaurants);
                    recyclerView.setAdapter(restaurantAdapter);

                    /**
                     * Progress Bar will become invisible when data is loaded, its like
                     * a loading screen
                     */
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);


                }

                @Override
                public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                    Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                }


            });
        } else {
            Retrofit retrofit = RetrofitClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            final Call<List<Restaurant>> restaurantList = apiInterface.postLocationData(UserLongitude, UserLatitude);
            restaurantList.enqueue(new Callback<List<Restaurant>>() {

                @Override
                public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {

                    Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                    restaurants = response.body();


                /*
                Collections.sort(restaurants, new Comparator<Restaurant>() {
                    @Override
                    public int compare(Restaurant restaurant, Restaurant t1) {
                        return restaurant.getRating() > t1.getRating() ? -1 : (restaurant.getRating() < t1.getRating()) ? 1 : 0;
                    }
                });

                */
                    restaurantAdapter = new RestaurantAdapter(Main2Activity.this, restaurants);
                    recyclerView.setAdapter(restaurantAdapter);

                    /**
                     * Progress Bar will become invisible when data is loaded, its like
                     * a loading screen
                     */
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);


                }

                @Override
                public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                    Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                }


            });


        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        /**
         *  // FILTER RESTAURANTS ACCORDING TO RATING ////
         */

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = RetrofitClient.getClient();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<List<Restaurant>> listCall = apiInterface.getRestaurants();
                listCall.enqueue(new Callback<List<Restaurant>>() {
                    @Override
                    public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                        Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");

                        restaurants = response.body();


                        Collections.sort(restaurants, new Comparator<Restaurant>() {
                            @Override
                            public int compare(Restaurant restaurant, Restaurant t1) {
                                return restaurant.getRating() > t1.getRating() ? -1 : (restaurant.getRating() < t1.getRating()) ? 1 : 0;
                            }
                        });

                        restaurantAdapter = new RestaurantAdapter(Main2Activity.this, restaurants);
                        recyclerView.setAdapter(restaurantAdapter);

                    }

                    @Override
                    public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                        Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                    }
                });
            }
        });

        /**
         *  //////////////////// ON SWIPE REFRESH //////////////////////
         */
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (UserLongitude == 0.0 && UserLatitude == 0.0) {

                    Retrofit retrofit1 = RetrofitClient.getClient();
                    ApiInterface apiInterface1 = retrofit1.create(ApiInterface.class);
                    Call<List<Restaurant>> call = apiInterface1.getRestaurants();
                    call.enqueue(new Callback<List<Restaurant>>() {

                        @Override
                        public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {

                            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");

                            restaurants = response.body();
                            restaurantAdapter = new RestaurantAdapter(Main2Activity.this, restaurants);
                            recyclerView.setAdapter(restaurantAdapter);
                        }

                        @Override
                        public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                            Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                        }
                    });

                    mySwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(Main2Activity.this, CustomerAccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_order_history) {

            Intent intent = new Intent(Main2Activity.this, OrderDetailsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit().clear();
            editor.apply();
            Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(Main2Activity.this, AboutUsActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void filter(String text) {
        ArrayList<Restaurant> filteredList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getRestaurantName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }
        restaurantAdapter.filterList(filteredList);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LocationEvent locationEvent) {

        UserLongitude = locationEvent.getLongitude();
        UserLatitude = locationEvent.getLatitude();
        Toast.makeText(this, "   " + UserLongitude + "    " + UserLatitude, Toast.LENGTH_SHORT).show();
    }
}


