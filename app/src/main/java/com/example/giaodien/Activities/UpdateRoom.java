package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.Model.Room;
import com.example.giaodien.R;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRoom extends AppCompatActivity {
    private EditText etRoomPrice, etRoomNumber;
    private Button btnUpdate, btnCancel;
    private Spinner spinnerRoomType;
    private String[] roomTypes = {"Standard", "Superior", "Premium", "Deluxe", "Suite"};
    private String roomType, roomPrice, roomNumber;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);

        // Lấy thông tin phòng từ Intent nếu cần thiết
        Intent intent = getIntent();
        roomNumber = intent.getStringExtra("room_number");
        roomPrice = intent.getStringExtra("room_price");
        roomType = intent.getStringExtra("room_type");
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Log.d("ROOMNUMBER", roomNumber);
        Init();
        CancelUpdateRoom();
        OnBackPressed();
        Spiner();
        SetTextView();
        updateRoom(roomNumber);
        etRoomNumber.setText(roomNumber);
    }

    private void SetTextView() {
        etRoomPrice.setText(roomPrice);
        Toast.makeText(this, roomType, Toast.LENGTH_SHORT).show();
        if(roomType.equals("Standard")) spinnerRoomType.setSelection(0);
        else if(roomType.equals("Superior")) spinnerRoomType.setSelection(1);
        else if(roomType.equals("Premium")) spinnerRoomType.setSelection(2);
        else if(roomType.equals("Deluxe")) spinnerRoomType.setSelection(3);
        else if(roomType.equals("Suite")) spinnerRoomType.setSelection(4);
    }

    private void updateRoom(String roomNumber) {

        btnUpdate.setOnClickListener(view -> {
            String selectedRoomType = (String) spinnerRoomType.getSelectedItem();
            String price = etRoomPrice.getText().toString();
            String room_Number = roomNumber;
            Room room = new Room(room_Number, selectedRoomType, price);
            apiService.updateRoom(room).enqueue(new Callback<DataResponse>() {
                @Override
                public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                    Log.d("Response", response.body().toString()); // In ra phản hồi
                    if(response.isSuccessful() && response.body()!=null){
                        DataResponse dataResponse = response.body();
                        if(dataResponse.isSuccess()){
                            Toast.makeText(UpdateRoom.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                        else{
                            Toast.makeText(UpdateRoom.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(UpdateRoom.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DataResponse> call, Throwable t) {
                    Toast.makeText(UpdateRoom.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error: ", t.getMessage());
                }
            });
        });
    }

    private void Spiner() {
        // Tạo ArrayAdapter với giao diện tùy chỉnh
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, roomTypes);
        adapter.setDropDownViewResource(R.layout.spinner_item);  // Cài đặt layout cho khi mở Spinner
        spinnerRoomType.setAdapter(adapter);

        spinnerRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void CancelUpdateRoom() {
        btnCancel.setOnClickListener(v -> {
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
        spinnerRoomType = findViewById(R.id.RoomType);
        etRoomPrice = findViewById(R.id.RoomPrice);
        etRoomNumber = findViewById(R.id.etRoomNumber);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
    }
}
