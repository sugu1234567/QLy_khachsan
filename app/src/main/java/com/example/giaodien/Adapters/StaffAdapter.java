package com.example.giaodien.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
    public StaffAdapter.StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffAdapter.StaffViewHolder holder, int position) {
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

        // Thêm sự kiện khi nhấn vào item
        holder.cardView.setOnClickListener(v -> {
            // Hiển thị dialog cập nhật thông tin nhan vien
            showStaffDialog(staffs);
        });

    }
    private void showStaffDialog(Staff staff) {
        // Tạo và thiết lập Dialog
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.staff_info_dialog); // Gắn layout chứa thông tin nhân viên

        // Liên kết các thành phần trong Dialog với mã Java
        EditText etStaffName = dialog.findViewById(R.id.etStaffName);
        EditText etStaffEmail = dialog.findViewById(R.id.etStaffEmail);
        EditText etStaffPhone = dialog.findViewById(R.id.etStaffPhone);
        EditText etUsername = dialog.findViewById(R.id.etUsername);
        EditText etPassword = dialog.findViewById(R.id.etPassword);
        RadioButton rbMale = dialog.findViewById(R.id.rbMale);
        RadioButton rbFemale = dialog.findViewById(R.id.rbFemale);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        // Điền thông tin nhân viên hiện tại vào các trường nhập liệu
        etStaffName.setText(staff.getFullname());
        etStaffEmail.setText(staff.getEmail());
        etStaffPhone.setText(staff.getPhone());
        etUsername.setText(staff.getUsername());
        etPassword.setText(staff.getPassword());

        // Thiết lập radio button cho giới tính
        if ("Nam".equals(staff.getSex())) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }

        // Xử lý sự kiện cập nhật thông tin nhân viên
        btnUpdate.setOnClickListener(v -> {
            // Cập nhật thông tin nhân viên sau khi chỉnh sửa
            staff.setFullname(etStaffName.getText().toString());
            staff.setEmail(etStaffEmail.getText().toString());
            staff.setPhone(etStaffPhone.getText().toString());
            staff.setUsername(etUsername.getText().toString());
            staff.setPassword(etPassword.getText().toString());

            // Cập nhật giới tính
            staff.setSex(rbMale.isChecked() ? "Nam" : "Nữ");

            // Cập nhật lại giao diện RecyclerView
            notifyDataSetChanged();  // Phương thức này nằm trong Adapter của RecyclerView
            dialog.dismiss();  // Đóng dialog sau khi cập nhật
        });

        // Sự kiện hủy bỏ dialog
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Mở dialog với chiều ngang đầy đủ màn hình
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
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
            cardView = itemView.findViewById(R.id.cardViewStaff);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
