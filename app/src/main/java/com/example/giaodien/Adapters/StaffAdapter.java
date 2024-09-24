package com.example.giaodien.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Model.Staff;
import com.example.giaodien.R;

import java.util.ArrayList;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {


    Context context;
    private ArrayList<Staff> staffList;

    public StaffAdapter(ArrayList<Staff> staffList, Context context) {
        this.staffList = staffList;
        this.context = context;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        if (staffList.get(position) == null) return;

        Staff staffs = staffList.get(position);

        holder.txtIdStaff.setText("Mã nhân viên: " + staffs.getStaff_id());
        holder.txtFullname.setText("Tên nhân viên: " + staffs.getFullname());
        holder.txtSex.setText("Giới tính: " + staffs.getSex());
        holder.txtEmail.setText("Email: " + staffs.getEmail());
        holder.txtSDT.setText("Số điện thoại: " + staffs.getPhone());
        holder.txtUsername.setText("Tài Khoản: " + staffs.getUsername());
        holder.txtPassword.setText("Mật khẩu: " + staffs.getPassword());

        if (staffs.getSex().equals("Nam")) holder.imgAvatar.setImageResource(R.drawable.avatar_nam);
        else if (staffs.getSex().equals("Nữ")) holder.imgAvatar.setImageResource(R.drawable.avatar_nu);

    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdStaff, txtFullname, txtSex, txtEmail, txtSDT, txtUsername, txtPassword;
        CardView cardView;
        ImageView imgAvatar;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdStaff = itemView.findViewById(R.id.txtIdStaff);
            txtFullname = itemView.findViewById(R.id.txtFullname);
            txtSex = itemView.findViewById(R.id.txtSex);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtSDT = itemView.findViewById(R.id.txtSDT);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtPassword = itemView.findViewById(R.id.txtPassword);
            cardView = itemView.findViewById(R.id.cardView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
