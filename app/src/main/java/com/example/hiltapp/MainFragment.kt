package com.example.hiltapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp
 * @ClassName:      MainFragment
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2021/3/9 11:05
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/3/9 11:05
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MainFragment : Fragment() {
    companion object {
        fun getInstance(): MainFragment {
            return MainFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(this.javaClass.simpleName,"onCreateView")
        return inflater.inflate(R.layout.fragment_main, container,false)
    }

}