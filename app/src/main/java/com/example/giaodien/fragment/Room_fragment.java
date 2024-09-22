package com.example.giaodien.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Activities.MainActivity;
import com.example.giaodien.Adapters.RoomAdapter;
import com.example.giaodien.Model.Room;
import com.example.giaodien.R;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Room_fragment extends Fragment {

    private RecyclerView recyclerViewRooms;
    private RoomAdapter roomAdapter;
    private ArrayList<Room> roomList;
    private ApiService apiService;
    private TextView tvDateFrom, tvDateTo;
    private FloatingActionButton fabAdd;
    private TabLayout tabLayout;
    public Room_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        Init(view);
        dateTimePicker();
        tabLayout();
        floatingActionButton();
        apiService = RetrofitClient.getClient().create(ApiService.class);
        recyclerViewRooms.setLayoutManager(new GridLayoutManager(getContext(), 3));

        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList);
        recyclerViewRooms.setAdapter(roomAdapter);

        fetchRooms();

        return  view;
    }

    private void floatingActionButton() {
        // Thiết lập FloatingActionButton (FAB) để thêm/sửa/xóa phòng
        fabAdd.setOnClickListener(view -> {
            // Xử lý khi nhấn FAB (Thêm hành động thêm/sửa/xóa ở đây)
            Toast.makeText(getContext(), "Thêm/Sửa/Xóa phòng", Toast.LENGTH_SHORT).show();
        });
    }

    private void tabLayout() {
        // Thiết lập Tab Layout

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Xử lý khi tab được chọn
                Toast.makeText(getContext(), "Tab: " + tab.getText() + " được chọn", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void dateTimePicker() {
        // Thiết lập DatePickerDialog cho TextView chọn ngày bắt đầu
        tvDateFrom.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year12, monthOfYear, dayOfMonth) ->
                            tvDateTo.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year12),
                    year, month, day);
            datePickerDialog.show();
        });
    }


    private void fetchRooms() {
        apiService.getRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    roomList.clear();
                    roomList.addAll(response.body());
                    roomAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e("MainActivity", "Response failed");
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.e("MainActivity", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Init(View view) {
        recyclerViewRooms = view.findViewById(R.id.recyclerViewRooms);
        tvDateFrom = view.findViewById(R.id.tv_date_from);
        tvDateTo = view.findViewById(R.id.tv_date_to);
        fabAdd = view.findViewById(R.id.fab_add);
        tabLayout = view.findViewById(R.id.tabLayout);
    }
}