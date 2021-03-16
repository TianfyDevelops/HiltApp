package com.example.hiltapp.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * @ProjectName: BottomNavigationViewFragmentJava
 * @Package: com.example.bottomnavigationviewfragmentjava.util
 * @ClassName: FragmentUtil
 * @Description: 用于以show/hide的方式管理首页面bottomNavigation+fragment的切换
 * 解决了因内存不足，或者屏幕切换，引起的fragment重叠的问题
 * 屏幕切换时，需要在AndroidManifest.xml中配置android:configChanges="orientation"
 * @Author: tianfy
 * @CreateDate: 2021/3/15 20:43
 * @UpdateUser: 更新者：
 * @UpdateDate: 2021/3/15 20:43
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
class FragmentUtil private constructor(activity: AppCompatActivity, defaultSelectItemId: Int) {
    private val supportFragmentManager: FragmentManager

    /**
     * 当前显示的fragment实例
     */
    private var currentShowFragment: Fragment? = null

    /**
     * 默认选中的条目id
     */
    private val defaultSelectItemId: Int

    /**
     * 当前选中的条目id
     */
    private var currentSelectItemId = 0

    /**
     * 切换到要显示的fragment
     *
     * @param itemId                  传入选中的条目id
     * @param fragmentViewContainerId 装载fragment的容器id
     * @param needShowFragment        需要显示的fragment实例
     */
    fun switchFragment(itemId: Int, fragmentViewContainerId: Int, needShowFragment: Fragment?) {
        currentSelectItemId = itemId
        if (currentShowFragment == null) {
            if (needShowFragment!!.isAdded) {
                if (needShowFragment.isHidden) {
                    supportFragmentManager.beginTransaction().show(needShowFragment).commit()
                }
            } else {
                supportFragmentManager.beginTransaction()
                    .add(fragmentViewContainerId, needShowFragment, needShowFragment.javaClass.name)
                    .commit()
            }
        } else {
            if (needShowFragment!!.isAdded) {
                if (needShowFragment.isHidden) {
                    supportFragmentManager.beginTransaction()
                        .hide(currentShowFragment!!)
                        .show(needShowFragment)
                        .commit()
                }
            } else {
                supportFragmentManager.beginTransaction()
                    .hide(currentShowFragment!!)
                    .add(fragmentViewContainerId, needShowFragment, needShowFragment.javaClass.name)
                    .commit()
            }
        }
        currentShowFragment = needShowFragment
    }

    /**
     * 获取缓存fragment实例的HashMap，在setOnNavigationItemSelectedListener之前调用
     *
     *1.先判断savedInstanceState是否为null,如果不为null,从supportFragmentManager根据tag依次取出
     *已经缓存的fragment实例，如果实例不为null，缓存到HashMap中，如果为null，则创建fragment的实例
     *并缓存到HashMap中
     *2.savedInstanceState不为null在内存不足，Activity被系统回收的时候会发生，当Activity因内存不足被系统
     *回收，会调用Activity的SaveInstanceState方法，保存Fragment的实例和状态
     *3.在AndroidManifest.xml中配置onConfigChange="orientation"方法，Activity不会重新走生命周期，只会走
     *onConfigChange方法回调
     * @param savedInstanceState 保存实例状态的bundle
     * @param clazzs             要创建fragment实例的class对象的全类名
     * @return 缓存fragment实例的HashMap
     */
    fun getFragmentHashMap(
        savedInstanceState: Bundle?,
        clazzs: Array<String>
    ): HashMap<String, Fragment> {
        val fragmentHashMap = HashMap<String, Fragment>()

        if (savedInstanceState != null) {
            for (clazzName in clazzs) {
                val fragmentByTag = supportFragmentManager.findFragmentByTag(clazzName)
                if (fragmentByTag != null) {
                    fragmentHashMap[clazzName] = fragmentByTag
                } else {
                    val fragmentInstance = getFragmentInstance(clazzName)
                    if (fragmentInstance != null) {
                        fragmentHashMap[clazzName] = fragmentInstance
                    }
                }
            }
            //让所有fragment重置为hide状态
            val fragments = supportFragmentManager.fragments
            if (!fragments.isEmpty()) {
                for (fragment in fragments) {
                    supportFragmentManager.beginTransaction().hide(fragment!!).commit()
                }
            }
        } else {
            //如果savedInstanceState为null说明是首次创建，则依次创建fragment的实例，并保存在HashMap中
            for (clazzName in clazzs) {
                val fragmentInstance = getFragmentInstance(clazzName)
                if (fragmentInstance != null) {
                    fragmentHashMap[clazzName] = fragmentInstance
                }
            }
        }
        return fragmentHashMap
    }

    /**
     * 设置需要选中的条目id
     * 在setOnNavigationItemSelectedListener方法之后调用
     * @param savedInstanceState     保存实例状态的bundle
     * @param onSelectItemIdListener 选中条目id的监听
     */
    fun setSelectItemId(savedInstanceState: Bundle?, onSelectItemIdListener: (Int) -> Unit) {
        if (savedInstanceState != null) {
            val currentSelectItemId = savedInstanceState[currentSelectItemIdKey] as Int
            onSelectItemIdListener.invoke(currentSelectItemId)
        } else {
            onSelectItemIdListener.invoke(defaultSelectItemId)
        }
    }

    /**
     * 保存选中的条目id
     * 在Activity的onSaveInstanceState方法中调用
     *
     * @param outState bundle
     */
    fun saveSelectItemId(outState: Bundle) {
        outState.putInt(currentSelectItemIdKey, currentSelectItemId)
    }

    /**
     * 根据全类名获取实例对象
     *
     * @param clazzName 全类名
     * @return fragment实例
     */
    private fun getFragmentInstance(clazzName: String): Fragment? {
        try {
            val aClass = Class.forName(clazzName)
            val constructor = aClass.getConstructor()
            return constructor.newInstance() as Fragment
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        /**
         * 保存当前选中条目id的key
         */
        private const val currentSelectItemIdKey = "currentSelectItemIdKey"
        fun newInstance(activity: AppCompatActivity, defaultSelectItemId: Int): FragmentUtil {
            return FragmentUtil(activity, defaultSelectItemId)
        }
    }

    init {
        supportFragmentManager = activity.supportFragmentManager
        this.defaultSelectItemId = defaultSelectItemId
    }
}