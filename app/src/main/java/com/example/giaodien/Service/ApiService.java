package com.example.giaodien.Service;

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

public interface ApiService {
    @POST("checklogin.php")
    Call<LoginResponse> loginUser(@Body Staff staff);

    @GET("dataroom.php")
    Call<List<Room>> getRooms();
}
