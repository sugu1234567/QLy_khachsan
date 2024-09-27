package com.example.giaodien.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.Response.BookingDetailsResponse;
import com.example.giaodien.Response.BookingRequest;
import com.example.giaodien.Model.Bookings;
import com.example.giaodien.Model.Customers;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.Model.Room;
import com.example.giaodien.R;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBooking extends AppCompatActivity {
    private Button btnBackBooking, btnUpdateBooking, btnDeleteBooking;
    private TextView tvCheckInTime, tvCheckOutTime, tvTotalPriceAmountUpdate, tvRoomNumberUpdate;
    private EditText etCustomerName, etCustomerPhone, etCustomerId;
    private RadioGroup radioGroup;
    private String roomNumber;
    private ApiService apiService;
    private double pricePerNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_booking);

        roomNumber = getIntent().getStringExtra("room_number");

        apiService = RetrofitClient.getClient().create(ApiService.class);

        Init();
        dateTimePicker();
        Cancel();
        OnBackPressed();
        fetchBookingDetails(roomNumber);
        updateBooking();
        cancelBookRoom(roomNumber);
        tvRoomNumberUpdate.setText("Phòng: "+roomNumber);
    }

    private void cancelBookRoom(String roomNumber) {
        btnDeleteBooking.setOnClickListener(view -> {
            apiService.cancelBookRoom(roomNumber).enqueue(new Callback<DataResponse>() {
                @Override
                public void onResponse(@NonNull Call<DataResponse> call, @NonNull Response<DataResponse> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        DataResponse bookRoomResponse = response.body();
                        if(bookRoomResponse.isSuccess()){
                            Toast.makeText(UpdateBooking.this, "Hủy đặt phòng thành công!", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(UpdateBooking.this, MainActivity.class);
//                            UpdateBooking.this.finish();
//                            startActivity(intent);
                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                        else{
                            Toast.makeText(UpdateBooking.this, "Hủy đặt phòng thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(UpdateBooking.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DataResponse> call, @NonNull Throwable t) {
                    Toast.makeText(UpdateBooking.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error: ", t.getMessage());
                }
            });
        });
    }

    private void updateBooking() {
        btnUpdateBooking.setOnClickListener(view -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String sex = "";
            String name = etCustomerName.getText().toString();
            String phone = etCustomerPhone.getText().toString();
            String cccd = etCustomerId.getText().toString();
            String fromDate = tvCheckInTime.getText().toString().replace("Ngày vào: ", "");
            String toDate = tvCheckOutTime.getText().toString().replace("Ngày ra: ", "");
            String totalPrice = tvTotalPriceAmountUpdate.getText().toString().replace("VNĐ", "");

            if(selectedId == R.id.rbMale) sex = "Nam";
            else if(selectedId == R.id.rbFemale) sex = "Nữ";

            if(name.equals("")) Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            else if(phone.equals("")) Toast.makeText(this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            else if(cccd.equals("")) Toast.makeText(this, "Vui lòng nhập CCCD/CMND!", Toast.LENGTH_SHORT).show();
            else {
                // Kiểm tra ngày
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date checkInDate = dateFormat.parse(fromDate);
                    Date checkOutDate = dateFormat.parse(toDate);

                    if (checkOutDate.before(checkInDate)) {
                        Toast.makeText(this, "Ngày ra không thể nhỏ hơn ngày vào!", Toast.LENGTH_SHORT).show();
                        return; // Ngừng thực hiện nếu ngày không hợp lệ
                    }
                    Customers customers = new Customers(name, sex, cccd, phone);
                    Bookings bookings = new Bookings(fromDate, toDate, totalPrice);
                    Room room = new Room(roomNumber);
                    fetchUpdateBookings(bookings, room, customers);
                }catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Lỗi định dạng ngày!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchUpdateBookings(Bookings bookings, Room room, Customers customers) {
        BookingRequest bookingRequest = new BookingRequest(bookings, room, customers);
        apiService.updateBookRoom(bookingRequest).enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    DataResponse bookRoomResponse = response.body();
                    if(bookRoomResponse.isSuccess()){
                        Toast.makeText(UpdateBooking.this, bookRoomResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(UpdateBooking.this, bookRoomResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(UpdateBooking.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Toast.makeText(UpdateBooking.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error: ", t.getMessage());
            }
        });
    }

    private void fetchBookingDetails(String roomNumber) {
        apiService.getBookingDetails(roomNumber).enqueue(new Callback<BookingDetailsResponse>() {
            @Override
            public void onResponse(Call<BookingDetailsResponse> call, Response<BookingDetailsResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    BookingDetailsResponse bookingDetailsResponse = response.body();
                    if(bookingDetailsResponse.isSuccess()){
                        Bookings bookings = response.body().getBooking();
                        Customers customers = response.body().getCustomer();
                        Room room = response.body().getRoom();

                        pricePerNight = Double.parseDouble(room.getPrice().replace(",",""));

                        if(customers.getFullname().equals("") || customers.getPhone().equals("") || customers.getCccd().equals("")){
                            etCustomerName.setText("NULL");
                            etCustomerPhone.setText("NULL");
                            etCustomerId.setText("NULL");
                        }
                        else{
                            etCustomerName.setText(customers.getFullname());
                            etCustomerPhone.setText(customers.getPhone());
                            etCustomerId.setText(customers.getCccd());
                        }
                        tvCheckInTime.setText("Ngày vào: "+bookings.getCheck_in_date());
                        tvCheckOutTime.setText("Ngày ra: "+bookings.getCheck_out_date());
                        tvTotalPriceAmountUpdate.setText(bookings.getPrice_booking()+" VNĐ");
                        // Thiết lập giới tính cho RadioButton
                        if (customers.getSex().equals("Nam")) {
                            radioGroup.check(R.id.rbMale); // Chọn RadioButton Nam
                        } else if (customers.getSex().equals("Nữ")) {
                            radioGroup.check(R.id.rbFemale); // Chọn RadioButton Nữ
                        }
                    }
                    else{
                        Toast.makeText(UpdateBooking.this, "Phòng chưa được đặt!", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(UpdateBooking.this, "Không tìm thấy thông tin.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingDetailsResponse> call, Throwable t) {
                Toast.makeText(UpdateBooking.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void Cancel() {
        btnBackBooking.setOnClickListener(view -> {
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

    private void dateTimePicker() {
        // Thiết lập DatePickerDialog cho TextView chọn ngày bắt đầu
        tvCheckInTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String dateFrom = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        tvCheckInTime.setText("Ngày vào: "+dateFrom);
                        validateDates();
                    },
                    year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Không cho chọn ngày trước ngày hiện tại
            datePickerDialog.show();
        });

        // Thiết lập DatePickerDialog cho TextView chọn ngày kết thúc
        tvCheckOutTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year12, monthOfYear, dayOfMonth) -> {
                        String dateTo = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year12;
                        tvCheckOutTime.setText("Ngày ra: "+dateTo);
                        validateDates();
                    },
                    year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Không cho chọn ngày trước ngày hiện tại
            datePickerDialog.show();
        });
    }

    private void validateDates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateFrom = tvCheckInTime.getText().toString().replace("Ngày vào: ", "");
        String dateTo = tvCheckOutTime.getText().toString().replace("Ngày ra: ", "");

        if (!dateFrom.isEmpty() && !dateTo.isEmpty()) {
            try {
                Date fromDate = dateFormat.parse(dateFrom);
                Date toDate = dateFormat.parse(dateTo);

                Calendar fromCalendar = Calendar.getInstance();
                fromCalendar.setTime(fromDate);
                Calendar toCalendar = Calendar.getInstance();
                toCalendar.setTime(toDate);

                if (toCalendar.before(fromCalendar)) {
                    Toast.makeText(this, "Ngày ra không thể nhỏ hơn ngày vào!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long differenceInMillis = toDate.getTime() - fromDate.getTime();
                long daysBetween = TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);

                // Tính tổng tiền
                double totalPrice = (daysBetween + 1) * pricePerNight; // Cộng thêm 1 nếu chọn cùng 1 ngày

                // Định dạng lại tổng tiền với dấu phẩy
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator(',');
                symbols.setDecimalSeparator('.');

                DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
                String formattedTotalPrice = decimalFormat.format(totalPrice);
                tvTotalPriceAmountUpdate.setText(formattedTotalPrice + " VNĐ");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    private void Init() {
        btnBackBooking = findViewById(R.id.btnBackBookingUpdate);
        btnUpdateBooking = findViewById(R.id.btnUpdateBooking);
        btnDeleteBooking = findViewById(R.id.btnDeleteBooking);
        tvCheckInTime = findViewById(R.id.tvCheckInTimeUpdate);
        tvCheckOutTime = findViewById(R.id.tvCheckOutTimeUpdate);
        etCustomerName = findViewById(R.id.etCustomerNameUpdate);
        etCustomerPhone = findViewById(R.id.etCustomerPhoneUpdate);
        etCustomerId = findViewById(R.id.etCustomerIdUpdate);
        radioGroup = findViewById(R.id.radioGroupUpdate);
        tvTotalPriceAmountUpdate = findViewById(R.id.tvTotalPriceAmountUpdate);
        tvRoomNumberUpdate = findViewById(R.id.tvRoomNumberUpdate);
    }
}