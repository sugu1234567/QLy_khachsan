package com.example.giaodien.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Adapters.StaffAdapter;
import com.example.giaodien.Model.Staff;
import com.example.giaodien.R;

import java.util.ArrayList;

public class StaffActivity extends AppCompatActivity {
    private RecyclerView recyclerViewStaff;
    private StaffAdapter staffAdapter;
    private ArrayList<Staff> staffList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, com.google.android.material.R.color.design_default_color_primary));
        // Khởi tạo view
        Init();

        // Khởi tạo danh sách nhân viên
        staffList = new ArrayList<>();

        // Thêm dữ liệu mẫu
        staffList.add(new Staff(1, "Ngô Văn Nam", "Nam", "Nhân Viên", "Ngovannam10022003@gmail.com", "123456", "nhanvien1", "123"));
        staffList.add(new Staff(2, "Nguyễn Trung Đức", "Nam", "Nhân Viên", "duc@gmail.com", "123456", "nhanvien1", "123"));
        staffList.add(new Staff(3, "Ngô Thị A", "Nữ", "Nhân Viên", "sad@gmail.com", "123456", "nhanvien1", "123"));
        staffList.add(new Staff(4, "Ngô Văn B", "Nam", "Nhân Viên", "Ngovannam10022003@gmail.com", "123456", "nhanvien1", "123"));
        // Thiết lập LayoutManager và Adapter cho RecyclerView
        recyclerViewStaff.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        staffAdapter = new StaffAdapter(staffList, this);
        recyclerViewStaff.setAdapter(staffAdapter);
    }

    // Khởi tạo các view
    private void Init() {
        recyclerViewStaff = findViewById(R.id.recyclerViewStaffs);
    }
}
