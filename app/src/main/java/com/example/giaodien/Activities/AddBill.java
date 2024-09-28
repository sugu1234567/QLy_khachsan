package com.example.giaodien.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.Model.Payments;
import com.example.giaodien.R;
import com.example.giaodien.Response.DataResponse;
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

public class AddBill extends AppCompatActivity {

    private TextView tvCurrentDate, tvSophong, tvTenKhachhang, tvTenNhanVien, tvNgayDatPhong, tvTotalPriceAmount;
    private Button btnPayBill, btnCancelPayBill;
    SharedPreferences sharedPreferences;
    private int bookingId, roomId;
    private String roomNumber, customerName, checkIn, price_room;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_bill);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);

        bookingId = getIntent().getIntExtra("booking_id", -1);
        roomId = getIntent().getIntExtra("room_id", -1);
        roomNumber = getIntent().getStringExtra("room_number");
        customerName = getIntent().getStringExtra("fullname");
        checkIn = getIntent().getStringExtra("check_in_date");
        price_room = getIntent().getStringExtra("price_room");

        Init();
        CancelPayBill();
        OnBackPressed();
        SetText();
        PayBill();

    }

    private void PayBill() {
        int staff_id = sharedPreferences.getInt("staff_id", -1);
        btnPayBill.setOnClickListener(view -> {
            String totalPrice = tvTotalPriceAmount.getText().toString().replace("VNĐ", "");
            Payments payments = new Payments(bookingId, staff_id, getCurrentDate(), totalPrice);
            apiService.payBill(payments).enqueue(new Callback<DataResponse>() {
                @Override
                public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        DataResponse dataResponse = response.body();
                        if(dataResponse.isSuccess()){
                            Toast.makeText(AddBill.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("reload", true); // Thêm dữ liệu để chỉ định reload các fragment khác
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                        else{
                            Toast.makeText(AddBill.this, dataResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(AddBill.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DataResponse> call, Throwable t) {
                    Toast.makeText(AddBill.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error: ", t.getMessage());
                }
            });
        });
    }

    private void SetText() {
        String staff_name = sharedPreferences.getString("fullname", "");
        // Lấy thời gian hiện tại và định dạng
        String currentDate = getCurrentDate();

        double pricePerNight = Double.parseDouble(price_room.replace(",",""));

        if(staff_name.equals("")) tvTenNhanVien.setText("NULL");
        else if(roomNumber.equals("")) tvSophong.setText("NULL");
        else if(customerName.equals("")) tvTenKhachhang.setText("NULL");
        else if(checkIn.equals("")) tvNgayDatPhong.setText("NULL");
        else{
            tvSophong.setText("Số phòng: "+ roomNumber);
            tvTenKhachhang.setText("Tên khách hàng: "+customerName);
            tvTenNhanVien.setText("Tên nhân viên: "+staff_name);
            tvNgayDatPhong.setText("Ngày đặt phòng: "+checkIn);
            tvCurrentDate.setText("Ngày thanh toán: " + currentDate);
        }

        // Kiểm tra ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date current = dateFormat.parse(currentDate);
            Date checkInDate = dateFormat.parse(checkIn);

            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(current);
            Calendar checkInDateCalendar = Calendar.getInstance();
            checkInDateCalendar.setTime(checkInDate);

            long differenceInMillis = current.getTime() - checkInDate.getTime();
            long daysBetween = TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);

            // Tính tổng tiền
            double totalPrice = (daysBetween + 1) * pricePerNight; // Cộng thêm 1 nếu chọn cùng 1 ngày

            // Định dạng lại tổng tiền với dấu phẩy
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
            String formattedTotalPrice = decimalFormat.format(totalPrice);

            tvTotalPriceAmount.setText(formattedTotalPrice+" VNĐ");

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private void OnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void CancelPayBill() {
        btnCancelPayBill.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }


    // Phương thức lấy thời gian thực
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    private void Init() {
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        tvSophong = findViewById(R.id.tvSophong);
        tvTenKhachhang = findViewById(R.id.tvTenKhachhang);
        tvTenNhanVien = findViewById(R.id.tvTenNhanVien);
        tvNgayDatPhong = findViewById(R.id.tvNgayDatPhong);
        tvTotalPriceAmount = findViewById(R.id.tvTotalPriceAmount);
        btnPayBill = findViewById(R.id.btnPayBill);
        btnCancelPayBill = findViewById(R.id.btnCancelPayBill);
        
    }
}
