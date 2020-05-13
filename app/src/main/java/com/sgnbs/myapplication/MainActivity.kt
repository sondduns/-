package com.sgnbs.myapplication

import android.app.Activity
import android.app.TimePickerDialog
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.sgnbs.myapplication.databinding.ActivityMainBinding
import com.sgnbs.myapplication.entities.User
import com.xlx.room.dao.WorkDao
import com.xlx.room.entities.WorkTime
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var workDao: WorkDao
    private lateinit var todayWork: WorkTime
    private lateinit var binding: ActivityMainBinding

    private val format = SimpleDateFormat("HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this

        workDao = (application as MyApplication).dataBase.workDao()
        getToday()
    }

    private fun getToday() {
        Thread {
            val date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val workTime = workDao.getToday(year, month, day)
            if (workTime == null) {
                todayWork = WorkTime()
                todayWork.month = month
                todayWork.year = year
                todayWork.day = day
                workDao.insert(todayWork)
                todayWork = workDao.getToday(year, month, day)
            }else {
                todayWork = workTime
            }

            runOnUiThread {
                binding.work = todayWork
                setText()
            }
        }.start()
    }


    /**
     * 打卡
     */
    fun onclick() {
        val date = Date()
        val calendar = Calendar.getInstance();
        calendar.time = date

        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val time = format.format(date)
        if (hour < 12) {
            if (todayWork.onTime == null) {
                todayWork.onTime = time
                update(0, time)

                toast("打卡成功")
            }else {
                toast("今日已打卡")
            }
        }else {
            update(1, time)
            toast("打卡成功")
        }
    }

    private fun setText() {
        if(todayWork.onTime != null) {
            on.text = String.format(getString(R.string.on), todayWork.onTime)
        }
        if(todayWork.offTime != null) {
            off.text = String.format(getString(R.string.off), todayWork.offTime)
        }
        if(todayWork.workHour != 0f) {
            work_time.text = String.format(getString(R.string.work_hour), todayWork.workHour)
        }
        if(todayWork.diffHour != 0f) {
            work_diff.text = String.format(getString(R.string.work_diff), todayWork.diffHour)
        }
    }


    private fun onTime(time: String) : String{
        val d1 = format.parse("08:00")
        val dNow = format.parse(time)
        return if (dNow != null && d1 != null && dNow.before(d1)) {
            format.format(d1)
        }else {
            time
        }
    }

    /**
     * 更新上下班时间并存储
     * type 0上班  1下班
     */
    private fun update(type: Int, time: String) {
        when(type) {
            0 -> {
                todayWork.onTime = onTime(time)
                on.text = String.format(getString(R.string.on), onTime(time))
            }
            1 -> {
                todayWork.offTime = time
                off.text = String.format(getString(R.string.off), time)
            }
        }
        getWorkTime()
        update()
    }

    private fun update() {
        Thread{
            workDao.update(todayWork)
        }.start()
    }

    fun onTake(type: Int) {
        val picker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val time = "$hourOfDay:$minute:00"
            update(type, time)
        }, 0, 0, true)
        picker.show()
    }

    /**
     * 计算工时
     */
    private fun getWorkTime() {
        if (!todayWork.onTime.isNullOrEmpty() && !todayWork.offTime.isNullOrEmpty()) {
            val start = todayWork.onTime.split(":")
            val end = todayWork.offTime.split(":")

            var hour = end[0].toFloat() - start[0].toFloat()
            val min = end[1].toBigDecimal().subtract(start[1].toBigDecimal()).divide(BigDecimal(60),2,BigDecimal.ROUND_HALF_DOWN).toFloat()
            hour += min

            val diff = hour.toBigDecimal().subtract(BigDecimal(8)).toFloat()
            todayWork.workHour = hour
            todayWork.diffHour = diff
            work_time.text = String.format(getString(R.string.work_hour), hour.toString())
            work_diff.text = String.format(getString(R.string.work_diff), diff.toString())
        }
    }
}


fun Activity.toast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}
