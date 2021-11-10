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
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

        val scope = CoroutineScope(Dispatchers.Main + User("Masha", 100, 23))
        scope.toLog("scope")

        scope.launch {
            this.toLog("c1")
            launch(Dispatchers.Default) {
                this.toLog("c2")
                launch {
                    this.toLog("c3")
                }
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
        log("{ $str job=${this.coroutineContext[Job]}, dispatcher=${this.coroutineContext[ContinuationInterceptor]}, user=${this.coroutineContext[User] ?: 0}")
    }
}

data class User(
    val name: String,
    val id: Int,
    val age: Int
) : AbstractCoroutineContextElement(User) {
    companion object Key : CoroutineContext.Key<User>
}