package com.example.giaodien.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.giaodien.Activities.Model.LoginResponse;
import com.example.giaodien.Activities.Model.Staff;
import com.example.giaodien.Activities.Service.ApiService;
import com.example.giaodien.Activities.Service.RetrofitClient;
import com.example.giaodien.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private Button loginButton;
    private EditText edtUsername, edtPassword;

    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        Init();

        loginButton.setOnClickListener(view -> loginListener());

    }

    private void loginListener() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();


        apiService.loginUser(username, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    LoginResponse loginResponse =response.body();
                    if(loginResponse.isSuccess()){
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
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