package com.example.giaodien.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;

import java.util.Calendar;

public class UpdateBooking extends AppCompatActivity {
    private Button btnBackBooking;
    private TextView tvCheckInTime, tvCheckOutTime;
    private EditText etCustomerName, etCustomerPhone, etCustomerId;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_booking);

        Init();
        dateTimePicker();
        Cancel();
    }




    private void Cancel() {
        btnBackBooking.setOnClickListener(view -> {
            Intent intent = new Intent(UpdateBooking.this, MainActivity.class);
            UpdateBooking.this.finish();
            startActivity(intent);
        });
    }

    private void dateTimePicker() {
        // Thiết lập DatePickerDialog cho TextView chọn ngày bắt đầu
        tvCheckInTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String dateFrom = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        tvCheckInTime.setText("Ngày vào: "+dateFrom);

                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Thiết lập DatePickerDialog cho TextView chọn ngày kết thúc
        tvCheckOutTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year12, monthOfYear, dayOfMonth) -> {
                        String dateTo = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year12;
                        tvCheckOutTime.setText("Ngày ra: "+dateTo);

                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }
    private void Init() {
        btnBackBooking = findViewById(R.id.btnBackBookingUpdate);
        tvCheckInTime = findViewById(R.id.tvCheckInTimeUpdate);
        tvCheckOutTime = findViewById(R.id.tvCheckOutTimeUpdate);
        etCustomerName = findViewById(R.id.etCustomerNameUpdate);
        etCustomerPhone = findViewById(R.id.etCustomerPhoneUpdate);
        etCustomerId = findViewById(R.id.etCustomerIdUpdate);
        radioGroup = findViewById(R.id.radioGroupUpdate);
    }
}