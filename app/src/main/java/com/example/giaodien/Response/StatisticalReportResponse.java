package com.example.giaodien.Response;

import java.util.List;

public class StatisticalReportResponse {
    private boolean success;
    private List<Data> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Data> getData() {
        return data;
    }

    public static class Data {
        private String room_type;
        private double total_amount;
        private int booking_count;

        public String getRoomType() {
            return room_type;
        }

        public double getTotalAmount() {
            return total_amount;
        }

        public int getBookingCount() {
            return booking_count;
        }
    }
}
