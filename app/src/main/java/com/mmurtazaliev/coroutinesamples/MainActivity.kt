package com.mmurtazaliev.coroutinesamples

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    private val formatter = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
    private val scope = CoroutineScope(Job())

    private lateinit var job1: Job
    private lateinit var arrayList: Array<String?>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnRunOne).setOnClickListener { runOne() }
        findViewById<Button>(R.id.btnRunTwo).setOnClickListener { runTwo() }
    }


    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        scope.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun runOne() {

        log("runOne start")
        scope.launch {
            log("pc start")
            val deferred1 = async {
                val start = System.currentTimeMillis()
                arrayList = arrayOfNulls(100000000)
                val end = System.currentTimeMillis()
                val result = (end - start)
                result
            }
            val deferred2 = async(start = CoroutineStart.LAZY) {
                val start = System.currentTimeMillis()
                for (i in arrayList.indices) {
                    arrayList[i] = "Oops"
                }
                val end = System.currentTimeMillis()
                val result = end - start
                result

            }
            val x = deferred1.await()
            log("x=$x")

            deferred2.start()
            val y = deferred2.await()
            log("y=$y")

            log("pc end")
        }
        arrayList = emptyArray()
        log("runOne end")
    }

    private fun runTwo() {
    }

    private fun log(text: String) {
        Log.i("QWERTY", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }
}

class myClass() {

}