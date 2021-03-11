package com.example.hiltapp.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp
 * @ClassName:      HiltApp
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2020/12/23 9:10
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/12/23 9:10
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
@HiltAndroidApp
class HiltApp : Application() {
    override fun onCreate() {
        super.onCreate()
        /*TODO:初始化一些三方sdk*/
    }
}