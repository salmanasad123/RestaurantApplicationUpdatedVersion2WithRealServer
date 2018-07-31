package com.example.salman.restaurantapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Salman on 6/23/2018.
 */

public class AccountInfoAdapter extends RecyclerView.Adapter<AccountInfoAdapter.AccountInfoViewHolder> {

    private static final String TAG = "MTAG";
    private static final int PICK_IMAGE = 100;

    List<Customer> customers;
    CustomerAccountActivity activity;
    View view;
    Customer customer;

    String temp;


    public AccountInfoAdapter(CustomerAccountActivity customerAccountActivity, List<Customer> customerList) {
        this.activity = customerAccountActivity;
        this.customers = customerList;

    }

    @Override
    public AccountInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_account_single_row, parent, false);
        return new AccountInfoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(AccountInfoViewHolder holder, int position) {

        customer = customers.get(position);
        holder.Name.setText("" + customer.getName());
        holder.PhoneNo.setText(customer.getCustomerPhone());
        holder.address.setText(customer.getCustomerAddress());

        String temp = customer.getProfileImage();

        Log.d(TAG, "onBindViewHolder: " + temp);

        if (temp != null && !temp.equals("")) {


            byte[] decodedString = Base64.decode(temp, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imgView.setImageBitmap(decodedByte);
        }

    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class AccountInfoViewHolder extends RecyclerView.ViewHolder {


        TextView Name;
        EditText PhoneNo;
        EditText address;
        ImageView imgView;


        public AccountInfoViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.customerName);
            PhoneNo = itemView.findViewById(R.id.etAccountDetailPhone);
            address = itemView.findViewById(R.id.etAccountDetailAddress);
            imgView = itemView.findViewById(R.id.imgProfilePic);


        }
    }
}
