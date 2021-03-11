package com.example.hiltapp

import androidx.fragment.app.Fragment
import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.KClass

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testClassName(){

        val clazz=MainFragment::class
//        getClassName(MainFragment::class.java)
        getClassName(clazz)
    }

    private fun getClassName(clazz: KClass<out Fragment>) {
        System.out.println(clazz.qualifiedName)
    }
//    private fun getClassName(clazz: Class<out Fragment>){
//        System.out.println(clazz::class.java.name)
//    }

}