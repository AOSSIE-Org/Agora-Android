package org.aossie.agoraandroid.remote;

import android.content.Context;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

  private static final String BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/";
  private static final HttpLoggingInterceptor.Level HTTP_INTERCEPTOR_LEVEL = HttpLoggingInterceptor.Level.BASIC;

  private static Retrofit getRetrofitInstance(Context context) {

    Gson gson = new GsonBuilder().setLenient().create();

    HttpLoggingInterceptor httpInterceptor = new HttpLoggingInterceptor();
    httpInterceptor.level(HTTP_INTERCEPTOR_LEVEL);

    ChuckerInterceptor chuckerInterceptor = new ChuckerInterceptor.Builder(context)
        .alwaysReadResponseBody(true)
        .build();

    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(httpInterceptor)
        .addInterceptor(chuckerInterceptor)
        .build();

    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory
            .create(gson))
        .build();
  }
  public static APIService getAPIService(Context context) {
    return getRetrofitInstance(context).create(APIService.class);
  }
}
