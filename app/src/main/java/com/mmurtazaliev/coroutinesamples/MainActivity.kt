package com.mmurtazaliev.coroutinesamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val formatter = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
    private val scope = CoroutineScope(Job())

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnRun).setOnClickListener { runFunc() }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { cancelFunc() }
    }


    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        scope.cancel()
    }

    private fun runFunc() {
        log("onRun, start")

        job = scope.launch {
            log("c start")
            var x = 0
            while (x < 5 && isActive) {
               delay(1000)
                log("c ${x++} isActive=$isActive")
            }
            log("c end")
        }

        log("onRun, end")
    }

    private fun cancelFunc() {
        log("onCancel")
        job.cancel()
    }

    private fun log(text: String) {
        Log.i("QWERTY", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }
}