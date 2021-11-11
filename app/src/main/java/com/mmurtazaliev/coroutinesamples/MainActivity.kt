package com.mmurtazaliev.coroutinesamples

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    private val formatter = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
    private val scope = CoroutineScope(Job())

    private lateinit var job1: Job
    private lateinit var arrayList: Array<String?>
    private val context = Job() + Dispatchers.Default + User("Masha", 100, 23)
    private val myScope = CoroutineScope(context)

    //private val myScope = CoroutineScope(Job() + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnRunOne).setOnClickListener { }
        findViewById<Button>(R.id.btnRunTwo).setOnClickListener { }

        val scope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
        repeat(6) {
            scope.launch {
                this.toLog("c_${it}_start")
                TimeUnit.MILLISECONDS.sleep(1000)
                this.toLog("c_${it}_end")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(text: String) {
        Log.i("QWERTY", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }

    private fun CoroutineScope.toLog(str: String) {
        log("{ $str dispatcher=${this.coroutineContext[ContinuationInterceptor]}")
    }

    private suspend fun getData(): String {
        return suspendCoroutine {
            thread {
                TimeUnit.MILLISECONDS.sleep(2000)
                it.resume("Data")
            }
        }
    }
}

data class User(
    val name: String,
    val id: Int,
    val age: Int
) : AbstractCoroutineContextElement(User) {
    companion object Key : CoroutineContext.Key<User>
}