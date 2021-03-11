package com.example.hiltapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hiltapp.util.FragmentUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.reflect.ParameterizedType

/**
 *
 * @ProjectName:    HiltApp
 * @Package:
 * @ClassName:      com.example.hiltapp.BnActivity
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2021/3/9 11:20
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/3/9 11:20
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class BnActivity : AppCompatActivity() {


    private var mainFragment: MainFragment? = null
    private var secondFragment: SecondFragment? = null
    private var threeFragment: ThreeFragment? = null

    private var showFragment: Fragment? = null

    private var cacheFragments: HashMap<String, Fragment> = HashMap<String, Fragment>()

    private var currentSelect: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bn)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        //1.先设置监听防止设置默认选中item时不走该方法
        bottomNavigationView.setOnNavigationItemSelectedListener {
            Log.d(this.javaClass.name, "setOnNavigationItemSelectedListener")
            when (it.itemId) {
                R.id.main_menu -> {
                    showFragment = cacheFragments.get(MainFragment::class.qualifiedName!!)
                }
                R.id.second_menu -> {
                    showFragment = cacheFragments.get(SecondFragment::class.qualifiedName!!)
                }
                R.id.three_menu -> {
                    showFragment = cacheFragments.get(ThreeFragment::class.qualifiedName!!)
                }
            }
            //2.记录当前选中的条目id
            currentSelect = it.itemId
            //3.切换fragment的方法
            FragmentUtil.switchFragment(this, showFragment!!)
            return@setOnNavigationItemSelectedListener true
        }
        //4.判断savedInstanceState是否为空，不为空则取出在Activity被系统回收时，保存的fragment的实例，并保存在自己的HashMap中
        if (savedInstanceState != null) {

            Log.d(this.javaClass.simpleName, "savedInstanceState not null")

//            getFragmentInstance(MainFragment::class.java)
//            getFragmentInstance(SecondFragment::class.java)
//            getFragmentInstance(ThreeFragment::class.java)
            val findMainFragmentByTag =
                supportFragmentManager.findFragmentByTag(MainFragment::class.qualifiedName)
            if (findMainFragmentByTag == null) {
                mainFragment = MainFragment.getInstance()
            } else {
                mainFragment = findMainFragmentByTag as MainFragment
            }
            cacheFragments.put(MainFragment::class.java.name, mainFragment!!)

            val findSecondFragmentByTag =
                supportFragmentManager.findFragmentByTag(SecondFragment::class.qualifiedName)
            if (findSecondFragmentByTag == null) {
                secondFragment = SecondFragment.getInstance()
            } else {
                secondFragment = findSecondFragmentByTag as SecondFragment
            }
            cacheFragments.put(SecondFragment::class.java.name, secondFragment!!)

            val findThreeFragmentByTag =
                supportFragmentManager.findFragmentByTag(ThreeFragment::class.qualifiedName)
            if (findThreeFragmentByTag == null) {
                threeFragment = ThreeFragment.getInstance()
            } else {
                threeFragment = findThreeFragmentByTag as ThreeFragment
            }
            cacheFragments.put(ThreeFragment::class.java.name, threeFragment!!)


            val fragments = supportFragmentManager.fragments
            for (fragment in fragments) {
                Log.d(this.javaClass.simpleName, fragment::class.qualifiedName!!)
                //5.判断该fragment是否被添加，如果没有被添加判断是否隐藏，如果没有隐藏，则隐藏
                //目的是隐藏所有可见的fragment，重新管理显示隐藏状态
                if (fragment.isAdded) {
                    supportFragmentManager.beginTransaction().hide(fragment).commit()
                }
            }
            //6.获取activity被回收时，自己保存的选中item的id
            val currentSelectItemId = savedInstanceState.getInt("currentItemId")
            currentSelectItemId?.let {
                bottomNavigationView.selectedItemId = it
            }
        } else {
            //7.如果是第一次创建，创建出所有的fragment，并添加到自己的hashMap中
            Log.d(this.javaClass.simpleName, "savedInstanceState is null")
            mainFragment = MainFragment.getInstance()
            secondFragment = SecondFragment.getInstance()
            threeFragment = ThreeFragment.getInstance()
            Log.d(this.javaClass.simpleName, MainFragment::class.qualifiedName!!)
            Log.d(this.javaClass.simpleName, SecondFragment::class.qualifiedName!!)
            Log.d(this.javaClass.simpleName, ThreeFragment::class.qualifiedName!!)

            cacheFragments.put(MainFragment::class.qualifiedName!!, mainFragment!!)
            cacheFragments.put(SecondFragment::class.qualifiedName!!, secondFragment!!)
            cacheFragments.put(ThreeFragment::class.qualifiedName!!, threeFragment!!)
            //8.设置默认选中的item
            bottomNavigationView.selectedItemId = R.id.main_menu
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //9.保存activity被回收时，当前选中的itemid
        currentSelect?.let { outState.putInt("currentItemId", it) }
    }


    private fun <T> getFragmentInstance(clazz: Class<T>): Fragment {
        var findMainFragmentByTag =
            supportFragmentManager.findFragmentByTag(clazz::class.qualifiedName)
        if (findMainFragmentByTag == null) {
            val constructor = clazz.getConstructor()
            findMainFragmentByTag = constructor.newInstance() as Fragment
        }
        cacheFragments.put(clazz.name, findMainFragmentByTag!!)
        return findMainFragmentByTag
    }


}