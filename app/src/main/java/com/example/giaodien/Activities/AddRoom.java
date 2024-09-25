package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.giaodien.R;

public class AddRoom extends AppCompatActivity {
    private EditText etAddRoomNumber, etAddRoomPrice;
    private Spinner AddRoomType, AddRoomStatus;
    private Button btnAddRoom, btnCancelAddRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_room);
        Init();
        Cancel();
        OnBackPressed();
        
    }

    private void Cancel() {
        btnCancelAddRoom.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    private void OnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                Intent intent = new Intent(RoomBooking .this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void Init() {
        etAddRoomNumber = findViewById(R.id.etAddRoomNumber);
        etAddRoomPrice = findViewById(R.id.etAddRoomPrice);
        AddRoomType = findViewById(R.id.AddRoomType);
        AddRoomStatus = findViewById(R.id.AddRoomStatus);
        btnAddRoom = findViewById(R.id.btnAddRoom);
        btnCancelAddRoom = findViewById(R.id.btnCancelAddRoom);
    }
}