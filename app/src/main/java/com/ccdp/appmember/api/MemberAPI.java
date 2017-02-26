package com.ccdp.appmember.api;


import com.ccdp.appmember.model.Member;
import com.ccdp.appmember.model.MemberResults;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MemberAPI {

    @GET("member/all")
    Call<MemberResults> all(@Query("token") String token);

    @GET("member/find")
    Call<MemberResults> find(@Query("id") int id,@Query("token") String token);

    @FormUrlEncoded
    @POST("member/insert")
    Call<Member> insert(@Field("member_name") String memberName,
                            @Field("member_email") String memberEmail,
                            @Field("member_birthdate") String memberBirthdate,
                            @Field("member_sex") String memberSex,
                            @Field("member_religion") String memberReligion,
                            @Field("member_address") String memberAddress,
                            @Field("token") String token);

    @FormUrlEncoded
    @POST("member/update")
    Call<Member> update(@Field("member_name") String memberName,
                            @Field("member_email") String memberEmail,
                            @Field("member_birthdate") String memberBirthdate,
                            @Field("member_sex") String memberSex,
                            @Field("member_religion") String memberReligion,
                            @Field("member_address") String memberAddress,
                            @Field("id") int id,
                        @Field("token") String token);

    @FormUrlEncoded
    @POST("member/delete")
    Call<Member> delete(@Field("id") int id,@Field("token") String token);

    @Multipart
    @POST("member/upload")
    Call<ResponseBody> upload(@Part("id") int id,@Part MultipartBody.Part file,@Part("token") String token);
}
