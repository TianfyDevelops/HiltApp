package com.example.hiltapp.di

import okio.IOException

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp.di
 * @ClassName:      ApiException
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2021/1/19 10:30
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/1/19 10:30
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class ApiException(val errorCode: Int, val errorMsg: String) : IOException()