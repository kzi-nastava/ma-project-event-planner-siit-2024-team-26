package com.example.eventplanner.clients;
import com.example.eventplanner.BuildConfig;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.service.EventService;
import com.example.eventplanner.service.ProductService;
import com.example.eventplanner.service.ServiceService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {

    public static final String SERVICE_API_PATH = "http://"+ BuildConfig.IP_ADDR + ":8080/api/";

    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    public static EventService eventService = retrofit.create(EventService.class);
    public static ServiceService serviceService = retrofit.create(ServiceService.class);
    public static ProductService productService = retrofit.create(ProductService.class);

}
