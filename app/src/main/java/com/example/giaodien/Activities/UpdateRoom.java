package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;

public class UpdateRoom extends AppCompatActivity {
    private EditText etRoomName, etRoomType, etRoomNumber;
    private Button btnUpdate, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);

        // Lấy thông tin phòng từ Intent nếu cần thiết
        Intent intent = getIntent();
        String roomId = intent.getStringExtra("room_id");
        String roomNumber = intent.getStringExtra("room_number");
        Log.d("ROOMNUMBER", roomNumber);
        Init();
        CancelUpdateRoom();
        etRoomNumber.setText(roomNumber);
    }

    private void CancelUpdateRoom() {
        btnCancel.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    private void Init() {
        etRoomName = findViewById(R.id.RoomType);
        etRoomType = findViewById(R.id.RoomPrice);
        etRoomNumber = findViewById(R.id.etRoomNumber);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
    }
}
