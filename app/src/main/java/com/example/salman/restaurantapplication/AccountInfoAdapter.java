package com.example.salman.restaurantapplication;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Salman on 6/23/2018.
 */

public class AccountInfoAdapter extends RecyclerView.Adapter<AccountInfoAdapter.AccountInfoViewHolder> {

    List<Customer> customers;
    CustomerAccountActivity activity;
    View view;
    Customer customer;


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


    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class AccountInfoViewHolder extends RecyclerView.ViewHolder {


        TextView Name;
        EditText PhoneNo;
        EditText address;


        public AccountInfoViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.customerName);
            PhoneNo = itemView.findViewById(R.id.etAccountDetailPhone);
            address = itemView.findViewById(R.id.etAccountDetailAddress);


        }
    }
}
