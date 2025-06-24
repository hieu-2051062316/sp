package com.example.hanoconnectadmin.networking;

import com.example.hanoconnectadmin.EditProfileActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://b5ba-14-248-114-119.ngrok-free.app/";

    private static Retrofit retrofit = null;

    public static ApiService getApiService(EditProfileActivity editProfileActivity) {
        if (retrofit == null) {
            // Tạo interceptor để log thông tin
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Tạo interceptor để thêm header bỏ qua cảnh báo của ngrok
            Interceptor ngrokInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("ngrok-skip-browser-warning", "true")
                            .build();
                    return chain.proceed(request);
                }
            };

            // Tạo OkHttpClient và thêm CẢ HAI interceptor vào
            // Đồng thời tăng thời gian chờ (timeout)
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(ngrokInterceptor)      // Thêm header trước
                    .addInterceptor(loggingInterceptor)      // Sau đó mới log
                    .connectTimeout(60, TimeUnit.SECONDS) // Tăng thời gian chờ kết nối
                    .readTimeout(60, TimeUnit.SECONDS)    // Tăng thời gian chờ đọc dữ liệu
                    .writeTimeout(60, TimeUnit.SECONDS)   // Tăng thời gian chờ ghi dữ liệu
                    .build();

            // Xây dựng Retrofit với OkHttpClient đã được cấu hình
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}