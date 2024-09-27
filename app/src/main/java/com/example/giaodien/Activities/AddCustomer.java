package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.Model.Customers;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.R;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomer extends AppCompatActivity {
    private EditText etCustomerNameAdd, etCustomerPhoneAdd, etCustomerIdAdd;
    private RadioGroup radioGroupAdd;
    private Button btnAddCustomer, btnCancelAddCustomer;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_customer);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Init();
        Cancel();
        OnBackPressed();
        addNewCustomer();
    }

    private void addNewCustomer() {
        btnAddCustomer.setOnClickListener(view -> {
            int selectedId = radioGroupAdd.getCheckedRadioButtonId();
            String sex = "";
            String fullname = etCustomerNameAdd.getText().toString();
            String phone = etCustomerPhoneAdd.getText().toString();
            String cccd = etCustomerIdAdd.getText().toString();

            if(selectedId == R.id.rbMale) sex = "Nam";
            else if(selectedId == R.id.rbFemale) sex = "Nữ";

            if(fullname.equals("")) Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            else if(phone.equals("")) Toast.makeText(this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            else if(cccd.equals("")) Toast.makeText(this, "Vui lòng nhập CCCD/CMND!", Toast.LENGTH_SHORT).show();
            else{
                Customers customers = new Customers(fullname, sex, cccd, phone);
                addDataCustomer(customers);
            }
        });
    }

    private void OnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                Intent intent = new Intent(RoomBooking .this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void addDataCustomer(Customers customers) {
        apiService.addNewCustomer(customers).enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    DataResponse dataResponse = response.body();
                    if(dataResponse.isSuccess()){
                        Toast.makeText(AddCustomer.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(AddCustomer.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AddCustomer.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Toast.makeText(AddCustomer.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error: ", t.getMessage());
            }
        });
    }

    private void Cancel() {
        btnCancelAddCustomer.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    private void Init() {
        etCustomerNameAdd = findViewById(R.id.etCustomerNameAdd);
        etCustomerPhoneAdd = findViewById(R.id.etCustomerPhoneAdd);
        etCustomerIdAdd = findViewById(R.id.etCustomerIdAdd);
        radioGroupAdd = findViewById(R.id.radioGroupAdd);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);
        btnCancelAddCustomer = findViewById(R.id.btnCancelAddCustomer);
    }
}