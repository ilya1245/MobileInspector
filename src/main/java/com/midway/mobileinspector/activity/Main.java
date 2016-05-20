package com.midway.mobileinspector.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.midway.mobileinspector.service.ControlService;

/**
 * Created by ilya on 13.02.16.
 */
public class Main extends Activity {
    private static final String TAG = Main.class.getSimpleName();
    private static boolean firsRun = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (firsRun) {
                startService(new Intent(this, ControlService.class));
                firsRun = false;
            }
        } finally {
            //PackageManager pm = getApplicationContext().getPackageManager();
            //pm.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            finish();
        }
    }

    public static void setFirsRun(boolean firsRun) {
        Main.firsRun = firsRun;
    }
}
