package com.example.hiltapp.di

import com.example.hiltapp.data.BaseResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp.di
 * @ClassName:      BaseResponseCall
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2021/1/19 10:16
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/1/19 10:16
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class BaseResponseCall<T>(private val delegate: Call<T>) : Call<BaseResponse<T>> {
    /**
     * 该方法会被Retrofit处理suspend方法的代码调用，并传进来一个callback,如果你回调了callback.onResponse，那么suspend方法就会成功返回
     * 如果你回调了callback.onFailure那么suspend方法就会抛异常
     *
     * 所以我们这里的实现是永远回调callback.onResponse,只不过在请求成功的时候返回的是BaseResponse.LoadSuccess对象，
     * 在失败的时候返回的是BaseResponse.LoadFail对象，这样外面在调用suspend方法的时候就不会抛异常，一定会返回BaseResponse.LoadSuccess 或 BaseResponse.LoadFail
     */
    override fun enqueue(callback: Callback<BaseResponse<T>>) {
        //delegate 是用来做实际的网络请求的Call<T>对象，网络请求的成功失败会回调不同的方法
        delegate.enqueue(object : Callback<T> {
            /**
             * 网络请求成功返回，会回调该方法（无论status code是不是200）
             */
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {//http status 是200+
                    //这里担心response.body()可能会为null(还没有测到过这种情况)，所以做了一下这种情况的处理，
                    // 处理了这种情况后还有一个好处是我们就能保证我们传给ApiResult.Success的对象就不是null，这样外面用的时候就不用判空了
                    val baseResult = if (response.body() == null) {
//                        LoadResult.LoadFail()
                        BaseResponse.LoadSuccess(response.body()!!)
                    } else {
                        BaseResponse.LoadSuccess(response.body()!!)
                    }
                    callback.onResponse(this@BaseResponseCall, Response.success(baseResult))
                } else {//http status错误
                    val failureApiResult = BaseResponse.LoadFail(ApiError.httpStatusCodeError.errorCode, ApiError.httpStatusCodeError.errorMsg)
                    callback.onResponse(this@BaseResponseCall, Response.success(failureApiResult))
                }
            }
            /**
             * 在网络请求中发生了异常，会回调该方法
             *
             * 对于网络请求成功，但是业务失败的情况，我们也会在对应的Interceptor中抛出异常，这种情况也会回调该方法
             */
            override fun onFailure(call: Call<T>, t: Throwable) {
                val failureApiResult = if (t is ApiException) {//Interceptor里会通过throw ApiException 来直接结束请求 同时ApiException里会包含错误信息
                    BaseResponse.LoadFail(t.errorCode, t.errorMsg)
                } else {
                    BaseResponse.LoadFail(ApiError.unknownException.errorCode, ApiError.unknownException.errorMsg)
                }

                callback.onResponse(this@BaseResponseCall, Response.success(failureApiResult))
            }

        })


    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }

    override fun clone(): Call<BaseResponse<T>> = BaseResponseCall(delegate.clone())

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun execute(): Response<BaseResponse<T>> {
        throw UnsupportedOperationException("BaseResponseCall does not support synchronous execution")
    }

    override fun request(): Request {
        return delegate.request()
    }
}