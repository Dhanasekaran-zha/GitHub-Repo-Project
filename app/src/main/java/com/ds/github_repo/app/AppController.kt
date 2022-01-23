package com.ds.github_repo.app

import android.app.Application
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.ds.github_repo.data.repo.AdminRepo
import com.ds.github_repo.utils.ConnectivityReceiver
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppController : Application() {

    companion object {
        var appController: AppController? = null

        fun getInstanse(): AppController? {
            return appController
        }
    }

    private var adminRepo: AdminRepo? = null
    private var gson: Gson? = null
    private var realmConfiguration: RealmConfiguration? = null
    private var realm:Realm?=null


    override fun onCreate() {
        super.onCreate()
        appController = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        Realm.init(this)
        realmConfiguration = RealmConfigurationFactory.getConfiguration()
        realm= Realm.getInstance(realmConfiguration)
        adminRepo=getAdminRepo()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
            this.registerReceiver(ConnectivityReceiver(), filter)
        }

    }


    fun getAdminRepo(): AdminRepo {
        if (adminRepo == null) adminRepo = createAdminRepo()
        return adminRepo!!
    }

    private fun createAdminRepo(): AdminRepo {
        val retrofit: Retrofit
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
            .addInterceptor { chain->
                val request: Request = chain.request()

                val newRequest: Request = request.newBuilder()
                    .addHeader(AppConstants.API_AURHORIZATION, AppConstants.TOKEN)
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
                .build()
        retrofit = Retrofit.Builder().client(httpClient)
                .baseUrl(ApiUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()

        return AdminRepo(retrofit.create(ApiInterface::class.java),realmConfiguration)
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

}