package com.example.giaodien.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;
import com.example.giaodien.Response.StatisticalReportResponse;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaoCaoThongKe extends AppCompatActivity {

    private BarChart barChart, barChart2, barChart3;
    private TextView startDateTextView, endDateTextView, tvTotalPriceAmount;
    private String fromDate = "", toDate = "";
    private ApiService apiService;
    private double totalRevenue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bao_cao_thong_ke);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Nút quay lại
        ImageView backButton = findViewById(R.id.back_arrow);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Các TextView hiển thị ngày
        LinearLayout startDateLayout = findViewById(R.id.start_date_layout);
        LinearLayout endDateLayout = findViewById(R.id.end_date_layout);
        startDateTextView = findViewById(R.id.tv_start_date);
        endDateTextView = findViewById(R.id.tv_end_date);
        tvTotalPriceAmount = findViewById(R.id.tvTotalReportBill);

        showDatePicker();

        // Thiết lập biểu đồ
        setupBarChart(fromDate, toDate);



    }

    // Hiển thị DatePickerDialog cho việc chọn ngày
    private void showDatePicker() {
        // Thiết lập DatePickerDialog cho TextView chọn ngày bắt đầu
        startDateTextView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String dateFrom = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        startDateTextView.setText("Từ ngày: "+dateFrom);
                        fromDate = dateFrom;
                        totalRevenue = 0;
                        // Cập nhật biểu đồ khi chọn ngày bắt đầu
                        setupBarChart(fromDate, toDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Thiết lập DatePickerDialog cho TextView chọn ngày kết thúc
        endDateTextView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year12, monthOfYear, dayOfMonth) -> {
                        String dateTo = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year12;
                        endDateTextView.setText("Đến ngày: "+dateTo);
                        toDate = dateTo;
                        totalRevenue = 0;
                        // Cập nhật biểu đồ khi chọn ngày bắt đầu
                        setupBarChart(fromDate, toDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void setupBarChart(String fromDate, String toDate) {
        barChart = findViewById(R.id.barChart);
        barChart2 = findViewById(R.id.barChart2);
        barChart3 = findViewById(R.id.barChart3);

        apiService.getReportData(fromDate, toDate).enqueue(new Callback<StatisticalReportResponse>() {
            @Override
            public void onResponse(Call<StatisticalReportResponse> call, Response<StatisticalReportResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    StatisticalReportResponse apiResponse = response.body();
                    if(apiResponse.isSuccess()){
                        ArrayList<BarEntry> entriesQuantity = new ArrayList<>();
                        ArrayList<BarEntry> entriesRevenue = new ArrayList<>();
                        ArrayList<String> roomTypeList = new ArrayList<>();

                        // Xử lý dữ liệu
                        int index = 0; // Chỉ số để xác định vị trí trên biểu đồ
                        for (StatisticalReportResponse.Data data : apiResponse.getData()) {
                            Log.d("RoomType", data.getRoomType());
                            Log.d("Quantity", data.getBookingCount()+"");
                            Log.d("Revenue", data.getTotalAmount()+"");

                            totalRevenue += data.getTotalAmount();

                            // Định dạng lại tổng tiền với dấu phẩy
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setGroupingSeparator(','); // Dấu phẩy cho hàng nghìn
                            symbols.setDecimalSeparator('.');   // Dấu chấm cho phần thập phân

                            DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
                            String formattedTotalPrice = decimalFormat.format(totalRevenue);
                            tvTotalPriceAmount.setText(formattedTotalPrice+" VNĐ");

                            // Thêm tên loại phòng vào danh sách
                            roomTypeList.add(data.getRoomType());

                            // Thêm dữ liệu vào entriesQuantity và entriesRevenue
                            entriesQuantity.add(new BarEntry(index, data.getBookingCount()));
                            entriesRevenue.add(new BarEntry(index, (float) data.getTotalAmount())); // Chuyển đổi về float nếu cần

                            index++; // Tăng chỉ số
                        }

                        updateBarChart(entriesQuantity, entriesRevenue, roomTypeList);
                        updateBarChart2(entriesRevenue, roomTypeList);
                        updateBarChart3(entriesQuantity, roomTypeList);
                    }
                    else{
                        Toast.makeText(BaoCaoThongKe.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(BaoCaoThongKe.this, "ERROR: "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatisticalReportResponse> call, Throwable t) {
                Toast.makeText(BaoCaoThongKe.this, "ON FAILURE: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ON FAILURE: ", t.getMessage());
            }
        });

    }
    private void updateBarChart(ArrayList<BarEntry> entriesQuantity, ArrayList<BarEntry> entriesRevenue, ArrayList<String> roomTypeList) {
        // Đặt formatter cho trục X để hiển thị tên phòng
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < roomTypeList.size()) {
                    return roomTypeList.get((int) value); // Trả về tên loại phòng tương ứng
                } else {
                    return "";
                }
            }
        });
        // Tạo BarDataSet cho số lượng đặt
        BarDataSet barDataSetQuantity = new BarDataSet(entriesQuantity, "Số lượng đặt phòng");
        barDataSetQuantity.setColor(ColorTemplate.COLORFUL_COLORS[0]);
        barDataSetQuantity.setValueTextSize(14f);

        // Tạo BarDataSet cho doanh thu
        BarDataSet barDataSetRevenue = new BarDataSet(entriesRevenue, "Doanh thu phòng");
        barDataSetRevenue.setColor(ColorTemplate.COLORFUL_COLORS[1]);
        barDataSetRevenue.setValueTextSize(14f);

        // Tạo BarData với hai BarDataSet
        BarData barData = new BarData(barDataSetQuantity, barDataSetRevenue);

        float groupSpace = 0.4f;
        float barSpace = 0.02f;
        float barWidth = 0.3f;

        barData.setBarWidth(barWidth);
        barChart.setData(barData);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(entriesQuantity.size());
        barChart.groupBars(0, groupSpace, barSpace); // Nhóm các cột
        barChart.invalidate(); // Cập nhật biểu đồ

        // Tùy chọn khác
        Description description = new Description();
        description.setText("Doanh thu và số lượng đặt phòng");
        barChart.setDescription(description);

        // Tùy chỉnh thêm cho BarChart
        barChart.getAxisRight().setEnabled(false); // Tắt trục phải
        barChart.getXAxis().setGranularity(1f);    // Đặt khoảng cách giữa các cột
        barChart.animateY(1000);        // Thêm hiệu ứng animation
    }

    private void updateBarChart2(ArrayList<BarEntry> entriesRevenue, ArrayList<String> roomTypeList) {
        // Đặt formatter cho trục X để hiển thị tên phòng
        XAxis xAxis = barChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < roomTypeList.size()) {
                    return roomTypeList.get((int) value); // Trả về tên loại phòng tương ứng
                } else {
                    return "";
                }
            }
        });


        // Tạo BarDataSet cho doanh thu
        BarDataSet barDataSetRevenue = new BarDataSet(entriesRevenue, "Doanh thu phòng");
        barDataSetRevenue.setColor(ColorTemplate.COLORFUL_COLORS[1]);
        barDataSetRevenue.setValueTextSize(14f);

        // Tạo BarData với 1 BarDataSet
        BarData barData = new BarData(barDataSetRevenue);

        float groupSpace = 0.4f;
        float barSpace = 0.02f;
        float barWidth = 0.3f;

        barData.setBarWidth(barWidth);
        barChart2.setData(barData);
        barChart2.getXAxis().setAxisMinimum(0);
        barChart2.getXAxis().setAxisMaximum(entriesRevenue.size());
        barChart2.invalidate(); // Cập nhật biểu đồ

        // Tùy chọn khác
        Description description = new Description();
        description.setText("Doanh thu");
        barChart2.setDescription(description);

        // Tùy chỉnh thêm cho BarChart
        barChart2.getAxisRight().setEnabled(false); // Tắt trục phải
        barChart2.getXAxis().setGranularity(1f);    // Đặt khoảng cách giữa các cột
        barChart2.animateY(1000);        // Thêm hiệu ứng animation
    }

    private void updateBarChart3(ArrayList<BarEntry> entriesQuantity, ArrayList<String> roomTypeList) {
        // Đặt formatter cho trục X để hiển thị tên phòng
        XAxis xAxis = barChart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < roomTypeList.size()) {
                    return roomTypeList.get((int) value); // Trả về tên loại phòng tương ứng
                } else {
                    return "";
                }
            }
        });
        // Tạo BarDataSet cho số lượng đặt
        BarDataSet barDataSetQuantity = new BarDataSet(entriesQuantity, "Số lượng đặt phòng");
        barDataSetQuantity.setColor(ColorTemplate.COLORFUL_COLORS[0]);
        barDataSetQuantity.setValueTextSize(14f);


        // Thiết lập ValueFormatter cho BarDataSet để định dạng số nguyên
        barDataSetQuantity.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value); // Chuyển đổi thành số nguyên
            }
        });

        // Tạo BarData với 1 BarDataSet
        BarData barData = new BarData(barDataSetQuantity);

        float groupSpace = 0.4f;
        float barSpace = 0.02f;
        float barWidth = 0.3f;

        barData.setBarWidth(barWidth);
        barChart3.setData(barData);
        barChart3.getXAxis().setAxisMinimum(0);
        barChart3.getXAxis().setAxisMaximum(entriesQuantity.size() + 0.5f);

        barChart3.invalidate(); // Cập nhật biểu đồ

        // Tùy chọn khác
        Description description = new Description();
        description.setText("Số lượng đặt phòng");
        barChart3.setDescription(description);

        // Tùy chỉnh thêm cho BarChart
        barChart.getAxisRight().setEnabled(false); // Tắt trục phải
        barChart.getXAxis().setGranularity(1f);    // Đặt khoảng cách giữa các cột
        barChart3.animateY(1000);        // Thêm hiệu ứng animation
    }
}
