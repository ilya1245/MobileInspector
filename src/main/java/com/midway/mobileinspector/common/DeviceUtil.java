package com.midway.mobileinspector.common;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.midway.mobileinspector.service.ControlService;

/**
 * Created by ilya on 12.03.16.
 */
public class DeviceUtil {

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
}
