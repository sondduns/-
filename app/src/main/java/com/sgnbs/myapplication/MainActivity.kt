package com.sgnbs.myapplication

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.sgnbs.myapplication.databinding.ActivityMainBinding
import com.sgnbs.myapplication.entities.User
import com.xlx.room.dao.WorkDao
import com.xlx.room.entities.WorkTime
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var workDao: WorkDao
    private lateinit var todayWork: WorkTime
    private lateinit var binding: ActivityMainBinding

    private val format = SimpleDateFormat("HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this

        workDao = MyApplication.getDataBase().workDao()
        getToday()
    }

    private fun getToday() {
        Thread {
            val date = Date()
            val calendar = Calendar.getInstance();
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            todayWork = workDao.getToday(year, month, day)
            if (todayWork == null) {
                todayWork = WorkTime()
                todayWork.month = month
                todayWork.year = year
                todayWork.day = day
                workDao.insert(todayWork)
                todayWork = workDao.getToday(year, month, day)
            }

            runOnUiThread { binding.work = todayWork }
        }.start()

    }


    fun onclick() {
        val date = Date()
        val calendar = Calendar.getInstance();
        calendar.time = date

        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val time = format.format(date)
        if (hour < 12) {
            if (todayWork.onTime == null) {
                todayWork.onTime = time
                update()
                on.text = String.format(getString(R.string.on), onTime(time))
                toast("打卡成功")
            }else {
                toast("今日已打卡")
            }
        }else {
            todayWork.offTime = time
            update()
            off.text = String.format(getString(R.string.off), time)
            toast("打卡成功")
        }
    }


    private fun onTime(time: String) : String{
        val d1 = format.parse("08:00:00")
        val dNow = format.parse(time)
        if (dNow.before(d1)) {
            return format.format(d1)
        }else {
            return ""
        }
    }

    private fun update() {
        Thread{
            workDao.update(todayWork)
        }.start()
    }
}


fun Activity.toast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}
