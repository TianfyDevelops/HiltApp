package com.example.hiltapp.api

import com.example.hiltapp.data.BaseResponse
import com.example.hiltapp.data.LoginBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp
 * @ClassName:      RetrofitService
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2020/12/23 9:20
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/12/23 9:20
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
interface RetrofitService {

    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") passWord: String,
        @Field("repassword") rePassWord: String
    ): BaseResponse<LoginBean>

    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") userName: String,
        @Field("password") passWord: String,
        @Field("repassword") rePassWord: String
    ): BaseResponse<LoginBean>


}