package com.example.giaodien.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.example.giaodien.Model.Room;
import com.example.giaodien.Activities.RoomBooking;
import com.example.giaodien.R;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    Context context;
    private ArrayList<Room> roomList;
    private String dateFrom;
    private String dateTo;

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
        notifyDataSetChanged();
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        notifyDataSetChanged();
    }

    public RoomAdapter(ArrayList<Room> roomList, String dateFrom, String dateTo) {
        this.roomList = roomList;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        if(roomList.get(position) == null) return;

        Room room = roomList.get(position);
        holder.roomNumber.setText(room.getRoom_number());
        holder.roomPrice.setText(room.getPrice());
        holder.roomType.setText(room.getRoom_type());
        holder.roomStatus.setText(room.getStatus());
        if(room.getStatus().equals("Đã đặt")){
            holder.roomItem.setBackground(ContextCompat.getDrawable(context,R.drawable.room_unavailable));
        }
        else if(room.getStatus().equals("Trống")){
            holder.roomItem.setBackground(ContextCompat.getDrawable(context,R.drawable.room_available));
        }
        holder.roomItem.setOnClickListener(view -> {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date currentDate = new Date();

            if(room.getStatus().equals("Đã đặt"))
                Toast.makeText(context, "Phòng đã được đặt!", Toast.LENGTH_SHORT).show();
            else {
                if(dateFrom.equals("") && dateTo.equals("")){
                    Toast.makeText(context, "Vui lòng chọn ngày đến và ngày đi", Toast.LENGTH_SHORT).show();
                }
                else {
                    Date fromDate = null, toDate = null;
                    try {
                         fromDate = dateFormat.parse(dateFrom);
                         toDate = dateFormat.parse(dateTo);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    
                    if(fromDate.before(currentDate) || toDate.before(currentDate) || toDate.before(fromDate)){
                        Toast.makeText(context, "Vui lòng chọn lại ngày!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        Intent intent = new Intent(context, RoomBooking.class);
                        intent.putExtra("room_number", room.getRoom_number());
                        intent.putExtra("date_from", dateFrom);
                        intent.putExtra("date_to", dateTo);
                        ((Activity) context).finish();
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomNumber, roomType, roomPrice, roomStatus;
        LinearLayout roomItem;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomNumber = itemView.findViewById(R.id.roomNumber);
            roomItem = itemView.findViewById(R.id.roomItem);
            roomType = itemView.findViewById(R.id.roomType);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            roomStatus = itemView.findViewById(R.id.roomStatus);
        }
    }
}
