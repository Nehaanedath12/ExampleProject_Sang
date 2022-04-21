package com.example.exampleproject.RetroFit;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("UserExists")
    public String UserExists;
    @SerializedName("iId")
    public int iId;
    @SerializedName("sUserName")
    public String sUserName;

    public LoginResponse(String userExists, int iId, String sUserName) {
        this.UserExists = userExists;
        this.iId = iId;
        this.sUserName = sUserName;
    }

    public String getUserExists() {
        return UserExists;
    }

    public int getiId() {
        return iId;
    }

    public String getsUserName() {
        return sUserName;
    }
}
