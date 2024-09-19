package com.example.giaodien.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Activities.Adapters.RoomAdapter;
import com.example.giaodien.Activities.Model.RoomModel;
import com.example.giaodien.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvDateFrom, tvDateTo;
    private FloatingActionButton fabAdd;
    private RecyclerView recyclerViewRooms;
    private RoomAdapter roomAdapter;
    private List<RoomModel> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liên kết các thành phần giao diện
        tvDateFrom = findViewById(R.id.tv_date_from);
        tvDateTo = findViewById(R.id.tv_date_to);
        fabAdd = findViewById(R.id.fab_add);
        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        recyclerViewRooms.setLayoutManager(new GridLayoutManager(this, 3));

        // Dữ liệu mẫu cho danh sách phòng
        roomList = new ArrayList<>();
        roomList.add(new RoomModel("101", true));
        roomList.add(new RoomModel("102", false));
        roomList.add(new RoomModel("103", true));
        roomList.add(new RoomModel("104", false));
        roomList.add(new RoomModel("105", true));

        // Khởi tạo Adapter và kết nối với RecyclerView
        roomAdapter = new RoomAdapter(this, roomList);
        recyclerViewRooms.setAdapter(roomAdapter);

        // Thiết lập DatePickerDialog cho TextView chọn ngày bắt đầu
        tvDateFrom.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) ->
                            tvDateFrom.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                    year, month, day);
            datePickerDialog.show();
        });

        // Thiết lập DatePickerDialog cho TextView chọn ngày kết thúc
        tvDateTo.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, year12, monthOfYear, dayOfMonth) ->
                            tvDateTo.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year12),
                    year, month, day);
            datePickerDialog.show();
        });

        // Thiết lập FloatingActionButton (FAB) để thêm/sửa/xóa phòng
        fabAdd.setOnClickListener(view -> {
            // Xử lý khi nhấn FAB (Thêm hành động thêm/sửa/xóa ở đây)
            Toast.makeText(MainActivity.this, "Thêm/Sửa/Xóa phòng", Toast.LENGTH_SHORT).show();
        });

        // Thiết lập Tab Layout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Xử lý khi tab được chọn
                Toast.makeText(MainActivity.this, "Tab: " + tab.getText() + " được chọn", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
