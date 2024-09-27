package com.example.giaodien.Model;

public class Payments {
    private int payment_id;
    private int booking_id;
    private int staff_id;
    private String payment_date;
    private String amount;

    public Payments(int booking_id, int staff_id, String payment_date, String amount) {
        this.booking_id = booking_id;
        this.staff_id = staff_id;
        this.payment_date = payment_date;
        this.amount = amount;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
