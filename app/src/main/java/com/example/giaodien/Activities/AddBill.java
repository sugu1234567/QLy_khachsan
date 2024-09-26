package com.example.giaodien.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddBill extends AppCompatActivity {

    private TextView tvCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_bill);

        // Ánh xạ TextView hiển thị ngày tháng
        tvCurrentDate = findViewById(R.id.tvCurrentDate);

        // Lấy thời gian hiện tại và định dạng
        String currentDate = getCurrentDate();

        // Gán thời gian vào TextView, kết hợp với chuỗi "Ngày thanh toán:"
        tvCurrentDate.setText("Ngày thanh toán: " + currentDate);
    }

    // Phương thức lấy thời gian thực
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }
}
