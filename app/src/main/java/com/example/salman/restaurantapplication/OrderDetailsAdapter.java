package com.example.salman.restaurantapplication;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salman on 7/5/2018.
 */

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    private static final String TAG = "MTAG";
    List<OrderHistory> orderHistories;
    OrderDetailsActivity activity;
    Dialog dialog;
    TextView productNames;
    TextView productQuantities;
    TextView PaymentMethod;
    TextView totalBill;

    public OrderDetailsAdapter(OrderDetailsActivity orderDetailsActivity, List<OrderHistory> orderHistoryList) {
        this.activity = orderDetailsActivity;
        this.orderHistories = orderHistoryList;
    }


    @Override
    public OrderDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_single_row, parent, false);
        return new OrderDetailsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(OrderDetailsViewHolder holder, int position) {
        final OrderHistory orderHistory = orderHistories.get(position);
        holder.tvRestaurantName.setText(orderHistory.getRestaurantName());
        holder.tvRestaurantAddress.setText(orderHistory.getRestaurantAddress());
        holder.tvOrderType.setText(orderHistory.getOrderType());
        Log.d(TAG, "onBindViewHolder: " + orderHistory.getOrderStatus());

        if (orderHistory.getOrderStatus() == 0) {
            holder.showOrderStatus.setText("Awaiting Confirmation");
        } else if (orderHistory.getOrderStatus() == 1) {
            holder.showOrderStatus.setText("Cooking");
        } else if (orderHistory.getOrderStatus() == 2) {
            holder.showOrderStatus.setText("Ready");
        } else if (orderHistory.getOrderStatus() == 3) {
            holder.showOrderStatus.setText("Completed");
        }
        Picasso.with(activity)
                .load(orderHistory.getLink())
                .resize(300, 300)
                .into(holder.RestaurantImageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(activity);


                dialog.setContentView(R.layout.order_history_alert_dialog);
                productNames = dialog.findViewById(R.id.TvOrderHistoryProductNames);
                PaymentMethod = dialog.findViewById(R.id.tvPaymentMethod);
                productQuantities = dialog.findViewById(R.id.TvOrderHistoryProductQuantities);

                totalBill = dialog.findViewById(R.id.TvOrderHistoryTotalBillAmount);

                productNames.setGravity(Gravity.CENTER_HORIZONTAL);
                productNames.setText(orderHistory.getProductName());
                PaymentMethod.setText(orderHistory.getPaymentType());
                productQuantities.setText(orderHistory.getQuantity());
                totalBill.setText(orderHistory.getTotalAmount().toString());


                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    public class OrderDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView tvRestaurantName;
        TextView tvRestaurantAddress;
        TextView tvOrderType;
        ImageView RestaurantImageView;
        TextView showOrderStatus;


        public OrderDetailsViewHolder(View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.restaurantNameTextView);
            tvRestaurantAddress = itemView.findViewById(R.id.restaurantAddress);
            tvOrderType = itemView.findViewById(R.id.tvOrderType);
            RestaurantImageView = itemView.findViewById(R.id.restaurantImageView);
            showOrderStatus = itemView.findViewById(R.id.tvOrderStatus);

        }
    }
}
