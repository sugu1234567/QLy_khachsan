package com.example.giaodien.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.giaodien.Activities.Model.Room;
import com.example.giaodien.Activities.RoomBooking;
import com.example.giaodien.R;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    Context context;
    private ArrayList<Room> roomList;

    public RoomAdapter(ArrayList<Room> roomList) {
        this.roomList = roomList;
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
            if(room.getStatus().equals("Đã đặt"))
                Toast.makeText(context, "Phòng đã được đặt!", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(context, RoomBooking.class);
                context.startActivity(intent);
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
