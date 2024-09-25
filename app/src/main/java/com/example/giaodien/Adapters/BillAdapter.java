package com.example.giaodien.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Model.Bookings;
import com.example.giaodien.R;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder>{

    private Context context;
    private ArrayList<Bookings> billList;

    public BillAdapter(Context context, ArrayList<Bookings> billList) {
        this.context = context;
        this.billList = billList;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        if (billList.get(position) == null) return;

        Bookings bookings = billList.get(position);

        holder.tvRoomNumber.setText(bookings.getRoomNumber());
        holder.tvCustomerName.setText(bookings.getCustomerName());
        holder.tvCheckInOut.setText("Thời gian vào: " + bookings.getCheck_in_date());
        holder.tvCheckOut.setText("Thời gian ra: " + bookings.getCheck_out_date());
        holder.tvStatus.setText("Trạng thái: " + bookings.getStatus());
        holder.tvTotalAmount.setText("Tổng tiền: " + bookings.getPrice_booking());

        // Đổi màu trạng thái dựa trên giá trị của "status"
        if (bookings.getStatus().equals("Chưa thanh toán")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        } else {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvCustomerName, tvCheckInOut, tvCheckOut, tvStatus, tvTotalAmount;
        CardView cardView;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCheckInOut = itemView.findViewById(R.id.tvCheckInOut);
            tvCheckOut = itemView.findViewById(R.id.tvCheckOut);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            cardView = itemView.findViewById(R.id.cardViewBill);
        }
    }
}
