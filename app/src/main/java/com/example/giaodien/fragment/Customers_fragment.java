package com.example.giaodien.fragment;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Adapters.CustomersAdapter;
import com.example.giaodien.Model.Customers;
import com.example.giaodien.R;

import java.util.ArrayList;

public class Customers_fragment extends Fragment {

    private RecyclerView recyclerViewCustomers;
    private CustomersAdapter customersAdapter;
    private ArrayList<Customers> customersList;


    public Customers_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        Init(view);
        customersList = new ArrayList<>();

        customersList.add(new Customers("Nguyen Van A", "Nam", "1231321", "1231321"));
        customersList.add(new Customers("Nguyen Van B", "Nam", "1231321", "1231321"));
        customersList.add(new Customers("Nguyen Van C", "Nữ", "1231321", "1231321"));
        customersList.add(new Customers("Nguyen Van D", "Nữ", "1231321", "1231321"));
        customersList.add(new Customers("Nguyen Van E", "Nam", "1231321", "1231321"));
        // Thêm nhiều khách hàng khác

        recyclerViewCustomers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        customersAdapter = new CustomersAdapter(customersList, getContext());
        recyclerViewCustomers.setAdapter(customersAdapter);


        setupSwipeToDelete();

        return view;
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
                            customersList.remove(position);
                            customersAdapter.notifyItemRemoved(position);
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
    }
}
