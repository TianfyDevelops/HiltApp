package com.example.hiltapp

import android.os.Bundle
import android.view.MenuItem
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
class BnActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var needShowFragment: Fragment? = null
    private val clazz = arrayOf(
        MainFragment::class.java.name,
        SecondFragment::class.java.name,
        ThreeFragment::class.java.name
    )

    private var fragmentHashMap: HashMap<String, Fragment>? = null
    private var fragmentUtil: FragmentUtil? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bn)

        initBottomNavigationFragment(savedInstanceState)

    }

    private fun initBottomNavigationFragment(savedInstanceState: Bundle?) {
        //获取FragmentUtil实例
        fragmentUtil = FragmentUtil.newInstance(this, R.id.main_menu)
        //获取缓存Fragment实例的HashMap
        fragmentHashMap = fragmentUtil!!.getFragmentHashMap(savedInstanceState, clazz)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        //设置默认选中的条目id
        fragmentUtil!!.setSelectItemId(savedInstanceState) { itemId: Int ->
            bottomNavigationView.selectedItemId = itemId
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //保存当前选中的条目id
        fragmentUtil!!.saveSelectItemId(outState)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        needShowFragment = when (item.itemId) {
            R.id.main_menu -> {
                fragmentHashMap?.get(MainFragment::class.java.name)
            }
            R.id.second_menu -> {
                fragmentHashMap?.get(SecondFragment::class.java.name)
            }
            R.id.three_menu -> {
                fragmentHashMap?.get(ThreeFragment::class.java.name)
            }
            else -> {
                fragmentHashMap?.get(MainFragment::class.java.name)
            }
        }
        //切换到要显示的fragment
        fragmentUtil!!.switchFragment(item.itemId, R.id.fragment_container_view, needShowFragment)
        return true
    }


}