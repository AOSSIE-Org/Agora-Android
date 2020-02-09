package org.aossie.agoraandroid.remote;

import retrofit2.Retrofit;

import static org.aossie.agoraandroid.utilities.AppConstants.BASE_URL;

public class RetrofitClient {

    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build();

    }

    public static APIService getAPIService() {
        return getRetrofitInstance().create(APIService.class);
    }


}
