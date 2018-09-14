package com.ysn.stethodev.ui

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ysn.stethodev.R
import com.ysn.stethodev.database.WaktuDb
import com.ysn.stethodev.database.entities.WaktuData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_shared_preferences.setOnClickListener {
            val sharedPreferences = getSharedPreferences("DATA_PREF", Context.MODE_PRIVATE)
            val waktu = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
                    .format(Date())
            sharedPreferences.edit()
                    .putString("waktu", waktu)
                    .apply()
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Please wait")

        button_network.setOnClickListener { _ ->
            progressDialog.show()
            Observable
                    .create<Boolean> { emitter ->
                        val request = Request.Builder()
                                .url("https://jsonplaceholder.typicode.com/posts")
                                .addHeader("Accept", "application/json")
                                .addHeader("Content-Type", "application/json")
                                .build()
                        OkHttpClient.Builder()
                                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                                .addNetworkInterceptor(StethoInterceptor())
                                .build()
                                .newCall(request)
                                .enqueue(object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        emitter.onError(e)
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        emitter.onNext(true)
                                        emitter.onComplete()
                                    }
                                })
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                progressDialog.dismiss()
                            },
                            {
                                progressDialog.dismiss()
                                it.printStackTrace()
                            },
                            {
                                /* nothing to do in here */
                            }
                    )
        }

        button_database.setOnClickListener { it ->
            val waktuDb: WaktuDb = WaktuDb.getInstance(this@MainActivity)!!
            Observable
                    .create<Boolean> { emitter ->
                        val strWaktu = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
                                .format(Date())
                        val waktuData = WaktuData(null, strWaktu)
                        waktuDb.waktuDao()
                                .insert(waktuData)
                        emitter.onNext(true)
                        emitter.onComplete()
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                Toast.makeText(this@MainActivity, "Data has been saved", Toast.LENGTH_SHORT)
                                        .show()
                            },
                            {
                                it.printStackTrace()
                                Toast.makeText(this@MainActivity, "Error occured", Toast.LENGTH_SHORT)
                                        .show()
                            },
                            {
                                /* nothing to do in here */
                            }
                    )
        }
    }
}
