package com.example.giaodien.Model;

public class Bookings {
    private int booking_id;
    private int customer_id;
    private int room_id;
    private String check_in_date;
    private String check_out_date;
    private String price_booking;
    private String status;
    private String roomNumber;
    private String customerName;


    public Bookings(String check_in_date, String check_out_date, String price_booking) {
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.price_booking = price_booking;
    }
    public Bookings(String roomNumber, String customerName, String check_in_date, String check_out_date, String status, String price_booking) {
        this.roomNumber = roomNumber;
        this.customerName = customerName;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.status = status;
        this.price_booking = price_booking;
    }
    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice_booking() {
        return price_booking;
    }

    public void setPrice_booking(String price_booking) {
        this.price_booking = price_booking;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
