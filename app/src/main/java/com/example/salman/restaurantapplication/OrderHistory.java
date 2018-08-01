package com.example.salman.restaurantapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salman on 7/5/2018.
 */

public class OrderHistory {

    @SerializedName("OrderDetailsID")
    @Expose
    private Integer orderDetailsID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("OrderType")
    @Expose
    private String orderType;
    @SerializedName("TotalAmount")
    @Expose
    private Integer totalAmount;
    @SerializedName("OrderStatus")
    @Expose
    private Integer orderStatus;
    @SerializedName("CustomerID")
    @Expose
    private Integer customerID;
    @SerializedName("RestaurantID")
    @Expose
    private Integer restaurantID;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("RestaurantName")
    @Expose
    private String restaurantName;
    @SerializedName("Link")
    @Expose
    private String link;
    @SerializedName("RestaurantAddress")
    @Expose
    private String restaurantAddress;
    @SerializedName("ShoppingCartID")
    @Expose
    private Integer shoppingCartID;

    public Integer getShoppingCartID() {
        return shoppingCartID;
    }

    public void setShoppingCartID(Integer shoppingCartID) {
        this.shoppingCartID = shoppingCartID;
    }

    public Integer getOrderDetailsID() {
        return orderDetailsID;
    }

    public void setOrderDetailsID(Integer orderDetailsID) {
        this.orderDetailsID = orderDetailsID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Integer restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}

