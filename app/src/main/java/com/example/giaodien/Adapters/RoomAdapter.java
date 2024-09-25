package com.example.giaodien.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.example.giaodien.Activities.AddCustomer;
import com.example.giaodien.Activities.UpdateBooking;
import com.example.giaodien.Activities.UpdateRoom;
import com.example.giaodien.Model.Room;
import com.example.giaodien.Activities.RoomBooking;
import com.example.giaodien.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    Context context;
    private ArrayList<Room> roomList;
    private String dateFrom;
    private String dateTo;
    private ActivityResultLauncher<Intent> launcher;

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

    public RoomAdapter(ArrayList<Room> roomList, String dateFrom, String dateTo, ActivityResultLauncher<Intent> launcher) {
        this.roomList = roomList;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.launcher = launcher;
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


            if (room.getStatus().equals("Đã đặt")) {
                Intent intent = new Intent(context, UpdateBooking.class);
                intent.putExtra("room_number", room.getRoom_number());
                launcher.launch(intent);
            } else {
                if (dateFrom.isEmpty() || dateTo.isEmpty()) {
                    Toast.makeText(context, "Vui lòng chọn ngày vào và ngày ra", Toast.LENGTH_SHORT).show();
                } else {
                    Date fromDate = null, toDate = null;
                    try {
                        fromDate = dateFormat.parse(dateFrom);
                        toDate = dateFormat.parse(dateTo);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (toDate.before(fromDate)) {
                        Toast.makeText(context, "Ngày ra không thể nhỏ hơn ngày vào!", Toast.LENGTH_SHORT).show();
                        return; // Ngừng thực hiện nếu ngày không hợp lệ
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
                        launcher.launch(intent);
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            showBottomSheetDialog(v.getContext(), room);
            return true;
        });
    }

    // Hiển thị BottomSheetDialog với các tùy chọn Cập nhật hoặc Xóa phòng
    private void showBottomSheetDialog(Context context, Room room) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_room_options, null);

        TextView tvUpdateRoom = bottomSheetView.findViewById(R.id.tv_update_room);
        TextView tvDeleteRoom = bottomSheetView.findViewById(R.id.tv_delete_room);

        // Cập nhật thông tin phòng
        tvUpdateRoom.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            // Chuyển sang màn hình cập nhật thông tin phòng
            Intent intent = new Intent(context, UpdateRoom.class);
            intent.putExtra("roomId", room.getRoom_id()+""); // Truyền thông tin phòng sang Activity
            intent.putExtra("room_number", room.getRoom_number());
            context.startActivity(intent);
        });

        // Xóa phòng với xác nhận từ người dùng
        tvDeleteRoom.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showDeleteConfirmationDialog(context, room); // Hiển thị xác nhận trước khi xóa
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    // Hiển thị hộp thoại xác nhận khi xóa phòng
    private void showDeleteConfirmationDialog(Context context, Room room) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa phòng")
                .setMessage("Bạn có chắc chắn muốn xóa phòng " + room.getRoom_number() + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xử lý xóa phòng
                    deleteRoom(room);
                    Toast.makeText(context, "Phòng " + room.getRoom_number() + " đã bị xóa", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss(); // Đóng hộp thoại nếu người dùng chọn Hủy
                })
                .show();
    }

    // Hàm xử lý xóa phòng
    private void deleteRoom(Room room) {
        // Xóa phòng khỏi danh sách và cập nhật RecyclerView
        roomList.remove(room);
        notifyDataSetChanged();
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
