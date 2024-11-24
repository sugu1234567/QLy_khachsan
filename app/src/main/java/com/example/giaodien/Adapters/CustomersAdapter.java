package com.example.giaodien.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Model.Customers;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.R;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomersViewHolder> implements Filterable {

    Context context;
    private ArrayList<Customers> customersList, customersListOld;
    private ApiService apiService;

    public CustomersAdapter(ArrayList<Customers> customersList) {
        this.customersList = customersList;
        this.customersListOld = customersList; // Lưu bản sao danh sách đầy đủ
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
            showCustomerDialog(customers, position);
        });
    }

    private void showCustomerDialog(Customers customer, int position) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.customer_info_dialog); // Layout chứa thông tin khách hàng

        // Liên kết các thành phần trong Dialog với mã Java
        EditText etCustomerName = dialog.findViewById(R.id.etUpdateCustomerName);
        EditText etCustomerPhone = dialog.findViewById(R.id.etUpdateCustomerPhone);
        EditText etCustomerId = dialog.findViewById(R.id.etUpdateCustomerId);

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroupUpdateSex);
        RadioButton rbMale = dialog.findViewById(R.id.rbMale);
        RadioButton rbFemale = dialog.findViewById(R.id.rbFemale);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdateDataCustomer);
        Button btnCancel = dialog.findViewById(R.id.btnCancelUpdateDataCustomer);


        // Điền thông tin khách hàng hiện tại
        etCustomerName.setText(customer.getFullname());
        etCustomerPhone.setText(customer.getPhone());
        etCustomerId.setText(customer.getCccd());

        etCustomerName.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Kiểm tra từng ký tự có phải là chữ không
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});

        if (customer.getSex().equals("Nam")) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }

        // Sự kiện cập nhật thông tin
        btnUpdate.setOnClickListener(v -> {

            int selectedId = radioGroup.getCheckedRadioButtonId();
            String sex = "";
            String fullname = etCustomerName.getText().toString();
            String phone = etCustomerPhone.getText().toString();
            String cccd = etCustomerId.getText().toString();

            if(selectedId == R.id.rbMale) sex = "Nam";
            else if(selectedId == R.id.rbFemale) sex = "Nữ";

            if(fullname.equals("")) Toast.makeText(context, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            else if(phone.equals("")) Toast.makeText(context, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            else if(cccd.equals("")) Toast.makeText(context, "Vui lòng nhập CCCD/CMND!", Toast.LENGTH_SHORT).show();
            else{
                int customerId = customer.getCustomer_id();
                Customers customers = new Customers(customerId, fullname, sex, cccd, phone);
                updateDataCustomer(customers, apiService, dialog, position);
            }

        });

        // Sự kiện hủy bỏ
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Mở dialog với chiều ngang đầy đủ màn hình
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void updateDataCustomer(Customers customers, ApiService apiService, Dialog dialog, int position) {
        apiService.updateDataCustomer(customers).enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    DataResponse dataResponse = response.body();
                    if(dataResponse.isSuccess()){
                        Toast.makeText(context, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        // Cập nhật lại giao diện RecyclerView
                        customersList.set(position, customers);
                        notifyItemChanged(position);
                        //notifyDataSetChanged();

                    }
                    else{
                        Toast.makeText(context, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
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
        return customersList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                if(query.isEmpty()){
                    customersList = customersListOld;
                }
                else{
                    ArrayList<Customers> arrayList = new ArrayList<>();
                    for(Customers customers : customersListOld){
                        if(customers.getFullname().toLowerCase().contains(query.toLowerCase())
                            || String.valueOf(customers.getCustomer_id()).contains(query.toLowerCase())
                        || customers.getCccd().toLowerCase().contains(query.toLowerCase())
                        ){
                            arrayList.add(customers);
                        }
                    }
                    customersList = arrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = customersList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                customersList = (ArrayList<Customers>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
