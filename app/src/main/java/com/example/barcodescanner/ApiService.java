package com.example.barcodescanner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    //@GET("/orders/all")
    @GET("/orders/last")
    Call<List<PostModel>> getOrders();

    @POST("/import_track/{order_id}/{track_id}")
    Call<PostModel> importTrack(
            @Path("order_id") String orderId,
            @Path("track_id") String trackId,
            @Body PostModel postModel
    );

    @POST("/add_order")
    Call<DataModal> createPost(@Body DataModal dataModal);
}
