package com.example.giaodien.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.giaodien.Activities.StaffActivity;
import com.example.giaodien.R;

public class Account_fragment extends Fragment {
    private TextView tvChangePassword; // Nút đổi mật khẩu
    private TextView tvEmployeeManagement;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Ánh xạ nút đổi mật khẩu
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
        tvEmployeeManagement = view.findViewById(R.id.tvEmployeeManagement);

        // Gọi hàm xử lý đổi mật khẩu khi người dùng nhấn vào nút
        setupChangePassword();
        setupFragmentStaff();
        return view;
    }

    // Hàm xử lý khi nhấn nút Quản Lý Nhân Viên
    private void setupFragmentStaff() {
        tvEmployeeManagement.setOnClickListener(view -> {
            // Dùng getContext() thay cho context trong Fragment
            Intent intent = new Intent(getContext(), StaffActivity.class);
            startActivity(intent);
        });
    }

    // Hàm thiết lập chức năng đổi mật khẩu
    private void setupChangePassword() {
        tvChangePassword.setOnClickListener(v -> showChangePasswordDialog());
    }

    // Hàm hiển thị hộp thoại đổi mật khẩu
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);

        // Ánh xạ các thành phần trong dialog
        EditText etOldPassword = dialogView.findViewById(R.id.etOldPassword);
        EditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = dialogView.findViewById(R.id.etConfirmPassword);
        Button btnUpdatePassword = dialogView.findViewById(R.id.btnUpdatePassword);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        AlertDialog alertDialog = builder.create();

        // Xử lý khi nhấn nút Cập nhật
        btnUpdatePassword.setOnClickListener(v -> {
            String oldPassword = etOldPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (newPassword.equals(confirmPassword)) {
                // TODO: Thêm logic kiểm tra mật khẩu cũ và cập nhật mật khẩu mới
                Toast.makeText(getContext(), "Mật khẩu đã được cập nhật", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý khi nhấn nút Hủy
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }
}