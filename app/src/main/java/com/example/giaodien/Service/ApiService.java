package com.example.giaodien.Service;

import com.example.giaodien.Model.BookRoomResponse;
import com.example.giaodien.Model.BookingDetailsResponse;
import com.example.giaodien.Model.BookingRequest;
import com.example.giaodien.Model.Bookings;
import com.example.giaodien.Model.Customers;
import com.example.giaodien.Model.LoginResponse;
import com.example.giaodien.Model.Room;
import com.example.giaodien.Model.Staff;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("checkLogin.php")
    Call<LoginResponse> loginUser(@Body Staff staff);

    @GET("dataRoom.php")
    Call<List<Room>> getRooms();

    @GET("dataSelectOneRoom.php")
    Call<BookingDetailsResponse> getBookingDetails(@Query("room_number") String roomNumber);

    @GET("dataCustomer.php")
    Call<List<Customers>> getCustomers();

    @GET("cancelRoomBooking.php")
    Call<BookRoomResponse> cancelBookRoom(@Query("room_number") String roomNumber);

    @POST("roomBookings.php")
    Call<BookRoomResponse> bookRoom(@Body BookingRequest bookingRequest);

    @POST("updateRoomBooking.php")
    Call<BookRoomResponse> updateBookRoom(@Body BookingRequest bookingRequest);


}
