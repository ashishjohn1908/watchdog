package com.app.watchdog.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Ashish John on 18/2/19.
 */
public class ApiServices {

    private static final String API_URL = "https://watchdog.finoit.com/api/watchdog/";

    public static RetrofitService retrofitService = null;

    public static RetrofitService getRetrofitService() {

        if (retrofitService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitService = retrofit.create(RetrofitService.class);
        }
        return retrofitService;
    }

    public interface RetrofitService {

        @Headers({"Accept: application/json", "Content-Type: application/json", "platform:Android"})
        @POST("app-info/")
        Call<DataModel> postDeviceInfo(@Body DataModel model,
                                       @Header("api_key") String apiKey);
    }
}
