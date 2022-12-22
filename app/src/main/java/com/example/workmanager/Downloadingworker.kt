package com.example.workmanager

import android.content.Context

import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class Downloadingworker(val context:Context, val params:WorkerParameters): Worker(context,params) {
    override fun doWork(): Result {
       return try {
           val getData=inputData.getInt("data",0)
            for (i in 1..getData) {
                Log.d("WorkerTask", "Downloading:$i")
            }
           val date = SimpleDateFormat("dd/M/yy:mm:ss")
           val currentTime = date.format(Date())
           val data = Data.Builder().putString("DATE",currentTime).build()
             Result.success(data)
        }catch (e:Exception){
           val data = Data.Builder().putString("DATE",e.message).build()
             Result.failure(data)
        }
    }

}