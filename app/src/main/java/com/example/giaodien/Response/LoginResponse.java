package com.example.giaodien.Response;

import com.example.giaodien.Model.Staff;

public class LoginResponse {
    private boolean success;
    private String message;
    private Staff staff;

    public Staff getStaff() {
        return staff;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
