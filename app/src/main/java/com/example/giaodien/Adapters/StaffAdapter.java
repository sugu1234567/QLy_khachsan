package com.example.giaodien.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Model.Staff;
import com.example.giaodien.R;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> implements Filterable {


    Context context;
    private ArrayList<Staff> staffList, staffListOld;
    private ApiService apiService;

    public StaffAdapter(ArrayList<Staff> staffList) {
        this.staffList = staffList;
        this.staffListOld = staffList; // Lưu bản sao danh sách đầy đủ
    }

    @NonNull
    @Override
    public StaffAdapter.StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.staff_item, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffAdapter.StaffViewHolder holder, int position) {
        if (staffList.get(position) == null) return;

        Staff staffs = staffList.get(position);
        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycler_animation_staff_activity));
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
            showStaffDialog(staffs, position);
        });

    }
    private void showStaffDialog(Staff staff, int position) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
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
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroupStaff);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdateStaff);
        Button btnCancel = dialog.findViewById(R.id.btnCancelStaff);

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
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String sex = "";
            String fullname = etStaffName.getText().toString();
            String email = etStaffEmail.getText().toString();
            String phone = etStaffPhone.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if(selectedId == R.id.rbMale) sex = "Nam";
            else if(selectedId == R.id.rbFemale) sex = "Nữ";

            if(fullname.equals("")) Toast.makeText(context, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            else if(phone.equals("")) Toast.makeText(context, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            else if(email.equals("")) Toast.makeText(context, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            else if(password.equals("")) Toast.makeText(context, "Vui lòng nhập Password!", Toast.LENGTH_SHORT).show();
            else{
                int staffId = staff.getStaff_id();
                Staff staffs = new Staff(staffId, fullname, sex, email, phone, username, password);
                updateDataStaff(staffs, apiService, dialog, position);
            }
        });

        // Sự kiện hủy bỏ dialog
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Mở dialog với chiều ngang đầy đủ màn hình
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void updateDataStaff(Staff staffs, ApiService apiService, Dialog dialog, int position) {
        apiService.updateDataStaff(staffs).enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    DataResponse dataResponse = response.body();
                    if(dataResponse.isSuccess()){
                        Toast.makeText(context, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        // Cập nhật lại giao diện RecyclerView
                        staffList.set(position, staffs);
                        notifyItemChanged(position);
                        //notifyDataSetChanged();

                    }
                    else{
                        Toast.makeText(context, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error: ", t.getMessage());
            }
        });
    }


    @Override
    public int getItemCount() {
        return staffList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                if(query.isEmpty()){
                    staffList = staffListOld;
                }
                else{
                    ArrayList<Staff> arrayList = new ArrayList<>();
                    for(Staff staff : staffListOld){
                        if(staff.getFullname().toLowerCase().contains(query.toLowerCase())
                                || String.valueOf(staff.getStaff_id()).contains(query.toLowerCase())
                        ){
                            arrayList.add(staff);
                        }
                    }
                    staffList = arrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = staffList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                staffList = (ArrayList<Staff>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
