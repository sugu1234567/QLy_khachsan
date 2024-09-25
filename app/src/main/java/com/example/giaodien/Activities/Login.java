package com.example.giaodien.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.Model.DataResponse;
import com.example.giaodien.Model.LoginResponse;
import com.example.giaodien.Model.Staff;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;
import com.example.giaodien.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private Button loginButton;
    private EditText edtUsername, edtPassword;

    private ApiService apiService;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Init();
        OnbackPressed();
        loginButton.setOnClickListener(view -> loginListener());

    }

    private void OnbackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setTitle("Thoát")
                                .setMessage("Bạn có muốn thoát không?")
                                        .setPositiveButton("Có", (dialogInterface, i) -> {
                                            finishAffinity();
                                        })
                        .setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss())
                        .create();
                dialog.show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void loginListener() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        Staff staff = new Staff(username, password);

        apiService.loginUser(staff).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    LoginResponse loginResponse =response.body();
                    if(loginResponse.isSuccess()){
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("staff_id", loginResponse.getStaff().getStaff_id());
                        editor.putString("fullname", loginResponse.getStaff().getFullname());
                        editor.putString("position", loginResponse.getStaff().getPosition());
                        editor.putString("username", loginResponse.getStaff().getUsername());
                        editor.putString("password", loginResponse.getStaff().getPassword());
                        editor.apply();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error: ", t.getMessage());
            }
        });
    }

    private void Init() {
        loginButton = findViewById(R.id.loginButton);
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
    }
}