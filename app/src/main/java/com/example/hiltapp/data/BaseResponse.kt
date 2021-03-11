package com.example.hiltapp.data

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp.data
 * @ClassName:      BaseResponse
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2020/12/23 10:17
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/12/23 10:17
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
sealed class BaseResponse<out T> {
    data class LoadSuccess<out T>(val data: T) : BaseResponse<T>()
    data class LoadFail(val errorCode: Int, val errorMsg: String) : BaseResponse<Nothing>()
}