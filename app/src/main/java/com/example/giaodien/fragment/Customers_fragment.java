package com.example.giaodien.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Activities.AddCustomer;
import com.example.giaodien.Adapters.CustomersAdapter;
import com.example.giaodien.Model.Customers;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.R;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Customers_fragment extends Fragment {

    private RecyclerView recyclerViewCustomers;
    private CustomersAdapter customersAdapter;
    private ArrayList<Customers> customersList;
    private ApiService apiService;
    private FloatingActionButton fb_add;
    private SearchView search_view;
    private ActivityResultLauncher<Intent> launcher;


    public Customers_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Làm mới danh sách khi quay lại
                        fetchCustomers();
                    }
                }
        );

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        Init(view);
        floatingActionButton();
        apiService = RetrofitClient.getClient().create(ApiService.class);

        customersList = new ArrayList<>();
        recyclerViewCustomers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        customersAdapter = new CustomersAdapter(customersList);
        recyclerViewCustomers.setAdapter(customersAdapter);

        fetchCustomers();
        setupSwipeToDelete();
        filterRecyclerView();
        return view;
    }

    private void filterRecyclerView() {
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                customersAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customersAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void floatingActionButton() {
        fb_add.setOnClickListener(view -> {

            Intent intent = new Intent(getContext(), AddCustomer.class);
            launcher.launch(intent);
        });
    }

    private void fetchCustomers() {
        apiService.getCustomers().enqueue(new Callback<List<Customers>>() {
            @Override
            public void onResponse(Call<List<Customers>> call, Response<List<Customers>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    customersList.clear();
                    customersList.addAll(response.body());
                    customersAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e("CustomerFragment", "Response failed");
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Customers>> call, Throwable t) {
                Log.e("CustomerFragment", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private Drawable icon;
            private final ColorDrawable background = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.red)); // Đặt màu nền đỏ khi vuốt

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // Không xử lý sự kiện di chuyển
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Lấy vị trí của khách hàng trong danh sách
                int position = viewHolder.getAdapterPosition();

                // Hiển thị hộp thoại xác nhận
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc muốn xóa khách hàng này?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Nếu người dùng chọn "Có", thực hiện xóa khách hàng
                            int customer_id = customersList.get(position).getCustomer_id();
                            Log.d("CUSTOMER_IDDDDDDDDD", customer_id+"");
                            apiService.deleteCustomer(customer_id).enqueue(new Callback<DataResponse>() {
                                @Override
                                public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                                    if(response.isSuccessful() && response.body()!=null){
                                        DataResponse dataResponse = response.body();
                                        if(dataResponse.isSuccess()){
                                            Toast.makeText(getContext(), dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            customersList.remove(position);
                                            customersAdapter.notifyItemRemoved(position);
                                        }
                                        else{
                                            Toast.makeText(getContext(), dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<DataResponse> call, Throwable t) {
                                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("Error: ", t.getMessage());
                                }
                            });
                        })
                        .setNegativeButton("Không", (dialog, which) -> {
                            // Nếu người dùng chọn "Không", khôi phục lại trạng thái ban đầu của item
                            customersAdapter.notifyItemChanged(position);
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
                    icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_delete); // Icon xóa

                    // Đặt màu nền đỏ
                    background.setBounds(itemView.getRight() + (int) dX - backgroundCornerOffset, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom());
                    background.draw(c);

                    // Đặt icon delete ở giữa chiều cao của item
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
        itemTouchHelper.attachToRecyclerView(recyclerViewCustomers); // Gắn sự kiện vuốt vào RecyclerView
    }


    private void Init(View view) {
        recyclerViewCustomers = view.findViewById(R.id.recyclerViewCustomers);
        fb_add = view.findViewById(R.id.fb_add);
        search_view = view.findViewById(R.id.search_view);
    }
}
