package com.example.hiltapp.ui

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiltapp.api.RetrofitService
import com.example.hiltapp.data.BaseResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp.ui
 * @ClassName:      MainActivityViewModel
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2021/2/24 16:39
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/2/24 16:39
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MainActivityViewModel @ViewModelInject constructor(private val retrofitService: RetrofitService) :
    ViewModel() {

    private val _isRegister: MutableLiveData<Boolean> = MutableLiveData()

    val isRegister: LiveData<Boolean> get() = _isRegister


    fun register(userName: String, passWord: String, rePassWord: String) {
        viewModelScope.launch {
            when (val register = retrofitService.register(userName, passWord, rePassWord)) {
                is BaseResponse.LoadSuccess -> {
                    _isRegister.value = true
                    Log.d(TAG,register.toString())
                }
                is BaseResponse.LoadFail -> {
                    _isRegister.value = false
                    Log.d(TAG,register.toString())
                }
            }
        }
    }

    companion object {
        private const val TAG: String ="MainActivityViewModel"
    }


}