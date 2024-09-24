package com.example.giaodien.Model;

public class BookingDetailsResponse {
    private boolean success;
    private Bookings booking;
    private Customers customer;
    private Room room;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public Bookings getBooking() {
        return booking;
    }

    public Customers getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }
}
