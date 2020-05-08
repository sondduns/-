package com.sgnbs.myapplication

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.sgnbs.myapplication.databinding.ActivityMainBinding
import com.sgnbs.myapplication.entities.User

class MainActivity : AppCompatActivity() {
    val user = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this
        user.name.set("张三")
        user.age.set(20)
        binding.user = user

    }

    fun onclick() {
        Thread {
            user.name.set("李四")
        }.start()
        Toast.makeText(this, "aaa", Toast.LENGTH_LONG).show()
    }


}
