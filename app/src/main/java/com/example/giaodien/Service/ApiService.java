package com.example.giaodien.Service;

import com.example.giaodien.Model.Payments;
import com.example.giaodien.Response.StatisticalReportResponse;
import com.example.giaodien.Response.BookingDetailsResponse;
import com.example.giaodien.Response.BookingRequest;
import com.example.giaodien.Model.Customers;
import com.example.giaodien.Response.DataResponse;
import com.example.giaodien.Response.LoginResponse;
import com.example.giaodien.Model.Room;
import com.example.giaodien.Model.Staff;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("checkLogin.php")
    Call<LoginResponse> loginUser(@Body Staff staff);

    @POST("dataRoom.php")
    Call<List<Room>> getRooms();

    @POST("dataStaff.php")
    Call<List<Staff>> getStaffs();

    @POST("dataCustomer.php")
    Call<List<Customers>> getCustomers();

    @POST("dataBooking.php")
    Call<List<BookingDetailsResponse>> getBills();

    @POST("roomBookings.php")
    Call<DataResponse> bookRoom(@Body BookingRequest bookingRequest);

    @POST("updateRoomBooking.php")
    Call<DataResponse> updateBookRoom(@Body BookingRequest bookingRequest);

    @POST("addCustomer.php")
    Call<DataResponse> addNewCustomer(@Body Customers customers);

    @POST("addRoom.php")
    Call<DataResponse> addNewRoom(@Body Room room);

    @POST("addStaff.php")
    Call<DataResponse> addNewStaff(@Body Staff staff);

    @POST("payBill.php")
    Call<DataResponse> payBill(@Body Payments payments);

    @POST("updateCustomer.php")
    Call<DataResponse> updateDataCustomer(@Body Customers customers);

    @POST("updateStaff.php")
    Call<DataResponse> updateDataStaff(@Body Staff staff);

    @POST("updateRoom.php")
    Call<DataResponse> updateRoom(@Body Room room);

    @POST("updatePassword.php")
    Call<DataResponse> updatePassword(@Body Staff staff);

    //@GET("statisticalReport.php")
    //Call<ReportResponse> getReportData(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("statisticalReport.php") // Thay thế bằng đường dẫn thực tế đến API
    Call<StatisticalReportResponse> getReportData(
            @Query("fromDate") String fromDate,
            @Query("toDate") String toDate
    );

    @GET("dataSelectOneRoom.php")
    Call<BookingDetailsResponse> getBookingDetails(@Query("room_number") String roomNumber);

    @GET("cancelRoomBooking.php")
    Call<DataResponse> cancelBookRoom(@Query("room_number") String roomNumber);

    @GET("deleteCustomer.php")
    Call<DataResponse> deleteCustomer(@Query("customer_id") int customerId);

    @GET("deleteStaff.php")
    Call<DataResponse> deleteStaff(@Query("staff_id") int staffId);

    @GET("deleteRoom.php")
    Call<DataResponse> deleteRoom(@Query("room_id") int roomId);


}
