package com.mmurtazaliev.coroutinesamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.concurrent.thread
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    private val formatter = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var job: Job? = null
    private lateinit var btnOne: Button
    private val exceptionHandler = CoroutineExceptionHandler{ context, exception ->
        log("error handled $exception")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnOne = findViewById<Button>(R.id.btnRunOne).also { it.setOnClickListener { onRun() } }
        findViewById<Button>(R.id.btnRunTwo).setOnClickListener { }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        log("onDestroy")
    }

    private fun onRun() {
        log("onRun start")

        scope.launch(exceptionHandler) {
            try {
                Integer.parseInt("a")
            } catch (e: Exception) {
                log("error $e")
            }
        }
        log("onRun ")
    }

    private fun log(text: String) {
        Log.i("QWERTY", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }

    private fun CoroutineScope.toLog(str: String) {
        log("{ $str dispatcher=${this.coroutineContext[ContinuationInterceptor]}")
    }

    private suspend fun getData(): String {
        return suspendCoroutine {
            log("suspend function start")
            thread {
                log("suspend function background work")
                TimeUnit.MILLISECONDS.sleep(2000)
                it.resume("Data")
            }
        }
    }
}