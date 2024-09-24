package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;

public class UpdateRoom extends AppCompatActivity {
    private EditText etRoomName, etRoomType, etRoomNumber;
    private Button btnUpdate, btnCancel;
    private Spinner spinnerRoomType;
    private String[] roomTypes = {"Standard", "Superior", "Premium", "Deluxe", "Suite"};

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
        Spiner();
        etRoomNumber.setText(roomNumber);
    }

    private void Spiner() {
        // Tạo ArrayAdapter với giao diện tùy chỉnh
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, roomTypes);
        adapter.setDropDownViewResource(R.layout.spinner_item);  // Cài đặt layout cho khi mở Spinner
        spinnerRoomType.setAdapter(adapter);
    }

    private void CancelUpdateRoom() {
        btnCancel.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    private void Init() {
        spinnerRoomType = findViewById(R.id.RoomType);
        etRoomType = findViewById(R.id.RoomPrice);
        etRoomNumber = findViewById(R.id.etRoomNumber);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
    }
}
