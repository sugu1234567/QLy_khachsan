package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;
import com.example.giaodien.fragment.Customers_fragment;

public class AddCustomer extends AppCompatActivity {
    private EditText etCustomerNameAdd, etCustomerPhoneAdd, etCustomerIdAdd;
    private RadioGroup radioGroupAdd;
    private Button btnAddCustomer, btnCancelAddCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_customer);

        Init();
        Cancel();
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