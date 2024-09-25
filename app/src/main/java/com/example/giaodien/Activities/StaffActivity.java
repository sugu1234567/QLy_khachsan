package com.example.giaodien.Activities;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
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

        // Khởi tạo view
        Init();

        // Ánh xạ Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hiển thị nút quay lại trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Hiển thị nút back
            getSupportActionBar().setDisplayShowTitleEnabled(false);  // Ẩn tiêu đề mặc định nếu có
        }

        // Xử lý sự kiện khi nhấn nút back (nút điều hướng)
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

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

        setupSwipeToDelete();
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private Drawable icon;
            private final ColorDrawable background = new ColorDrawable(ContextCompat.getColor(StaffActivity.this, R.color.red));

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // Không xử lý sự kiện di chuyển
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();  // Lấy vị trí nhân viên

                new AlertDialog.Builder(StaffActivity.this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc muốn xóa Nhân viên này?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            staffList.remove(position);
                            staffAdapter.notifyItemRemoved(position);
                        })
                        .setNegativeButton("Không", (dialog, which) -> {
                            staffAdapter.notifyItemChanged(position); // Khôi phục trạng thái ban đầu nếu hủy
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                if (dX < 0) { // Vuốt từ phải sang trái
                    icon = ContextCompat.getDrawable(StaffActivity.this, R.drawable.ic_delete);

                    background.setBounds(itemView.getRight() + (int) dX - backgroundCornerOffset, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom());
                    background.draw(c);

                    int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int iconTop = itemView.getTop() + iconMargin;
                    int iconBottom = iconTop + icon.getIntrinsicHeight();

                    int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    icon.draw(c);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewStaff); // Gắn sự kiện vuốt vào RecyclerView
    }

    // Khởi tạo các view
    private void Init() {
        recyclerViewStaff = findViewById(R.id.recyclerViewStaffs);
    }
}
