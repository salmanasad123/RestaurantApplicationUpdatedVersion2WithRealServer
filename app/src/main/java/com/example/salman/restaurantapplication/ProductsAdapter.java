package com.example.salman.restaurantapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Salman on 6/7/2018.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.productsViewHolder> {

    private static final String TAG = "MTAG";

    public int restaurantIdFromEventBus;
    public Integer customerIDfromEventBus;
    ShowMenuProducts showMenuProducts;
    List<GetMenuProducts> getMenuProducts;
    List<Cart> cartList;
    SharedPreferences sharedPreferences;
    String name;

    /**
     * // should be made static because the products adapter class is loaded again for every item clicked
     */
    static int shoppingcartid;  // should be made static because the products adapter class is loaded again for every item clicked
    static int customerIDfromshoppingcartstable;


    public ProductsAdapter(ShowMenuProducts showMenuProducts, List<GetMenuProducts> getMenuProducts) {
        this.showMenuProducts = showMenuProducts;
        this.getMenuProducts = getMenuProducts;


        EventBus.getDefault().register(this);
    }

    @Override
    public productsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_products_single_row, parent, false);
        return new productsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final productsViewHolder holder, final int position) {
        final GetMenuProducts products = getMenuProducts.get(position);

        sharedPreferences = showMenuProducts.getSharedPreferences("ProductsSP", Context.MODE_PRIVATE);
        sharedPreferences.getString("btnState" + products.getRestaurantID() + products.getProductName(), "None");

        Log.d(TAG, "onBindViewHolder: SharedPref" + name);

        if (sharedPreferences.getString("btnState" + products.getRestaurantID()
                + products.getProductName(), "None").equals(products.getProductName())) {
            holder.addProductbtn.setEnabled(false);
            holder.addProductbtn.setText("Added");
        } else {
            holder.addProductbtn.setEnabled(true);
            holder.addProductbtn.setText("Add");
        }

        Log.d(TAG, "onBindViewHolder: " + products.getProductName() + " " + products.getPrice());

        holder.productName.setText(products.getProductName());
        holder.productPrice.setText("PKR  " + products.getPrice().toString());


        holder.addProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (customerIDfromEventBus != customerIDfromshoppingcartstable) {


                    Retrofit retrofit1 = RetrofitClient.getClient();
                    ApiInterface apiInterface1 = retrofit1.create(ApiInterface.class);
                    Call<ShoppingCart> shoppingCartCall = apiInterface1.postToShoppingCart(customerIDfromEventBus);

                    shoppingCartCall.enqueue(new Callback<ShoppingCart>() {
                        @Override
                        public void onResponse(Call<ShoppingCart> call, Response<ShoppingCart> response) {
                            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");

                            shoppingcartid = response.body().getShoppingCartID();
                            customerIDfromshoppingcartstable = response.body().getCustomerID();


                            addtocart();

                        }

                        @Override
                        public void onFailure(Call<ShoppingCart> call, Throwable t) {
                            Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                        }
                    });

                } else {
                    addtocart();
                }
            }

            public void addtocart() {
                Retrofit retrofit = RetrofitClient.getClient();


                final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                final Call<Cart> cartCall = apiInterface.addToCart(products.getProductID(), 1, shoppingcartid, restaurantIdFromEventBus);


                cartCall.enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");

                        if (response.code() == 500) {
                            Toast.makeText(showMenuProducts, "Item Already In Cart", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(showMenuProducts, "Item Added To Cart  " + products.getProductName(), Toast.LENGTH_SHORT).show();


                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            holder.addProductbtn.setEnabled(false);
                            holder.addProductbtn.setText("Added");

                            editor.putString("btnState" + products.getRestaurantID() + products.getProductName(), products.getProductName());
                            editor.apply();

                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                    }

                });


            }
        });


    }


    @Override
    public int getItemCount() {
        return getMenuProducts.size();
    }

    public void filterList(ArrayList<GetMenuProducts> filteredList) {
        getMenuProducts = filteredList;
        notifyDataSetChanged();
    }

    public class productsViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        Button addProductbtn;


        public productsViewHolder(View itemView) {

            super(itemView);
            productImage = itemView.findViewById(R.id.ProductImageView);
            productName = itemView.findViewById(R.id.ProductName);
            productPrice = itemView.findViewById(R.id.ProductPrice);
            addProductbtn = itemView.findViewById(R.id.addProducts);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(GetRestaurantIDEvent restaurantIDEvent) {
        restaurantIdFromEventBus = restaurantIDEvent.getValue();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(AccountIDEvent accountIDEvent) {
        customerIDfromEventBus = accountIDEvent.getId();
    }
}
