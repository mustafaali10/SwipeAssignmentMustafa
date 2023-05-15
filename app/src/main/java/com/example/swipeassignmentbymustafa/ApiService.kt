package com.example.swipeassignmentbymustafa

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @GET("public/get")

     fun getProducts(): Call<MutableList<PostModel>>


     @Multipart
     @POST("public/add")
        fun addProducts(@Part("product_name") productName: RequestBody,
                        @Part("product_type") productType: RequestBody,
                        @Part("price") price: RequestBody,
                        @Part("tax") tax: RequestBody,
                        @Part image: MultipartBody.Part?): Call<ResponseBody>
}