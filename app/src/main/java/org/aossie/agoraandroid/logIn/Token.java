
package org.aossie.agoraandroid.logIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {
    public Token() {
    }

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("expiresOn")
    @Expose
    private String expiresOn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(String expiresOn) {
        this.expiresOn = expiresOn;
    }

}