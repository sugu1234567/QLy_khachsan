package com.example.giaodien.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;

public class UpdateRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);

        // Lấy thông tin phòng từ Intent nếu cần thiết
        Intent intent = getIntent();
        String roomId = intent.getStringExtra("roomId");

        // Ánh xạ các View trong layout activity_update_room.xml
        EditText etRoomName = findViewById(R.id.RoomType);
        EditText etRoomType = findViewById(R.id.RoomPrice);

        // Giả sử lấy thông tin phòng từ cơ sở dữ liệu dựa vào roomId để hiển thị
        // set dữ liệu vào các EditText nếu có
        // etRoomName.setText(...);
        // etRoomType.setText(...);

        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnUpdate.setOnClickListener(v -> {
            // Xử lý lưu thông tin phòng đã cập nhật
            String updatedRoomName = etRoomName.getText().toString();
            String updatedRoomType = etRoomType.getText().toString();

            // TODO: Cập nhật thông tin phòng vào cơ sở dữ liệu

            // Thông báo thành công
            Toast.makeText(UpdateRoom.this, "Cập nhật thông tin phòng thành công", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình trước đó
        });
        btnCancel.setOnClickListener(v -> {
            // Quay lại màn hình chính
            finish(); // Đóng Activity và quay về màn hình trước đó
        });
    }
}
