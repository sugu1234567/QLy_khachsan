package com.example.giaodien.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.example.giaodien.Activities.UpdateBooking;
import com.example.giaodien.Model.Room;
import com.example.giaodien.Activities.RoomBooking;
import com.example.giaodien.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    Context context;
    private ArrayList<Room> roomList;
    private String dateFrom;
    private String dateTo;

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
        notifyDataSetChanged();
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        notifyDataSetChanged();
    }

    public RoomAdapter(ArrayList<Room> roomList, String dateFrom, String dateTo) {
        this.roomList = roomList;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        if(roomList.get(position) == null) return;

        Room room = roomList.get(position);
        holder.roomNumber.setText(room.getRoom_number());
        holder.roomPrice.setText(room.getPrice());
        holder.roomType.setText(room.getRoom_type());
        holder.roomStatus.setText(room.getStatus());
        if(room.getStatus().equals("Đã đặt")){
            holder.roomItem.setBackground(ContextCompat.getDrawable(context,R.drawable.room_unavailable));
        }
        else if(room.getStatus().equals("Trống")){
            holder.roomItem.setBackground(ContextCompat.getDrawable(context,R.drawable.room_available));
        }

        holder.roomItem.setOnClickListener(view -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar currentCalendar = getCalendar();

            if (room.getStatus().equals("Đã đặt")) {
                Intent intent = new Intent(context, UpdateBooking.class);
                intent.putExtra("room_number", room.getRoom_number());
                ((Activity) context).finish();
                context.startActivity(intent);
            } else {
                if (dateFrom.isEmpty() || dateTo.isEmpty()) {
                    Toast.makeText(context, "Vui lòng chọn ngày đến và ngày đi", Toast.LENGTH_SHORT).show();
                } else {
                    Date fromDate = null, toDate = null;
                    try {
                        fromDate = dateFormat.parse(dateFrom);
                        toDate = dateFormat.parse(dateTo);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Chuyển đổi từ Date sang Calendar để so sánh
                    Calendar fromCalendar = Calendar.getInstance();
                    fromCalendar.setTime(fromDate);
                    fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    fromCalendar.set(Calendar.MINUTE, 0);
                    fromCalendar.set(Calendar.SECOND, 0);
                    fromCalendar.set(Calendar.MILLISECOND, 0);

                    Calendar toCalendar = Calendar.getInstance();
                    toCalendar.setTime(toDate);
                    toCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    toCalendar.set(Calendar.MINUTE, 0);
                    toCalendar.set(Calendar.SECOND, 0);
                    toCalendar.set(Calendar.MILLISECOND, 0);

                    if (fromCalendar.before(currentCalendar) || toCalendar.before(currentCalendar) || toCalendar.before(fromCalendar)) {
                        Toast.makeText(context, "Vui lòng chọn lại ngày!", Toast.LENGTH_SHORT).show();
                    } else {

                        long differenceInMillis = toDate.getTime() - fromDate.getTime();
                        long daysBetween = TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);

                        // Lấy giá phòng và tính tổng tiền
                        String priceString = room.getPrice().replace(",", "");
                        double pricePerNight = Double.parseDouble(priceString);
                        // cộng thêm 1 lý do nếu người dùng chọn ngày đến và đi trong cùng 1 ngày ta vẫn tính tiền
                        // nếu không cộng thêm 1 khi ngày đến trùng ngày đi giá tiền sẽ là 0K
                        double totalPrice = (daysBetween+1) * pricePerNight;

                        // Định dạng lại tổng tiền với dấu phẩy
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                        symbols.setGroupingSeparator(','); // Dấu phẩy cho hàng nghìn
                        symbols.setDecimalSeparator('.');   // Dấu chấm cho phần thập phân

                        DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);
                        String formattedTotalPrice = decimalFormat.format(totalPrice);

                        Intent intent = new Intent(context, RoomBooking.class);
                        intent.putExtra("room_number", room.getRoom_number());
                        intent.putExtra("date_from", dateFrom);
                        intent.putExtra("date_to", dateTo);
                        intent.putExtra("total_price", formattedTotalPrice);
                        ((Activity) context).finish();
                        context.startActivity(intent);
                    }
                }
            }
        });

    }

    private static @NonNull Calendar getCalendar() {
        Date currentDate = new Date();

        // Cần phải chuyển giờ:phút:giây về 0 hết là vì muốn trong ngày hiện tại khách hàng vẫn có thể đặt phòng được
        // Nếu chỉ lấy currentDate từ hàm new Date() thì sẽ trả về cả thời gian hiện tại mà trong khi đó
        // fromdate và todate thời gian lại là 00:00:00 nên cần phải chuyển tất cả giờ về 0 và chuyển về chung Calendar để thực hiện so sánh với 2 kiểu dữ liệu như nhau.

        // Đặt currentDate về 00:00:00
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        return currentCalendar;
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomNumber, roomType, roomPrice, roomStatus;
        LinearLayout roomItem;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomNumber = itemView.findViewById(R.id.roomNumber);
            roomItem = itemView.findViewById(R.id.roomItem);
            roomType = itemView.findViewById(R.id.roomType);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            roomStatus = itemView.findViewById(R.id.roomStatus);
        }
    }
}
