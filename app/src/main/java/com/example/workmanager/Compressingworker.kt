package com.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class Compressingworker(val context: Context, val params: WorkerParameters): Worker(context,params) {
    override fun doWork(): Result {
        return try {
            for (i in 1..3000) {
                Log.d("WorkerTask", "Compressing:$i")
            }
            Result.success()
        }catch (e:Exception){
            Result.failure()

        }
    }
}