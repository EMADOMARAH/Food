package com.emadomarah.food.network;


import com.emadomarah.food.model.ImageId;
import com.emadomarah.food.network.Models.Response.Image.ImageResponse;
import com.emadomarah.food.network.Models.Response.ImageId.ImageIdResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {


    @Multipart
    @POST("image/segmentation/complete/v1.0")
    public Call<ImageResponse> uploadImage(
            @Header("Authorization") String header
            ,@Part MultipartBody.Part image
            );

    @POST("nutrition/recipe/nutritionalInfo/v1.0")
    public Call<ImageIdResponse>getImageInfo(
            @Header("Authorization") String header,
            @Body ImageId imageId
            );
}
