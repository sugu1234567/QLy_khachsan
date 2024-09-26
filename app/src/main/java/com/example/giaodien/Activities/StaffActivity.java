package com.example.giaodien.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Adapters.StaffAdapter;
import com.example.giaodien.Model.Staff;
import com.example.giaodien.R;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffActivity extends AppCompatActivity {
    private RecyclerView recyclerViewStaff;
    private SearchView search_view_staff;
    private StaffAdapter staffAdapter;
    private ArrayList<Staff> staffList;
    private ActivityResultLauncher<Intent> launcher;
    private ApiService apiService;
    private FloatingActionButton fb_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Làm mới danh sách khi quay lại
                        fetchStaffs();
                    }
                }
        );

        // Khởi tạo view
        Init();
        toolBar();

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Khởi tạo danh sách nhân viên
        staffList = new ArrayList<>();
        // Thiết lập LayoutManager và Adapter cho RecyclerView
        recyclerViewStaff.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        staffAdapter = new StaffAdapter(staffList);
        recyclerViewStaff.setAdapter(staffAdapter);

        fetchStaffs();
        setupSwipeToDelete();
        filterRecyclerView();
        floatingActionButton();
    }

    private void floatingActionButton() {
        fb_add.setOnClickListener(view -> {
            Intent intent = new Intent(StaffActivity.this, AddStaff.class);
            launcher.launch(intent);
        });
    }

    private void filterRecyclerView() {
        search_view_staff.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                staffAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                staffAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void fetchStaffs() {
        apiService.getStaffs().enqueue(new Callback<List<Staff>>() {
            @Override
            public void onResponse(Call<List<Staff>> call, Response<List<Staff>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    staffList.clear();
                    staffList.addAll(response.body());
                    staffAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e("MainActivity", "Response failed");
                    Toast.makeText(StaffActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Staff>> call, Throwable t) {
                Log.e("MainActivity", "API call failed: " + t.getMessage());
                Toast.makeText(StaffActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toolBar() {
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
                            int staff_id = staffList.get(position).getStaff_id();
                            apiService.deleteStaff(staff_id).enqueue(new Callback<DataResponse>() {
                                @Override
                                public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                                    if(response.isSuccessful() && response.body()!=null){
                                        DataResponse dataResponse = response.body();
                                        if(dataResponse.isSuccess()){
                                            Toast.makeText(StaffActivity.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            staffList.remove(position);
                                            staffAdapter.notifyItemRemoved(position);
                                        }
                                        else{
                                            Toast.makeText(StaffActivity.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<DataResponse> call, Throwable t) {
                                    Toast.makeText(StaffActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("Error: ", t.getMessage());
                                }
                            });
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
        search_view_staff = findViewById(R.id.search_view_staff);
        fb_add = findViewById(R.id.fab_add);
    }
}
