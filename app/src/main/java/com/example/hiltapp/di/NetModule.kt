package com.example.hiltapp.di

import android.content.Context
import android.os.Build
import com.example.hiltapp.api.RetrofitService
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp
 * @ClassName:      NetModule
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2020/12/23 9:15
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/12/23 9:15
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
@Module
@InstallIn(ApplicationComponent::class)
object NetModule {
    private const val baseUrl: String = "https://www.wanandroid.com/"
    /**
     * 提供httpclient实例
     */
    @Provides
    @Singleton
    fun provideHttpClient(cookieJar: PersistentCookieJar): OkHttpClient {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(Duration.ofMillis(2000))
                .readTimeout(Duration.ofMillis(2000))
                .addInterceptor(BusinessErrorInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .writeTimeout(Duration.ofMillis(2000))
                .build()
        } else {
            OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .writeTimeout(2000, TimeUnit.MILLISECONDS)
                .addInterceptor(BusinessErrorInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        }
    }

    /**
     * 提供api实例
     */
    @Provides
    @Singleton
    fun provideRetrofitService(okHttpClient: OkHttpClient): RetrofitService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(BaseResponseCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }


    /**
     * 提供存储cookie的三方库实例
     */
    @Provides
    @Singleton
    fun provideCookiejar(@ApplicationContext context: Context): PersistentCookieJar {
        return PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    }


}