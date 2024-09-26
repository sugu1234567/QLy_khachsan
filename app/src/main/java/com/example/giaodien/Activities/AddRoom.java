package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.giaodien.Model.Room;
import com.example.giaodien.R;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRoom extends AppCompatActivity {
    private EditText etAddRoomNumber, etAddRoomPrice;
    private Spinner addRoomType;
    private Button btnAddRoom, btnCancelAddRoom;
    private String roomType;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_room);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        Init();
        Cancel();
        OnBackPressed();
        spiner();
        etAddRoomPriceTextChange();
        createNewRoom();
    }

    private void etAddRoomPriceTextChange() {
        etAddRoomPrice.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(current)) {
                    // Xóa TextWatcher để tránh vô hạn đệ quy
                    etAddRoomPrice.removeTextChangedListener(this);

                    String cleanString = charSequence.toString().replaceAll("[,]", ""); // Xóa dấu phẩy
                    if (!cleanString.isEmpty()) {
                        // Định dạng lại với dấu phẩy
                        String formatted = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(cleanString));
                        current = formatted;
                        etAddRoomPrice.setText(formatted);
                        etAddRoomPrice.setSelection(formatted.length()); // Di chuyển con trỏ về cuối
                    } else {
                        current = "";
                        etAddRoomPrice.setText("");
                    }

                    // Thêm lại TextWatcher
                    etAddRoomPrice.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void createNewRoom() {
        btnAddRoom.setOnClickListener(view -> {
            String selectedRoomType = roomType;
            String room_Number = etAddRoomNumber.getText().toString();
            String room_Price = etAddRoomPrice.getText().toString();
            if(room_Number.equals("") || room_Price.equals("")){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
            else{
                Room room = new Room(room_Number, selectedRoomType, room_Price);
                apiService.addNewRoom(room).enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            DataResponse dataResponse = response.body();
                            if(dataResponse.isSuccess()){
                                Toast.makeText(AddRoom.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent resultIntent = new Intent();
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(AddRoom.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(AddRoom.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Toast.makeText(AddRoom.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", t.getMessage());
                    }
                });
            }

        });
    }

    private void spiner() {
        addRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i ==0) roomType = "Standard";
                else if(i==1) roomType = "Superior";
                else if(i==2) roomType = "Premium";
                else if(i==3) roomType = "Deluxe";
                else if(i==4) roomType = "Suite";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void Cancel() {
        btnCancelAddRoom.setOnClickListener(view -> {
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
        etAddRoomNumber = findViewById(R.id.etAddRoomNumber);
        etAddRoomPrice = findViewById(R.id.etAddRoomPrice);
        addRoomType = findViewById(R.id.AddRoomType);
        btnAddRoom = findViewById(R.id.btnAddRoom);
        btnCancelAddRoom = findViewById(R.id.btnCancelAddRoom);
    }
}