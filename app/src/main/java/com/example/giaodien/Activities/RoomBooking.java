package com.example.giaodien.Activities;

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

import com.example.giaodien.Model.BookRoomResponse;
import com.example.giaodien.Model.BookingRequest;
import com.example.giaodien.Model.Bookings;
import com.example.giaodien.Model.Customers;
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
    private String roomNumber, dateFrom, dateTo;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_booking);

        roomNumber = getIntent().getStringExtra("room_number");
        dateFrom = getIntent().getStringExtra("date_from");
        dateTo = getIntent().getStringExtra("date_to");
        apiService = RetrofitClient.getClient().create(ApiService.class);

        Init();
        SetTextView();
        Cancel();
        ConfirmBooking();
        OnBackPressed();
    }

    private void SetTextView() {
        tvRoomNumber.setText("Phòng: "+roomNumber);
        tvCheckInTime.setText("Check in: "+dateFrom);
        tvCheckOutTime.setText("Check out: "+dateTo);
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
            Bookings bookings = new Bookings(dateFrom, dateTo);
            Room room = new Room(roomNumber);
            Customers customers = new Customers(name, sex, cccd, phone);
            fetchBookings(bookings, room, customers);
        });
    }

    private void fetchBookings(Bookings bookings, Room room, Customers customers) {
        BookingRequest bookingRequest = new BookingRequest(bookings, room, customers);
        // Log thông tin bookingRequest
        Log.d("BookingRequest", new Gson().toJson(bookingRequest));

        apiService.bookRoom(bookingRequest).enqueue(new Callback<BookRoomResponse>() {
            @Override
            public void onResponse(Call<BookRoomResponse> call, Response<BookRoomResponse> response) {

                if(response.isSuccessful() && response.body()!=null){
                    BookRoomResponse bookRoomResponse = response.body();
                    if(bookRoomResponse.isSuccess()){
                        Toast.makeText(RoomBooking.this, bookRoomResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RoomBooking.this, MainActivity.class);
                        RoomBooking.this.finish();
                        startActivity(intent);
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
            public void onFailure(Call<BookRoomResponse> call, Throwable t) {
                Toast.makeText(RoomBooking.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error: ", t.getMessage());
            }
        });
    }

    private void Cancel() {
        btnCancelBooking.setOnClickListener(view -> {
            Intent intent = new Intent(RoomBooking.this, MainActivity.class);
            RoomBooking.this.finish();
            startActivity(intent);
        });
    }

    private void OnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(RoomBooking.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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