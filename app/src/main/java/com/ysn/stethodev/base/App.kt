package com.ysn.stethodev.base

import android.app.Application
import com.facebook.stetho.Stetho
import com.ysn.stethodev.database.WaktuDb

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialization Stetho
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )
    }

}