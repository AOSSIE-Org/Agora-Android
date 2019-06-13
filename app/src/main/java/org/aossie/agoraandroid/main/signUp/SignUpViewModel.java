package org.aossie.agoraandroid.main.signUp;


import android.content.Context;
import android.widget.Toast;


import androidx.databinding.ObservableField;

import org.aossie.agoraandroid.main.remote.APIService;
import org.aossie.agoraandroid.main.remote.RetrofitClient;

import java.util.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends Observable {

    private final Context context;
    public final ObservableField<String> userName = new ObservableField<>("");
    public final ObservableField<String> userEmail = new ObservableField<>("");
    public final ObservableField<String> userPass = new ObservableField<>("");
    public final ObservableField<String> firstName = new ObservableField<>("");
    public final ObservableField<String> lastName = new ObservableField<>("");


    public SignUpViewModel(Context context) {

        this.context = context;
    }


    public void signUpRequest(String userName, String userPassword, String userEmail, String firstName, String lastName) {

        APIService apiService = RetrofitClient.getAPIService();
        Call<ResponseBody> signUpResponse = apiService.createUser(userName, userPassword, userEmail, firstName, lastName);
        signUpResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context, "" + response.body(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
