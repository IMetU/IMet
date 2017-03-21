package com.example.imetu.imet.database;

import android.app.Application;
import android.content.res.Configuration;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class DBApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        Stetho.initializeWithDefaults(this);

    }
    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
