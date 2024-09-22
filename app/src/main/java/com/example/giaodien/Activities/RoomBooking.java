package com.example.giaodien.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;

public class RoomBooking extends AppCompatActivity {
    private Button btnCancelBooking, btnConfirmBooking;
    private EditText etCustomerName, etCustomerPhone, etCustomerId;
    private RadioButton rbMale, rbFemale;
    private RadioGroup radioGroup;
    private TextView tvRoomNumber, tvCheckInTime, tvCheckOutTime, tvTotalPriceAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_booking);
        Init();
        Cancel();
        ConfirmBooking();
    }

    private void ConfirmBooking() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String sex = "";
        if(selectedId == R.id.rbMale) sex = "Nam";
        else if(selectedId == R.id.rbFemale) sex = "Nữ";

        String name = etCustomerName.getText().toString();
        String phone = etCustomerPhone.getText().toString();
        String cccd = etCustomerId.getText().toString();
    }

    private void Cancel() {
        btnCancelBooking.setOnClickListener(view -> finish());
    }

    private void Init() {
        btnCancelBooking = findViewById(R.id.btnCancelBooking);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        etCustomerId = findViewById(R.id.etCustomerId);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        radioGroup = findViewById(R.id.radioGroup);
        tvRoomNumber = findViewById(R.id.tvRoomNumber);
        tvCheckInTime = findViewById(R.id.tvCheckInTime);
        tvCheckOutTime = findViewById(R.id.tvCheckOutTime);
        tvTotalPriceAmount = findViewById(R.id.tvTotalPriceAmount);
    }
}