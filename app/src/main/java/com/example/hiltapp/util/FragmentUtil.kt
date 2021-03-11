package com.example.hiltapp.util

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.hiltapp.R
import kotlin.reflect.KClass

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

        private var fragmentManager: FragmentManager? = null


        fun init(activity: FragmentManager) {
            this.fragmentManager = activity
        }

        /**
         * 处理Activity因内存不足被回收或者屏幕旋转时Activity重建导致Activity重叠的方法
         * @param savedInstanceState
         * @param setSelectItemId 传入选择默认选择tab方法
         */
        fun handleShowHide(savedInstanceState: Bundle?, setSelectItemId: () -> Unit) {
            if (fragmentManager == null) {
                throw ClassNotFoundException("please call init fun in start")
            }
            if (savedInstanceState != null) {
                Log.d(this.javaClass.simpleName, "savedInstanceState not null")
                val fragments = fragmentManager!!.fragments
                for (fragment in fragments) {
                    Log.d(this.javaClass.simpleName, fragment::class.qualifiedName!!)
                    //5.判断该fragment是否被添加，如果没有被添加判断是否隐藏，如果没有隐藏，则隐藏
                    //目的是隐藏所有可见的fragment，重新管理显示隐藏状态
                    if (fragment.isAdded) {
                        fragmentManager!!.beginTransaction().hide(fragment).commit()
                    }
                }
                //6.获取activity被回收时，自己保存的选中item的id
                setSelectItemId.invoke()
            } else {
                //7.如果是第一次创建，创建出所有的fragment，并添加到自己的hashMap中
                Log.d(this.javaClass.simpleName, "savedInstanceState is null")
                //8.设置默认选中的item
                setSelectItemId.invoke()
            }

        }


        /**
         *切换fragment,以show/hide的方式
         * @param activity activity实例
         * @param needShowFragment 需要显示的fragment实例
         */
        fun switchFragment(needShowFragment: Fragment) {
            if (fragmentManager == null) {
                throw ClassNotFoundException("please call init fun in start")
            }
            val beginTransaction = fragmentManager!!.beginTransaction()
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

        /**
         * 根据传入的Class数组，获取对应的fragment实例，并保存在HashMap中
         * @param clazzs class类型的数组
         * @return 存储fragment实例的HashMap
         */
        fun getFragmentMaps(clazzs: Array<KClass<out Fragment>>): HashMap<String, Fragment> {
            var clazzMap: HashMap<String, Fragment> = HashMap()
            clazzs.forEach {
                clazzMap.put(it.qualifiedName!!, getFragmentInstance(it))
            }
            return clazzMap
        }

        /**
         * 根据对应的class类型获取对应的fragment实例，并存储在HashMap中
         */
        private fun getFragmentInstance(clazz: KClass<out Fragment>): Fragment {
            if (fragmentManager == null) {
                throw ClassNotFoundException("please call init fun in start")
            }
            //根据类的全路径名，获取fragment实例
            var findMainFragmentByTag =fragmentManager!!.findFragmentByTag(clazz.qualifiedName)
            //如果获取不到，反射类的构造方法，并创建fragment实例
            if (findMainFragmentByTag == null) {
                val constructor = clazz.java.getConstructor()
                findMainFragmentByTag = constructor.newInstance() as Fragment
            }
            return findMainFragmentByTag
        }
    }


}