package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.Model.Staff;
import com.example.giaodien.R;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStaff extends AppCompatActivity {
    private Button btnAddStaff, btnCancelAddStaff;
    private EditText etStaffNameAdd, etAddStaffEmail, etAddStaffPhone, etAddUsernameStaff, etAddPasswordStaff;
    private RadioGroup radioGroupAddStaff;
    private Spinner PositionStaff;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_staff);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        Init();
        OnBackPressed();
        CancelAddStaff();
        CreateNewStaff();
        OnlyCharacters();
    }

    private void OnlyCharacters() {
        etStaffNameAdd.setFilters(new InputFilter[]{new InputFilter() {
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

    }

    // Phương thức kiểm tra email
    public boolean isValidEmail(String email) {
        // Kiểm tra nếu email không rỗng và khớp với pattern của email
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void CreateNewStaff() {
        btnAddStaff.setOnClickListener(view -> {
            String name = etStaffNameAdd.getText().toString();
            String email = etAddStaffEmail.getText().toString();
            String phone = etAddStaffPhone.getText().toString();
            String username = etAddUsernameStaff.getText().toString();
            String password = etAddPasswordStaff.getText().toString();
            String position = (String) PositionStaff.getSelectedItem();
            int selectedId = radioGroupAddStaff.getCheckedRadioButtonId();
            String sex = "";
            if(selectedId == R.id.rbMale) sex = "Nam";
            else if(selectedId == R.id.rbFemale) sex = "Nữ";
            if(name.equals("") || email.equals("") || phone.equals("") || username.equals("") || password.equals("")){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
            else if(!isValidEmail(email)){
                Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
            else{
                Staff staff = new Staff(name, sex, position, email, phone, username, password);
                apiService.addNewStaff(staff).enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            DataResponse dataResponse = response.body();
                            if(dataResponse.isSuccess()){
                                Toast.makeText(AddStaff.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent resultIntent = new Intent();
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(AddStaff.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(AddStaff.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Toast.makeText(AddStaff.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", t.getMessage());
                    }
                });
            }
        });
    }

    private void CancelAddStaff() {
        btnCancelAddStaff.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
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

    private void Init() {
        btnCancelAddStaff = findViewById(R.id.btnCancelAddStaff);
        btnAddStaff = findViewById(R.id.btnAddStaff);
        etStaffNameAdd = findViewById(R.id.etStaffNameAdd);
        etAddStaffEmail = findViewById(R.id.etAddStaffEmail);
        etAddStaffPhone = findViewById(R.id.etAddStaffPhone);
        etAddUsernameStaff = findViewById(R.id.etAddUsernameStaff);
        etAddPasswordStaff = findViewById(R.id.etAddPasswordStaff);
        radioGroupAddStaff = findViewById(R.id.radioGroupAddStaff);
        PositionStaff = findViewById(R.id.PositionStaff);
    }
}
