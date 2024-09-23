package com.example.giaodien.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Model.Customers;
import com.example.giaodien.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomersViewHolder> {

    Context context;
    private ArrayList<Customers> customersList;

    public CustomersAdapter(ArrayList<Customers> customersList) {
        this.customersList = customersList;
    }

    @NonNull
    @Override
    public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.customers_item, parent, false);
        return new CustomersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersViewHolder holder, int position) {
        if (customersList.get(position) == null) return;

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycler_animation));

        Customers customers = customersList.get(position);

        holder.txtIdCustomer.setText("Mã khách hàng: " + customers.getCustomer_id());
        holder.txtFullname.setText("Tên khách hàng: " + customers.getFullname());
        holder.txtSex.setText("Giới tính: " + customers.getSex());
        holder.txtCCCD.setText("CCCD: " + customers.getCccd());
        holder.txtSDT.setText("Số điện thoại: " + customers.getPhone());

        if (customers.getSex().equals("Nam")) holder.imgSv.setImageResource(R.drawable.avatar_nam);
        else if (customers.getSex().equals("Nữ")) holder.imgSv.setImageResource(R.drawable.avatar_nu);

        // Thêm sự kiện khi nhấn vào item
        holder.cardView.setOnClickListener(v -> {
            // Hiển thị dialog cập nhật thông tin khách hàng
            showCustomerDialog(customers);
        });
    }

    private void showCustomerDialog(Customers customer) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.customer_info_dialog); // Layout chứa thông tin khách hàng

        // Liên kết các thành phần trong Dialog với mã Java
        EditText etCustomerName = dialog.findViewById(R.id.etCustomerName);
        EditText etCustomerPhone = dialog.findViewById(R.id.etCustomerPhone);
        EditText etCustomerId = dialog.findViewById(R.id.etCustomerId);

        RadioButton rbMale = dialog.findViewById(R.id.rbMale);
        RadioButton rbFemale = dialog.findViewById(R.id.rbFemale);
        Button btnUpdate = dialog.findViewById(R.id.btnConfirmBooking);
        Button btnCancel = dialog.findViewById(R.id.btnCancelBooking);

        // Điền thông tin khách hàng hiện tại
        etCustomerName.setText(customer.getFullname());
        etCustomerPhone.setText(customer.getPhone());
        etCustomerId.setText(customer.getCccd());

        if (customer.getSex().equals("Nam")) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }

        // Sự kiện cập nhật thông tin
        btnUpdate.setOnClickListener(v -> {
            // Cập nhật thông tin khách hàng sau khi chỉnh sửa
            customer.setFullname(etCustomerName.getText().toString());
            customer.setPhone(etCustomerPhone.getText().toString());
            customer.setCccd(etCustomerId.getText().toString());

            customer.setSex(rbMale.isChecked() ? "Nam" : "Nữ");

            // Cập nhật lại giao diện RecyclerView
            notifyDataSetChanged();
            dialog.dismiss();
        });

        // Sự kiện hủy bỏ
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Mở dialog với chiều ngang đầy đủ màn hình
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return customersList.size();
    }

    public static class CustomersViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdCustomer, txtFullname, txtSex, txtCCCD, txtSDT;
        CardView cardView;
        CircleImageView imgSv;

        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdCustomer = itemView.findViewById(R.id.txtIdCustomer);
            txtFullname = itemView.findViewById(R.id.txtFullname);
            txtSex = itemView.findViewById(R.id.txtSex);
            txtCCCD = itemView.findViewById(R.id.txtCCCD);
            txtSDT = itemView.findViewById(R.id.txtSDT);
            cardView = itemView.findViewById(R.id.cardView);
            imgSv = itemView.findViewById(R.id.imgSv);
        }
    }
}
