package com.ccdp.appmember.api;

import com.ccdp.appmember.model.LoginResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lenovo on 2/20/2017.
 */

public interface UserAPI {

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResult> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/logout")
    Call<LoginResult> logout(@Field("token") String token);
}
