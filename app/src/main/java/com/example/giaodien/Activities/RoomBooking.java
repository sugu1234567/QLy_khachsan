package com.example.giaodien.Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;

public class RoomBooking extends AppCompatActivity {
    private Button btnCancelBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_booking);
        Init();
        Cancel();
    }

    private void Cancel() {
        btnCancelBooking.setOnClickListener(view -> finish());
    }

    private void Init() {
        btnCancelBooking = findViewById(R.id.btnCancelBooking);
    }
}