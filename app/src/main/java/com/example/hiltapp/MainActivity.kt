package com.example.hiltapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.hiltapp.ui.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUserName = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etRePassword = findViewById<EditText>(R.id.et_repassword)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnRegister = findViewById<Button>(R.id.btn_register)



        btnLogin.setOnClickListener {


        }

        btnRegister.setOnClickListener {
            val userName = etUserName.text.toString()
            val password = etPassword.text.toString()
            val repassword = etRePassword.text.toString()
            if (userName.isEmpty()) {
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            mainActivityViewModel.register(userName, password, repassword)
        }

        mainActivityViewModel.isRegister.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "注册失败", Toast.LENGTH_LONG).show()
            }

        })


    }
}


