package com.example.go4lunch_randa.models;

public class Reservation {
    private String bookingDate;
    private String workmateUid;
    private String restaurantId;
    private String restaurantName;

    public Reservation(String bookingDate, String workmateUid, String restaurantPlaceId, String restaurantName) {
        this.bookingDate = bookingDate;
        this.workmateUid = workmateUid;
        this.restaurantId = restaurantPlaceId;
        this.restaurantName = restaurantName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getUserId() {
        return workmateUid;
    }

    public void setUserId(String userId) {
        this.workmateUid = userId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
