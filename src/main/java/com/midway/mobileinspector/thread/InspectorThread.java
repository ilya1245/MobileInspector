package com.midway.mobileinspector.thread;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import com.midway.mobileinspector.common.Configuration;
import com.midway.mobileinspector.common.Constants;
import com.midway.mobileinspector.common.HttpHelper;
import com.midway.mobileinspector.common.Place;
import com.midway.mobileinspector.common.StringUtils;
import com.midway.mobileinspector.service.ControlService;
import com.midway.mobileinspector.system.ApplicationManager;
import com.midway.mobileinspector.util.DeviceStatusHelper;
import com.midway.mobileinspector.util.MobileNetworkHelper;
import com.midway.mobileinspector.util.TestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by ilya on 14.02.16.
 */
public class InspectorThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(InspectorThread.class.getSimpleName());
    private boolean run = true;
    MobileNetworkHelper mobileNetworkHelper = new MobileNetworkHelper(ControlService.getContext());
    //FusedLocationProvider locationManager = FusedLocationProvider.getInstance(ControlService.getContext());

    @Override
    public void run() {
        //locationManager.setListeningEnabled(true);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        do {
            String securityStatus = HttpHelper.getDeviceSecurity();
            if (StringUtils.isNotEmpty(securityStatus) && !Constants.SECURITY_STATUS_OK.equals(securityStatus)) {
                for (String appPackage : Constants.CLONE_ARRAY) {
                    try {
                        //ShellUtil.uninstallApplication(appPackage);
                        uninstallApp(appPackage);
                    } catch (
                            //IOException | InterruptedException
                            //        |
                            SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException
                                    e) {
                        String message = e.getCause().getMessage();
                        logger.warn(message);
                        HttpHelper.sendMessage(message);
                        HttpHelper.setDeviceSecurityStatus(Constants.SEQURITY_STATUS_NOT_DONE);
                    }


                }
            }
            //HttpHelper.setDeviceSecurityStatus(Constants.SEQURITY_STATUS_DONE);

            Place place = new Place(ControlService.getLocationManager().getLastKnownLocation());
            if (place != null && place.getLocation() != null) {
                //HttpHelper.sendLocation(place.getMixedData());
                HttpHelper.sendLocationNew(place);

            }
            //HttpHelper.sendMessage("Battary level: " + DeviceUtil.getBatteryLevel());
            HttpHelper.sendDeviceData("Battary level", Float.toString(DeviceStatusHelper.getBatteryLevel()));
            HttpHelper.sendDeviceData("Screen status", Boolean.toString(DeviceStatusHelper.isScreenOn()));
            HttpHelper.sendDeviceData("Network location", Boolean.toString(DeviceStatusHelper.isNetworkLocationAvailable()));
            HttpHelper.sendDeviceData("GPS location", Boolean.toString(DeviceStatusHelper.isGpsLocationAvailable()));

            HttpHelper.pickupDeviceConfig();
            TestUtils.sleep(Configuration.getInspectorSleepTime());
        } while (run);


    }

/*    private Place getLastKnownPlace() {
        LocationManager locationManager = (LocationManager)ControlService.getContext().getSystemService(Context.LOCATION_SERVICE);
        //FusedLocationProvider locationManager = FusedLocationProvider.getInstance(ControlService.getContext());
        List<String> providers = locationManager.getProviders(true);
        Place place = new Place();
        Location bestLocation = null;

*//*        mobileNetworkHelper.switchMobileDataOnOff(false);
        mobileNetworkHelper.switchMobileDataOnOff(true);
        TestUtils.sleep(10000);*//*

        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }

            if (bestLocation == null ||
                    (l.getTime() - bestLocation.getTime()) > Constants.LOCATION_TIME_ADVANCE ||
                    (l.getAccuracy() <= bestLocation.getAccuracy() && (l.getTime() - bestLocation.getTime()) > -Constants.LOCATION_TIME_ADVANCE)
                    ) {
                // Found best last known location: %s", l);
                bestLocation = l;
                place.setLocation(l);
                place.setLocationTime(l.getTime());
                place.setProvider(provider);
            }
        }
        return place;
    }*/

/*    private Place getLastKnownPlace2() {
        Location l = ControlService.getLocationManager().getLastKnownLocation();
        Place place = new Place(l);
        *//*place.setLocation(l);
        place.setLocationTime(l.getTime());
        place.setProvider(l.getProvider());
        l.getAccuracy()*//*
        return place;
    }*/


    public Location getLocation() {
        //LocationManager locationManager = (LocationManager) ControlService.getContext().getSystemService(Context.LOCATION_SERVICE);


        LocationManager locationManager = (LocationManager) ControlService.getContext().getSystemService(Context.LOCATION_SERVICE );
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location netLocation = null, gpsLocation = null, finalLocation = null;

        if (isGPSEnabled) {
            HttpHelper.setDeviceSecurityStatus("GPS is enabled");
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (isNetworkEnabled) {
            HttpHelper.setDeviceSecurityStatus("Network is enabled");
            netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (gpsLocation != null && netLocation != null) {

            if (gpsLocation.getAccuracy() >= netLocation.getAccuracy())
                finalLocation = gpsLocation;
            else
                finalLocation = netLocation;

            // I used this just to get an idea (if both avail, its upto you which you want to take as I taken location with more accuracy)

        } else {

            if (gpsLocation != null) {
                finalLocation = netLocation;
            } else if (netLocation != null) {
                finalLocation = gpsLocation;
            }
        }
        return finalLocation;
    }



    public void uninstallApp(String appPackage)  throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        ApplicationManager am = new ApplicationManager(ControlService.getContext());
        if (isPackageExist(appPackage)) {
            am.uninstallPackage(appPackage);
            HttpHelper.sendMessage("package " + appPackage + " has been uninstalled");
        }
    }

    private boolean isPackageExist(String appPackage) {
        PackageManager pm = ControlService.getContext().getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(appPackage,PackageManager.GET_META_DATA);
            HttpHelper.sendMessage("package " + appPackage + "  exists");
        } catch (PackageManager.NameNotFoundException e) {
            HttpHelper.sendMessage("package " + appPackage + " doesn't exist");
            return false;
        }
        return true;
    }


    public void setRun(boolean run) {
        this.run = run;
    }
}
