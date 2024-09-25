package com.example.giaodien.Service;

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

    @GET("dataRoom.php")
    Call<List<Room>> getRooms();

    @GET("dataStaff.php")
    Call<List<Staff>> getStaffs();

    @GET("dataSelectOneRoom.php")
    Call<BookingDetailsResponse> getBookingDetails(@Query("room_number") String roomNumber);

    @GET("dataCustomer.php")
    Call<List<Customers>> getCustomers();

    @GET("cancelRoomBooking.php")
    Call<DataResponse> cancelBookRoom(@Query("room_number") String roomNumber);

    @GET("deleteCustomer.php")
    Call<DataResponse> deleteCustomer(@Query("customer_id") int customerId);

    @GET("deleteStaff.php")
    Call<DataResponse> deleteStaff(@Query("staff_id") int staffId);

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

    @POST("updateCustomer.php")
    Call<DataResponse> updateDataCustomer(@Body Customers customers);

    @POST("updateStaff.php")
    Call<DataResponse> updateDataStaff(@Body Staff staff);

    @POST("updateRoom.php")
    Call<DataResponse> updateRoom(@Body Room room);

    @POST("updatePassword.php")
    Call<DataResponse> updatePassword(@Body Staff staff);
}
