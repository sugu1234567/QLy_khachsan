package com.example.giaodien.Activities.Service;

import com.example.giaodien.Activities.Model.LoginResponse;
import com.example.giaodien.Activities.Model.Room;
import com.example.giaodien.Activities.Model.Staff;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("checklogin.php")
    Call<LoginResponse> loginUser(@Field("username") String username, @Field("password") String password);

    @GET("dataroom.php")
    Call<List<Room>> getRooms();
}
