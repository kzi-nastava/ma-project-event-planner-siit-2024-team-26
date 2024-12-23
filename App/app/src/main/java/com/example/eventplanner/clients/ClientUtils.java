package com.example.eventplanner.clients;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.eventplanner.BuildConfig;
import com.example.eventplanner.clients.authorization.TokenManager;
import com.example.eventplanner.clients.authorization.TokenResponse;
import com.example.eventplanner.clients.service.AuthenticatedUserService;
import com.example.eventplanner.clients.service.AuthenticationService;
import com.example.eventplanner.clients.service.EventService;
import com.example.eventplanner.clients.service.NotificationService;
import com.example.eventplanner.clients.service.ProductService;
import com.example.eventplanner.clients.service.ServiceProductService;
import com.example.eventplanner.clients.service.ServiceService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {

    public static final String SERVICE_API_PATH = "http://"+ BuildConfig.IP_ADDR + ":8080/api/";


    private static Context givenContext;

    private static TokenManager tokenManager;

    public static void setContext(Context context){
        givenContext = context;
        tokenManager = new TokenManager(givenContext);
    }

    public static TokenManager getTokenManager(){
        if (tokenManager == null) {
            return new TokenManager(givenContext);
        }
        return tokenManager;
    }

    //TOKEN
    private static String getToken(){
        return tokenManager.getToken();
    }

    public static void saveToken(TokenResponse tokenResponse){
        tokenManager.saveToken(tokenResponse.getToken());
    }

    public static void removeToken(){
        tokenManager.removeToken();
    }

    //////////
    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        Interceptor tokenInterceptor = chain -> {
            String token = getToken();
            Request originalRequest = chain.request();
            // Proveri da li postoji token
            if (token != null && !token.isEmpty()) {
                Request requestWithToken = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(requestWithToken);
            } else {
                // Ako token ne postoji, nastavi bez Authorization zaglavlja
                return chain.proceed(originalRequest);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(tokenInterceptor)
                .pingInterval(30, TimeUnit.SECONDS)
                .build();

        return client;
    }


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                        @Override
                        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                        }
                    })
                    .create()))
            .client(test())
            .build();

    public static EventService eventService = retrofit.create(EventService.class);
    public static ServiceService serviceService = retrofit.create(ServiceService.class);
    public static ProductService productService = retrofit.create(ProductService.class);
    public static ServiceProductService serviceProductService = retrofit.create(ServiceProductService.class);
    public static AuthenticatedUserService authenticatedUserService = retrofit.create(AuthenticatedUserService.class);
    public static AuthenticationService authenticationService = retrofit.create(AuthenticationService.class);
    public static NotificationService notificationService = retrofit.create(NotificationService.class);

}
