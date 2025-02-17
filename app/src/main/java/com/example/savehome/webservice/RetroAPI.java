package com.example.savehome.webservice;

import com.example.savehome.Model.AddAudioModel;
import com.example.savehome.Model.AddContactModel;
import com.example.savehome.Model.AddVideoModel;
import com.example.savehome.Model.ChangePasswordModel;
import com.example.savehome.Model.DeleteContactModel;
import com.example.savehome.Model.DeleteVideoModel;
import com.example.savehome.Model.EditAudioModel;
import com.example.savehome.Model.EditProfileModel;
import com.example.savehome.Model.EditVideoModel;
import com.example.savehome.Model.ForgotPasswordModel;
import com.example.savehome.Model.GetUserDetailModel;
import com.example.savehome.Model.ListAudioModel;
import com.example.savehome.Model.ListContactModel;
import com.example.savehome.Model.ListVideoModel;
import com.example.savehome.Model.LogOutModel;
import com.example.savehome.Model.LoginModel;
import com.example.savehome.Model.RegisterModel;
import com.example.savehome.Model.ResetPasswordModel;
import com.example.savehome.Model.SocialLoginModel;
import com.example.savehome.Model.VerifyOTPModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetroAPI {

    @Multipart
    @POST("add-contact")
    Call<AddContactModel> AddContactDetail(@Header("custom-token") String token,
                                           @Part MultipartBody.Part photo,
                                           @Part("userid") Integer userid,
                                           @Part("name") RequestBody name,
                                           @Part("number") RequestBody number
    );

    @Multipart
    @POST("register")
    Call<RegisterModel> register(@Part("gender") RequestBody gender,
                                 @Part("email") RequestBody email,
                                 @Part("password") RequestBody password,
                                 @Part("name") RequestBody name,
                                 @Part("fcm_token") RequestBody fcm_token,
                                 @Part("fcm_id") RequestBody fcm_id,
                                 @Part MultipartBody.Part image
    );


    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> login(@Field("username") String username,
                           @Field("password") String password,
                           @Field("fcm_token") String fcm_token,
                           @Field("fcm_id") String fcm_id
    );

    @FormUrlEncoded
    @POST("change-password")
    Call<ChangePasswordModel> changePassword(@Field("email") String email,
                                             @Field("password") String password,
                                             @Field("old_password") String old_password
    );

    @FormUrlEncoded
    @POST("forget-password")
    Call<ForgotPasswordModel> forgotPassword(@Field("email") String email
    );

    @FormUrlEncoded
    @POST("reset-password")
    Call<ResetPasswordModel> resetPassword(@Field("password") String password,
                                           @Field("email") String email
    );

    @FormUrlEncoded
    @POST("verify-otp")
    Call<VerifyOTPModel> verifyOTP(
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("update-profile")
    Call<EditProfileModel> EditProfileDetail(@Field("photo") String photo,
                                             @Field("name") String name,
                                             @Field("email") String email

    );

    @POST("logout")
    Call<LogOutModel> LogOutDetail(@Header("custom-token") String token
    );


    @POST("get-user-details")
    Call<GetUserDetailModel> GetUserDetailModel(@Header("custom-token") String token
    );

    @Multipart
    @POST("list-contact")
    Call<ListContactModel> ListContactDetail(@Header("custom-token") String token,
                                             @Part MultipartBody.Part photo,
                                             @Part("userid") Integer userid,
                                             @Part("name") RequestBody name,
                                             @Part("number") RequestBody number
    );

    @Multipart
    @POST("list-contact")
    Call<AddAudioModel> AddAudioDetail(@Header("custom-token") String token,
                                       @Part MultipartBody.Part audio,
                                       @Part("userid") Integer userid,
                                       @Part("uploadtime") RequestBody uploadtime

    );

    @Multipart
    @POST("add-video")
    Call<AddVideoModel> AddVideoDetail(@Header("custom-token") String token,
                                       @Part MultipartBody.Part video,
                                       @Part("userid") Integer userid,
                                       @Part("uploadtime") String uploadtime

    );

    @Multipart
    @POST("edit-video")
    Call<EditVideoModel> EditVideoDetail(@Header("custom-token") String token,
                                         @Part MultipartBody.Part video,
                                         @Part("userid") Integer userid,
                                         @Part("uploadtime") String uploadtime,
                                         @Part("id") String id

    );


    @FormUrlEncoded
    @POST("delete-video")
    Call<DeleteVideoModel> DeleteVideoDetail(@Header("custom-token") String token,
                                             @Field("id") Integer id

    );


    @FormUrlEncoded
    @POST("list-video")
    Call<ListVideoModel> ListVideoModelDetail(@Header("custom-token") String token,
                                              @Field("id") Integer id
    );

    @Multipart
    @POST("delete-contact")
    Call<DeleteContactModel> DeleteContactModelDetail(@Header("custom-token") String token,
                                                      @Part MultipartBody.Part photo,
                                                      @Part("userid") Integer userid,
                                                      @Part("name") String name,
                                                      @Part("number") String number,
                                                      @Part("id") String id

    );


    @Multipart
    @POST("edit-audio")
    Call<EditAudioModel> EditAudioModelDetail(@Header("custom-token") String token,
                                              @Part MultipartBody.Part audio,
                                              @Part("userid") Integer userid,
                                              @Part("uploadtime") String uploadtime,
                                              @Part("id") String id
    );

    @Multipart
    @POST("list-audio")
    Call<ListAudioModel> ListAudioDetail(@Header("custom-token") String token,
                                         @Part("id") Integer id

    );

    @FormUrlEncoded
    @POST("social-login")
    Call<SocialLoginModel> SocialLoginModelDetail(@Field("google_id") String google_id,
                                                  @Field("email") String email,
                                                  @Field("device_type") String device_type,
                                                  @Field("fcm_id") String fcm_id,
                                                  @Field("fcm_token") String fcm_token,
                                                  @Field("name") String name
    );

}
