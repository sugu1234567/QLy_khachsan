package com.example.giaodien.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.giaodien.Activities.AddRoom;
import com.example.giaodien.Activities.MainActivity;
import com.example.giaodien.Activities.UpdateBooking;
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
    private ActivityResultLauncher<Intent> launcher;
    private SearchView searchRoom;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;

    public Room_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Làm mới danh sách khi quay lại
                        fetchRooms();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);

        sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        Init(view);
        dateTimePicker();
        floatingActionButton();
        apiService = RetrofitClient.getClient().create(ApiService.class);
        recyclerViewRooms.setLayoutManager(new GridLayoutManager(getContext(), 3));

        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList, "", "", launcher);
        recyclerViewRooms.setAdapter(roomAdapter);

        fetchRooms();
        decentralization();
        filterRecyclerView();

        // Thực hiện hành động reload ở đây
        // this::reloadData: Sử dụng tham chiếu phương thức để tham chiếu đến phương thức reloadData()
        swipeRefreshLayout.setOnRefreshListener(this::reloadData);
        
        return  view;
    }

    private void reloadData() {
        fetchRooms();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void decentralization() {
        String position = sharedPreferences.getString("position", "");
        if (!position.equals("ADMIN")){
            fabAdd.setVisibility(View.GONE);
        }
    }

    private void floatingActionButton() {
        // Thiết lập FloatingActionButton (FAB) để thêm/sửa/xóa phòng
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddRoom.class);
            launcher.launch(intent);
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
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String dateFrom = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        tvDateFrom.setText("Ngày vào: "+dateFrom);
                        roomAdapter.setDateFrom(dateFrom); // Cập nhật ngày cho adapter
                    },
                    year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Không cho chọn ngày trước ngày hiện tại
            datePickerDialog.show();
        });

        // Thiết lập DatePickerDialog cho TextView chọn ngày kết thúc
        tvDateTo.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year12, monthOfYear, dayOfMonth) -> {
                        String dateTo = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year12;
                        tvDateTo.setText("Ngày ra: "+dateTo);
                        roomAdapter.setDateTo(dateTo); // Cập nhật ngày cho adapter
                    },
                    year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Không cho chọn ngày trước ngày hiện tại
            datePickerDialog.show();
        });
    }


    public void fetchRooms() {
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

    private void filterRecyclerView() {
        searchRoom.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                roomAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                roomAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void Init(View view) {
        recyclerViewRooms = view.findViewById(R.id.recyclerViewRooms);
        tvDateFrom = view.findViewById(R.id.tv_date_from);
        tvDateTo = view.findViewById(R.id.tv_date_to);
        fabAdd = view.findViewById(R.id.fab_add);
        searchRoom = view.findViewById(R.id.searchRoom);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }
}