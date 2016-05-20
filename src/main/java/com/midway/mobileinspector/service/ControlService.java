package com.midway.mobileinspector.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.midway.mobileinspector.activity.Main;
import com.midway.mobileinspector.common.Constants;
import com.midway.mobileinspector.common.PropertyLoadUtil;
import com.midway.mobileinspector.provider.FusedLocationProvider;
import com.midway.mobileinspector.thread.InspectorThread;
import com.midway.mobileinspector.thread.OCProcessThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by ilya on 13.02.16.
 */
public class ControlService extends Service {
    private static Context context;
    private static final Logger logger = LoggerFactory.getLogger(ControlService.class.getSimpleName());
    private static InspectorThread inspectorTread;
    private static OCProcessThread ocProcessThread;
    private static final String SCHEDULED_SERVICE_RESTART = "scheduled_service_restart";
    private static final String EXTRA_ORIGINAL_ACTION = "original_intent_action";
    private static final boolean ENABLE_RESTART = true;
    private static final int RESTART_DELAY = 5000;
    private static FusedLocationProvider locationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static String android_id;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Toast.makeText(this, "ControlService Created", Toast.LENGTH_LONG).show();
        logger.debug("onCreate");

        //createDirectory();
        PropertyLoadUtil.init();
        inspectorTread = new InspectorThread();
        inspectorTread.setPriority(Thread.MAX_PRIORITY);
        //ocProcessThread = new OCProcessThread();
        Main.setFirsRun(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "ControlService Started", Toast.LENGTH_LONG).show();
        logger.debug("onStartCommand");
        android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
        locationManager = FusedLocationProvider.getInstance(ControlService.getContext());
        locationManager.setListeningEnabled(true);
        //logger.info(ControlService.android_id);
        //ocProcessThread.start();
        inspectorTread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void createDirectory() {
        File tcpDumpDir = new File(Constants.TCPDUMP_FILE_DIR);
        File logcatDumpDir = new File(Constants.LOGCAT_FILE_DIR);
        if (!tcpDumpDir.mkdirs()) logger.error("Directory for tcp dump files was not created.");
        if (!logcatDumpDir.mkdirs()) logger.error("Directory for logcat files was not created.");
    }
    public static void startInspectorTread() {
        new Thread(inspectorTread).start();
    }

    /*
    @Override
    public void onDestroy() {
        Toast.makeText(this, "ControlService Destroyed", Toast.LENGTH_LONG).show();
        logger.debug("onDestroy");
        inspectorTread.setRun(false);
        AlarmManager mgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (mgr != null) {
            Intent restartServiceIntent = new Intent(SCHEDULED_SERVICE_RESTART, null, this, ControlService.class);
            restartServiceIntent.putExtra(EXTRA_ORIGINAL_ACTION, SCHEDULED_SERVICE_RESTART);
            PendingIntent pi = PendingIntent.getService(this, 2, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
            mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + RESTART_DELAY, pi);
            logger.info( "Restarting service in {} ms", RESTART_DELAY);
        } else {
            logger.info( "Will not attempt to restart service. AlarmManager: {} service_restart_enabled: {}", mgr, ENABLE_RESTART);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
    */
    public static Context getContext() {
        return context;
    }

    public static FusedLocationProvider getLocationManager() {
        return locationManager;
    }

   /* public static void setLocationManager(FusedLocationProvider locationManager) {
        ControlService.locationManager = locationManager;
    }*/
}
