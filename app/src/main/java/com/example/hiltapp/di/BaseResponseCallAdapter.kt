package com.example.hiltapp.di

import com.example.hiltapp.data.BaseResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp.di
 * @ClassName:      BaseResponseCallAdapter
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2021/1/19 10:14
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/1/19 10:14
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class BaseResponseCallAdapter<T>(private val type: Type) : CallAdapter<T, Call<BaseResponse<T>>> {
    override fun adapt(call: Call<T>): Call<BaseResponse<T>> {

        return BaseResponseCall(call)
    }

    override fun responseType(): Type = type
}