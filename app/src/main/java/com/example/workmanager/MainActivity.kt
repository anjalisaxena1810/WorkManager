package com.example.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.mbms.DownloadRequest
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.workmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.btnStart.setOnClickListener {
//            OneTimeWorkRequest()
            periodicWorkRequest()
        }
    }

    private fun OneTimeWorkRequest(){
        val workmanager = WorkManager.getInstance(applicationContext)
        val constraints =Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val data:Data = Data.Builder().putInt("data",200).build()

        val downloadingRequest=OneTimeWorkRequest.Builder(Downloadingworker::class.java).setConstraints(constraints)
            .setInputData(data).build()

        val uploadingRequest = OneTimeWorkRequest.Builder(Uploadingworker::class.java).build()
        val filteringRequest = OneTimeWorkRequest.Builder(Filteringworker::class.java).build()
        val CompressingRequest = OneTimeWorkRequest.Builder(Compressingworker::class.java).build()


        val paralleWorkRequest= mutableListOf<OneTimeWorkRequest>()
        paralleWorkRequest.add(filteringRequest)
        paralleWorkRequest.add(CompressingRequest)

        workmanager.beginWith(paralleWorkRequest)
            .then(uploadingRequest).then(downloadingRequest).enqueue()


//        workmanager.beginWith(filteringRequest).then(CompressingRequest).then(uploadingRequest).then(downloadingRequest).enqueue()

        workmanager.enqueue(downloadingRequest)
        workmanager.getWorkInfoByIdLiveData(downloadingRequest.id).observe(this, Observer {
            binding.textview.text = it.state.name
            if(it.state.isFinished){
                binding.textview.text = it.outputData.getString("DATE")
            }
        })
    }

    private fun periodicWorkRequest(){
        val  periodicWorkRequest = PeriodicWorkRequest.Builder(Downloadingworker::class.java,16,TimeUnit.MINUTES)
        WorkManager.getInstance(this@MainActivity).enqueue(periodicWorkRequest)
    }
}