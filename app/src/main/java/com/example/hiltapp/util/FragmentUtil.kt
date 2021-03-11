package com.example.hiltapp.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hiltapp.MainFragment
import com.example.hiltapp.R
import com.example.hiltapp.SecondFragment
import com.example.hiltapp.ThreeFragment

/**
 *
 * @ProjectName:    HiltApp
 * @Package:        com.example.hiltapp.util
 * @ClassName:      FragmentUtil
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2021/3/9 11:02
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/3/9 11:02
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class FragmentUtil {

    companion object {

        private var currentShowFragment: Fragment? = null

        /**
         *切换fragment,以show/hide的方式
         * @param activity activity实例
         * @param needShowFragment 需要显示的fragment实例
         */
        fun switchFragment(activity: AppCompatActivity, needShowFragment: Fragment) {
            val beginTransaction = activity.supportFragmentManager.beginTransaction()
            //判断当前fragment是否为空，如果为空，添加需要显示的fragment并提交事务
            if (currentShowFragment == null) {
                beginTransaction.add(
                    R.id.fragment_container_view,
                    needShowFragment,
                    needShowFragment::class.qualifiedName!!
                ).commit()
            } else {
                //判断当前需要显示fragment是否已经被添加，如果被添加判断是否隐藏，如果隐藏，让其显示，并隐藏当前显示的fragment,并提交事务
                if (needShowFragment.isAdded) {
                    if (needShowFragment.isHidden) {
                        beginTransaction.show(needShowFragment).hide(currentShowFragment!!).commit()
                    }
                } else {
                    //如果当前fragment不为null,隐藏当前fragment，并显示需要显示的fragment,并提交事务
                    beginTransaction.hide(currentShowFragment!!)
                        .add(
                            R.id.fragment_container_view,
                            needShowFragment,
                            needShowFragment::class.qualifiedName!!
                        ).commit()
                }
            }
            //保存当前显示的fragment
            currentShowFragment = needShowFragment
        }
    }


}