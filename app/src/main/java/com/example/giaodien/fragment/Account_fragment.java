package com.example.giaodien.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.giaodien.Activities.Login;
import com.example.giaodien.Activities.StaffActivity;
import com.example.giaodien.Model.DataResponse;
import com.example.giaodien.Model.Staff;
import com.example.giaodien.R;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account_fragment extends Fragment {
    private TextView tvWelcomeMessage; // Nút đổi mật khẩu
    private LinearLayout changePassword, logout, staffManager, statistics, adminFunctions;
    private ApiService apiService;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        Init(view);

        setupChangePassword();
        setupFragmentStaff();
        logOut();
        setWelcomeText();
        decentralization();
        return view;
    }

    private void decentralization() {
        String position = sharedPreferences.getString("position", "");
        if (!position.equals("ADMIN")){
            adminFunctions.setVisibility(View.GONE);
        }
    }

    private void setWelcomeText() {
        String welcome = sharedPreferences.getString("fullname", "");
        tvWelcomeMessage.setText("Chào mừng "+welcome+"!");
    }

    private void logOut() {
        logout.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có muốn đăng xuất?")
                    .setPositiveButton("Có", (dialogInterface, i) -> {
                        Intent intent = new Intent(getContext(), Login.class);
                        ((Activity)getContext()).finish();
                        startActivity(intent);
                    })
                    .setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            dialog.show();
        });
    }


    // Hàm xử lý khi nhấn nút Quản Lý Nhân Viên
    private void setupFragmentStaff() {
        staffManager.setOnClickListener(view -> {
            // Dùng getContext() thay cho context trong Fragment
            Intent intent = new Intent(getContext(), StaffActivity.class);
            startActivity(intent);
        });
    }

    // Hàm thiết lập chức năng đổi mật khẩu
    private void setupChangePassword() {
        changePassword.setOnClickListener(v -> showChangePasswordDialog());
    }

    // Hàm hiển thị hộp thoại đổi mật khẩu
    private void showChangePasswordDialog() {
        String currentPassword = sharedPreferences.getString("password", "");

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

            if(oldPassword.equals(currentPassword)){
                if(newPassword.equals(confirmPassword)){
                    updatePassword(newPassword, alertDialog);
                }
                else{
                    Toast.makeText(getContext(), "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getContext(), "Mật khẩu hiện tại không chính xác!", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý khi nhấn nút Hủy
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }

    private void updatePassword(String newPassword, AlertDialog alertDialog) {
        int staff_id = sharedPreferences.getInt("staff_id", -1);
        Staff staff = new Staff(staff_id, newPassword);
        if(staff_id != -1) {
            apiService.updatePassword(staff).enqueue(new Callback<DataResponse>() {
                @Override
                public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        DataResponse dataResponse = response.body();
                        if(response.isSuccessful()){
                            Toast.makeText(getContext(), dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("password", newPassword);
                            editor.apply();
                            alertDialog.dismiss();
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

                }
            });
        }
        else{
            Toast.makeText(getContext(), "Thay đổi mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    private void Init(View view) {
        tvWelcomeMessage = view.findViewById(R.id.tvWelcomeMessage);
        changePassword = view.findViewById(R.id.changePassword);
        logout = view.findViewById(R.id.logout);
        staffManager = view.findViewById(R.id.staffManager);
        statistics = view.findViewById(R.id.Statistics);
        adminFunctions = view.findViewById(R.id.adminFunctions);
    }
}