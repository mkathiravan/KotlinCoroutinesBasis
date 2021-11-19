package com.example.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {


    lateinit var scope: CoroutineScope
    private val JOB_TIME = 3000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClick.setOnClickListener {

            scope = CoroutineScope(Dispatchers.Main)

            startTask()
        }
    }


    //The following function used by async and await coroutines it will return the result.
    private fun startTask1()
    {
        scope.launch {

            var time = measureTimeMillis {

                val result : Deferred<String> = async {

                    println("DEBUG startTask ${Thread.currentThread().name}")

                    getDataFromNetwork()

                }

                updateUI(result.await())
            }
        }
    }


    //The following function is used by launch coroutines so it does not return any value.
    private fun startTask() {

        var result = "";

        scope.launch {

            var time = measureTimeMillis {

                println("DEBUG startTask ${Thread.currentThread().name}")

                result = getDataFromNetwork()

            }

            println("DEBUG Total Elapsed Time ${time}")


            updateUI(result)
        }
    }

    private suspend fun getDataFromNetwork(): String {

        //If the context is changing from main Thread to background thread we should use withContext.

        withContext(Dispatchers.IO){

            println("DEBUG getDataFromNetwork ${Thread.currentThread().name}")


            delay(JOB_TIME.toLong()
            )
        }

        return "Task Completed"

    }


    private suspend fun updateUI(message: String)
    {
        withContext(Dispatchers.Main)
        {
            println("Debug UpdateUI ${Thread.currentThread().name}")
            txt_result.text = txt_result.text.toString() + "\n " + message
        }
    }
}