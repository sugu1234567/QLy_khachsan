package com.example.giaodien.Response;

import com.example.giaodien.Model.Bookings;
import com.example.giaodien.Model.Customers;
import com.example.giaodien.Model.Room;

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
