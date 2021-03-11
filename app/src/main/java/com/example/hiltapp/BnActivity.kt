package com.example.hiltapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hiltapp.util.FragmentUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

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

    private var showFragment: Fragment? = null

    private var cacheFragments: HashMap<String, Fragment>? = null

    private var currentSelect: Int? = null

    private var clazzs =
        arrayOf(MainFragment::class.java, SecondFragment::class.java, ThreeFragment::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bn)
        FragmentUtil.init(this)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        //1.先设置监听防止设置默认选中item时不走该方法
        bottomNavigationView.setOnNavigationItemSelectedListener {
            Log.d(this.javaClass.name, "setOnNavigationItemSelectedListener")
            when (it.itemId) {
                R.id.main_menu -> {
                    showFragment = cacheFragments?.get(MainFragment::class.qualifiedName!!)
                }
                R.id.second_menu -> {
                    showFragment = cacheFragments?.get(SecondFragment::class.qualifiedName!!)
                }
                R.id.three_menu -> {
                    showFragment = cacheFragments?.get(ThreeFragment::class.qualifiedName!!)
                }
            }
            //2.记录当前选中的条目id
            currentSelect = it.itemId
            //3.切换fragment的方法
            FragmentUtil.switchFragment(showFragment!!)
            return@setOnNavigationItemSelectedListener true
        }
        //4.判断savedInstanceState是否为空，不为空则取出在Activity被系统回收时，保存的fragment的实例，并保存在自己的HashMap中
        cacheFragments = FragmentUtil.getFragmentMaps(clazzs)
        Log.d(this.javaClass.simpleName,cacheFragments.toString())
        FragmentUtil.handleShowHide(savedInstanceState) {
            if (savedInstanceState != null) {
                //6.获取activity被回收时，自己保存的选中item的id
                val currentSelectItemId = savedInstanceState.getInt("currentItemId")
                currentSelectItemId?.let {
                    bottomNavigationView.selectedItemId = it
                }
            } else {
                bottomNavigationView.selectedItemId = R.id.main_menu
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //9.保存activity被回收时，当前选中的itemid
        currentSelect?.let { outState.putInt("currentItemId", it) }
    }


}