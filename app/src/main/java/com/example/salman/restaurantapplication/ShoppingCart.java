package com.example.salman.restaurantapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salman on 7/6/2018.
 */

public class ShoppingCart {
    @SerializedName("ShoppingCartID")
    @Expose
    private Integer shoppingCartID;
    @SerializedName("CustomerID")
    @Expose
    private Integer customerID;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

    public Integer getShoppingCartID() {
        return shoppingCartID;
    }

    public void setShoppingCartID(Integer shoppingCartID) {
        this.shoppingCartID = shoppingCartID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

}

