package com.example.giaodien.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Activities.AddBill;
import com.example.giaodien.R;
import com.example.giaodien.Response.BookingDetailsResponse;
import com.example.giaodien.Service.ApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> implements Filterable {

    private Context context;
    private ArrayList<BookingDetailsResponse> billList, billListOld;
    private ApiService apiService;
    private String selectedStatus = "";
    private ActivityResultLauncher<Intent> launcher;

    public BillAdapter(ArrayList<BookingDetailsResponse> billList, ActivityResultLauncher<Intent> launcher) {
        this.billList = billList;
        this.billListOld = billList;
        this.launcher = launcher;
    }

    public void setSelectedStatus(String status) {
        this.selectedStatus = status;
        getFilter().filter(""); // Gọi lại filter để cập nhật dữ liệu
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        if (billList.get(position) == null) return;

        BookingDetailsResponse bookingDetailsResponse = billList.get(position);

        holder.tvRoomNumber.setText(bookingDetailsResponse.getRoom().getRoom_number());
        holder.tvCustomerName.setText(bookingDetailsResponse.getCustomer().getFullname());
        holder.tvCheckInText.setText("Ngày vào: "+bookingDetailsResponse.getBooking().getCheck_in_date());
        holder.tvCheckOut.setText("Ngày ra: "+bookingDetailsResponse.getBooking().getCheck_out_date());
        holder.tvStatus.setText("Trạng thái: "+bookingDetailsResponse.getBooking().getStatus());
        holder.tvTotalAmount.setText("Tổng tiền: "+bookingDetailsResponse.getBooking().getPrice_booking()+" VNĐ");

        if(bookingDetailsResponse.getBooking().getStatus().equals("Đã thanh toán")){
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green_light));
        }
        else if(bookingDetailsResponse.getBooking().getStatus().equals("Chưa thanh toán")){
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        else if(bookingDetailsResponse.getBooking().getStatus().equals("Hủy đặt phòng")){
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.item_selected_border));
        }

        // Khi nhấn vào CardView, chuyển đến màn hình thêm hóa đơn (AddBill)
        holder.cardView.setOnClickListener(view -> {
            if(bookingDetailsResponse.getBooking().getStatus().equals("Chưa thanh toán")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                try {
                    String currentDateString = dateFormat.format(calendar.getTime());
                    Date currentDate = dateFormat.parse(currentDateString);
                    Date checkInDate = dateFormat.parse(bookingDetailsResponse.getBooking().getCheck_in_date());
                    if(currentDate.before(checkInDate)){
                        Toast.makeText(context, "Phòng này được đặt trước!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent = new Intent(context, AddBill.class);
                        intent.putExtra("booking_id", bookingDetailsResponse.getBooking().getBooking_id());
                        intent.putExtra("room_id", bookingDetailsResponse.getRoom().getRoom_id());
                        intent.putExtra("room_number", bookingDetailsResponse.getRoom().getRoom_number());
                        intent.putExtra("fullname", bookingDetailsResponse.getCustomer().getFullname());
                        intent.putExtra("check_in_date", bookingDetailsResponse.getBooking().getCheck_in_date());
                        intent.putExtra("price_room", bookingDetailsResponse.getRoom().getPrice());
                        launcher.launch(intent);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                if((query.isEmpty() || query.equals("")) && selectedStatus.equals("Tất cả")){
                    billList = billListOld;
                }
                else{
                    ArrayList<BookingDetailsResponse> arrayList = new ArrayList<>();
                    for(BookingDetailsResponse response : billListOld){
                        if((response.getRoom().getRoom_number().toLowerCase().contains(query.toLowerCase()) ||
                                response.getCustomer().getFullname().toLowerCase().contains(query.toLowerCase()))
                                && selectedStatus.equals("Tất cả")){
                            arrayList.add(response);
                        }
                        else if((response.getRoom().getRoom_number().toLowerCase().contains(query.toLowerCase()) ||
                                response.getCustomer().getFullname().toLowerCase().contains(query.toLowerCase()))
                        && response.getBooking().getStatus().equals("Đã thanh toán")
                        && selectedStatus.equals("Đã thanh toán")){
                            arrayList.add(response);
                        }
                        else if((response.getRoom().getRoom_number().toLowerCase().contains(query.toLowerCase()) ||
                                response.getCustomer().getFullname().toLowerCase().contains(query.toLowerCase()))
                                && response.getBooking().getStatus().equals("Chưa thanh toán")
                                && selectedStatus.equals("Chưa thanh toán")){
                            arrayList.add(response);
                        }
                        else if((response.getRoom().getRoom_number().toLowerCase().contains(query.toLowerCase()) ||
                                response.getCustomer().getFullname().toLowerCase().contains(query.toLowerCase()))
                                && response.getBooking().getStatus().equals("Hủy đặt phòng")
                                && selectedStatus.equals("Hủy đặt phòng")){
                            arrayList.add(response);
                        }

                    }
                    billList = arrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = billList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                billList = (ArrayList<BookingDetailsResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvCustomerName, tvCheckInText, tvCheckOut, tvStatus, tvTotalAmount;
        CardView cardView;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCheckInText = itemView.findViewById(R.id.tvCheckInText);
            tvCheckOut = itemView.findViewById(R.id.tvCheckOut);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            cardView = itemView.findViewById(R.id.cardViewBill);

        }
    }
}
