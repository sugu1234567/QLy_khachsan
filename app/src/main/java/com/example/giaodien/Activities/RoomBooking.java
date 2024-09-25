package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.Response.BookingRequest;
import com.example.giaodien.Model.Bookings;
import com.example.giaodien.Model.Customers;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.Model.Room;
import com.example.giaodien.R;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomBooking extends AppCompatActivity {
    private Button btnCancelBooking, btnConfirmBooking;
    private EditText etCustomerName, etCustomerPhone, etCustomerId;
    private RadioButton rbMale, rbFemale;
    private RadioGroup radioGroup;
    private TextView tvRoomNumber, tvCheckInTime, tvCheckOutTime, tvTotalPriceAmount;
    private String roomNumber, dateFrom, dateTo, totalPrice;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_booking);

        roomNumber = getIntent().getStringExtra("room_number");
        dateFrom = getIntent().getStringExtra("date_from");
        dateTo = getIntent().getStringExtra("date_to");
        totalPrice = getIntent().getStringExtra("total_price");

        apiService = RetrofitClient.getClient().create(ApiService.class);

        Init();
        SetTextView();
        Cancel();
        ConfirmBooking();
        OnBackPressed();
    }

    private void SetTextView() {
        tvRoomNumber.setText("Phòng: "+roomNumber);
        tvCheckInTime.setText("Ngày vào: "+dateFrom);
        tvCheckOutTime.setText("Ngày ra: "+dateTo);
        tvTotalPriceAmount.setText(totalPrice+" VNĐ");
    }

    private void ConfirmBooking() {
        btnConfirmBooking.setOnClickListener(view -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String sex = "";
            String name = etCustomerName.getText().toString();
            String phone = etCustomerPhone.getText().toString();
            String cccd = etCustomerId.getText().toString();

            if(selectedId == R.id.rbMale) sex = "Nam";
            else if(selectedId == R.id.rbFemale) sex = "Nữ";

            if(name.equals("")) Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            else if(phone.equals("")) Toast.makeText(this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            else if(cccd.equals("")) Toast.makeText(this, "Vui lòng nhập CCCD/CMND!", Toast.LENGTH_SHORT).show();
            else {
                Customers customers = new Customers(name, sex, cccd, phone);
                Bookings bookings = new Bookings(dateFrom, dateTo, totalPrice);
                Room room = new Room(roomNumber);
                fetchBookings(bookings, room, customers);
            }
        });
    }

    private void fetchBookings(Bookings bookings, Room room, Customers customers) {
        BookingRequest bookingRequest = new BookingRequest(bookings, room, customers);
        // Log thông tin bookingRequest
        Log.d("BookingRequest", new Gson().toJson(bookingRequest));

        apiService.bookRoom(bookingRequest).enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {

                if(response.isSuccessful() && response.body()!=null){
                    DataResponse bookRoomResponse = response.body();
                    if(bookRoomResponse.isSuccess()){
                        Toast.makeText(RoomBooking.this, bookRoomResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RoomBooking.this, MainActivity.class);
//                        RoomBooking.this.finish();
//                        startActivity(intent);
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(RoomBooking.this, bookRoomResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RoomBooking.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Toast.makeText(RoomBooking.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error: ", t.getMessage());
            }
        });
    }

    private void Cancel() {
        btnCancelBooking.setOnClickListener(view -> {
//            Intent intent = new Intent(RoomBooking.this, MainActivity.class);
//            RoomBooking.this.finish();
//            startActivity(intent);
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
        btnCancelBooking = findViewById(R.id.btnCancelBooking);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        etCustomerId = findViewById(R.id.etCustomerId);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        radioGroup = findViewById(R.id.radioGroup);
        tvRoomNumber = findViewById(R.id.tvRoomNumber);
        tvCheckInTime = findViewById(R.id.tvCheckInTime);
        tvCheckOutTime = findViewById(R.id.tvCheckOutTime);
        tvTotalPriceAmount = findViewById(R.id.tvTotalPriceAmount);
    }
}