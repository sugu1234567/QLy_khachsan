package com.example.giaodien.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giaodien.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class BaoCaoThongKe extends AppCompatActivity {

    private BarChart barChart;
    private TextView startDateTextView, endDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bao_cao_thong_ke);

        // Nút quay lại
        ImageView backButton = findViewById(R.id.back_arrow);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Các TextView hiển thị ngày
        LinearLayout startDateLayout = findViewById(R.id.start_date_layout);
        LinearLayout endDateLayout = findViewById(R.id.end_date_layout);
        startDateTextView = findViewById(R.id.tv_start_date);
        endDateTextView = findViewById(R.id.tv_end_date);

        // Gán sự kiện chọn ngày cho các TextView
        startDateLayout.setOnClickListener(v -> showDatePicker(startDateTextView));
        endDateLayout.setOnClickListener(v -> showDatePicker(endDateTextView));

        // Thiết lập biểu đồ
        setupBarChart();
    }

    // Hiển thị DatePickerDialog cho việc chọn ngày
    private void showDatePicker(final TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, year1, monthOfYear, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
            dateTextView.setText(selectedDate); // Gán ngày đã chọn vào TextView
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setupBarChart() {
        barChart = findViewById(R.id.barChart);

        // Dữ liệu cho số lượng đặt
        ArrayList<BarEntry> entriesQuantity = new ArrayList<>();
        entriesQuantity.add(new BarEntry(0, 500));   // Phòng Standard, số lượng
        entriesQuantity.add(new BarEntry(1, 102));   // Phòng Superior, số lượng
        entriesQuantity.add(new BarEntry(2, 7));     // Phòng Premium, số lượng
        entriesQuantity.add(new BarEntry(3, 7));     // Phòng Deluxe, số lượng
        entriesQuantity.add(new BarEntry(4, 7));     // Phòng Suite, số lượng

        // Dữ liệu cho doanh thu
        ArrayList<BarEntry> entriesRevenue = new ArrayList<>();
        entriesRevenue.add(new BarEntry(0, 10000f));   // Phòng Standard, doanh thu
        entriesRevenue.add(new BarEntry(1, 15000f));   // Phòng Superior, doanh thu
        entriesRevenue.add(new BarEntry(2, 20000f));   // Phòng Premium, doanh thu
        entriesRevenue.add(new BarEntry(3, 30000f));   // Phòng Deluxe, doanh thu
        entriesRevenue.add(new BarEntry(4, 40000f));   // Phòng Suite, doanh thu

        // Tạo nhãn cho các loại phòng
        final String[] roomTypes = new String[]{"Standard", "Superior", "Premium", "Deluxe", "Suite"};

        // Đặt formatter cho trục X để hiển thị tên phòng
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < roomTypes.length) {
                    return roomTypes[(int) value];
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
        barChart.getXAxis().setAxisMaximum(roomTypes.length);
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
}
