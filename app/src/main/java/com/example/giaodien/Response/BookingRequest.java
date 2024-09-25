package com.example.giaodien.Response;

import com.example.giaodien.Model.Bookings;
import com.example.giaodien.Model.Customers;
import com.example.giaodien.Model.Room;

public class BookingRequest {
    private Bookings bookings;
    private Room room;
    private Customers customers;

    public BookingRequest(Bookings bookings, Room room, Customers customers) {
        this.bookings = bookings;
        this.room = room;
        this.customers = customers;
    }

    // Getters và Setters
    public Bookings getBookings() {
        return bookings;
    }

    public void setBookings(Bookings bookings) {
        this.bookings = bookings;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }
}
