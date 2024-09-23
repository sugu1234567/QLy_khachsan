package com.example.giaodien.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;

import java.util.Calendar;

public class UpdateBooking extends AppCompatActivity {
    private Button btnBackBooking;
    private TextView tvCheckInTime, tvCheckOutTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_booking);
        tvCheckInTime = findViewById(R.id.tvCheckInTime);
        tvCheckOutTime = findViewById(R.id.tvCheckOutTime);

        // Gán sự kiện lấy ngày cho Check In
        tvCheckInTime.setOnClickListener(v -> showDatePickerDialog(tvCheckInTime));

        // Gán sự kiện lấy ngày cho Check Out
        tvCheckOutTime.setOnClickListener(v -> showDatePickerDialog(tvCheckOutTime));
        Init();
        Cancel();
    }

    private void Init() {
        {
            btnBackBooking = findViewById(R.id.btnBackBooking);
        }
    }

    private void Cancel() {
        btnBackBooking.setOnClickListener(view -> finish());
    }

    private void showDatePickerDialog(TextView textView) {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateBooking.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Cập nhật TextView với ngày đã chọn
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textView.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }
}