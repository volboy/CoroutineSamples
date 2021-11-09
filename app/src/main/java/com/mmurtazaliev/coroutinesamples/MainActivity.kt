package com.mmurtazaliev.coroutinesamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val formatter = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
    private val scope = CoroutineScope(Job())

    private lateinit var job1: Job

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

    private fun runOne() {

        log("runOne start")
        scope.launch {
            log("pc start")
            job1 = launch(start = CoroutineStart.LAZY) {
                log("c1 start")
                delay(3000)
                log("c1 end")
            }

            val job2 = launch {
                log("c2 start")
                delay(1500)
                log("c2 end")
            }
            log("pc wait")
            job2.join()
            log("pc end")
        }
        log("runOne end")
    }

    private fun runTwo() {
        job1.start()
    }

    private fun log(text: String) {
        Log.i("QWERTY", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }
}