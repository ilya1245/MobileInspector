package com.midway.mobileinspector.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.PowerManager;

import com.midway.mobileinspector.provider.FusedLocationProvider;
import com.midway.mobileinspector.service.ControlService;

/**
 * Created by ilya on 19.03.16.
 */
public class DeviceStatusHelper {
    private static Context context = ControlService.getContext();
    private static PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    private static FusedLocationProvider lp = ControlService.getLocationManager();

    public static boolean isScreenOn() {
        return pm.isScreenOn();
    }

    public static float getBatteryLevel() {
        Intent batteryIntent = ControlService.getContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return -1f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }

    public static boolean isNetworkLocationAvailable() {
        return lp.isNetworkLocationAvailable();
    }

    public static boolean isGpsLocationAvailable() {
        return lp.isGpsLocationAvailable();
    }
}
