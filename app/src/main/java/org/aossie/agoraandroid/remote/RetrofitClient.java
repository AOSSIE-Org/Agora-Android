package org.aossie.agoraandroid.remote;

import retrofit2.Retrofit;

public class RetrofitClient {

    private static final String BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/";

    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build();

    }

    public static APIService getAPIService() {
        return getRetrofitInstance().create(APIService.class);
    }


}
