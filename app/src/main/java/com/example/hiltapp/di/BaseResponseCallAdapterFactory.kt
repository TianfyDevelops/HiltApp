package com.example.hiltapp.di

import com.example.hiltapp.data.BaseResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp.di
 * @ClassName:      BaseResponseCallAdapterFactory
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2021/1/19 10:05
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/1/19 10:05
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class BaseResponseCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //检查returnType是否是Call<T>类型
        check(getRawType(returnType) == Call::class.java) { "$returnType must be retrofit2.Call." }
        check(returnType is ParameterizedType) { "$returnType must be ParameterizedType.RawType are not supported. " }
        //取出Call<T>里的T，检查是否是BaseResponse<T>类型
        val baseResponseType = getParameterUpperBound(0, returnType)
        check(getRawType(baseResponseType) == BaseResponse::class.java) { "$baseResponseType must be BaseResponse" }
        check(baseResponseType is ParameterizedType) { "$baseResponseType must be parameterizedType.RawType are not supported" }
        //取出BaseResponse<T>里的T类型 也就是API返回数据对应的数据类型
        val dataType = getParameterUpperBound(0, baseResponseType)
        return BaseResponseCallAdapter<Any>(dataType)
    }
}
