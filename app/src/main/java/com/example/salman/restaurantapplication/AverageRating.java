package com.example.salman.restaurantapplication;

/**
 * Created by Salman on 7/24/2018.
 */


/**
 * This class is used to convert Double to double, since I get null pointer exception so I create this class
 * and I create object of this class and response.body() is used to set the value the private field in this class.
 * and after that it will be converted to double.
 * Double d = response.body() gives null pointer exception
 */
public class AverageRating {
    private Double rating;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
