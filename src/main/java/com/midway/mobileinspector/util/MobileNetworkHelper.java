package com.midway.mobileinspector.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.midway.mobileinspector.common.HttpHelper;

import java.lang.reflect.Method;

/**
 * Created by ilya on 23.02.16.
 */
public class MobileNetworkHelper {
    private Context context;
    private static final String TAG = MobileNetworkHelper.class.getSimpleName();

    public MobileNetworkHelper(Context context) {
        this.context = context;
    }

    /**
     * Turn wifi on or off
     * @param onOrOff true - on wifi, false - off wifi
     * @return 1 if done or 0 if not
     */
    public int switchWifiOnOff(boolean onOrOff) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled() != onOrOff){
            wifiManager.setWifiEnabled(onOrOff);
        }
        int counter = 0;
        do {
            try {
                Thread.sleep(1000);
                counter++;
            } catch (InterruptedException ie){
                //Log.v(TAG, ExceptionUtils.getStackTrace(ie));
            }
        } while (wifiManager.isWifiEnabled() != onOrOff && counter < 30);
        //Log.v(TAG, "counter=" + counter + "; isWifiEnable=" + wifiManager.isWifiEnabled() + "; onOrOff=" + onOrOff);
        boolean result = wifiManager.isWifiEnabled() ^ onOrOff; //false means OK
        if (!result) {
            Log.v(TAG, "Wifi switced to " + (onOrOff ? "on." : "off."));
        }
        if (onOrOff) TestUtils.sleep(60 * 1000);
        return result ? 0 : 1;
    }


    /**
     * Turn 3G on or off
     * @param onOrOff true - on 3G, false - off 3G
     * @return 1 if done or 0 if not
     */
    public int switchMobileDataOnOff(boolean onOrOff) {
        if (isMobileDataEnabled() != onOrOff) {
            try {
                ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                dataMtd.setAccessible(true);
                dataMtd.invoke(conMgr, onOrOff);
            } catch (Exception e) {
                Log.e(TAG, e.getCause().getMessage());
            }
        }
        int counter = 0;
        do {
            try {
                Thread.sleep(1000);
                counter++;
            } catch (InterruptedException ie){
                Log.v(TAG, ie.getCause().getMessage());
            }
        } while (isMobileDataEnabled() != onOrOff && counter < 30);
        //Log.v(TAG, "counter=" + counter + "; isMobileDataEnabled=" + isMobileDataEnabled() + "; onOrOff=" + onOrOff);
        boolean result = isMobileDataEnabled() ^ onOrOff;  //false means OK
        if (!result) {
            Log.v(TAG, "3G switced to " + (onOrOff ? "on." : "off."));
        }
        if (onOrOff) TestUtils.sleep(60 * 1000);
        return result ? 0 : 1;
    }


    public void switchGpsOnOff(boolean onOrOff) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            dataMtd.setAccessible(true);
            dataMtd.invoke(conMgr, onOrOff);
        } catch (Exception e) {
            Log.e(TAG, e.getCause().getMessage());
        }
    }

    /**
     * @return 3G status
     */
    public boolean isMobileDataEnabled() {
        Object connectivityService = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean) m.invoke(cm);
        } catch (Exception e) {
            Log.e(TAG, e.getCause().getMessage());
        }
        return false;
    }

    /**
     * Turn wifi on and 3g off
     * @return 1 if done or 0 if not
     */
    public int onWifiOnly() {
        return (switchWifiOnOff(true) == 1 && switchMobileDataOnOff(false) == 1) ? 1 : 0;
    }

    /**
     * Turn 3G on and wifi off
     * @return 1 if done or 0 if not
     */
    public int on3gOnly() {
        return (switchMobileDataOnOff(true) == 1 && switchWifiOnOff(false) == 1) ? 1 : 0;
    }

    public int getActiveNetworkType() {
        int ret = 0; // IF_UNKNOWN
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            ret = netInfo.getType();
            if( ret == ConnectivityManager.TYPE_MOBILE ) {
                ret = 1; // IF_MOBILE
                // check sub_type here
                TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                int type = telephonyManager.getNetworkType();
                switch(type) {
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN: ret = 4;  break; // -> MOBILE_NETWORK_TYPE_NAME_UNKNOWN
                    case TelephonyManager.NETWORK_TYPE_GPRS:    ret = 5;  break; // -> MOBILE_NETWORK_TYPE_NAME_GPRS
                    case TelephonyManager.NETWORK_TYPE_EDGE:    ret = 6;  break; // -> MOBILE_NETWORK_TYPE_NAME_EDGE
                    case TelephonyManager.NETWORK_TYPE_UMTS:    ret = 7;  break; // -> MOBILE_NETWORK_TYPE_NAME_UMTS
                    case TelephonyManager.NETWORK_TYPE_HSDPA:   ret = 8;  break; // -> MOBILE_NETWORK_TYPE_NAME_HSDPA
                    case TelephonyManager.NETWORK_TYPE_HSUPA:   ret = 9;  break; // -> MOBILE_NETWORK_TYPE_NAME_HSUPA
                    case TelephonyManager.NETWORK_TYPE_HSPA:    ret = 10; break; // -> MOBILE_NETWORK_TYPE_NAME_HSPA
                    case TelephonyManager.NETWORK_TYPE_CDMA:    ret = 11; break; // -> MOBILE_NETWORK_TYPE_NAME_CDMA
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:  ret = 12; break; // -> MOBILE_NETWORK_TYPE_NAME_EVDO_0
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:  ret = 13; break; // -> MOBILE_NETWORK_TYPE_NAME_EVDO_A
                    /* Since: API Level 9 */
                    // case TelephonyManager.NETWORK_TYPE_EVDO_B: ret= 14; break; // -> MOBILE_NETWORK_TYPE_NAME_EVDO_B
                    case TelephonyManager.NETWORK_TYPE_1xRTT:   ret = 15; break; // -> MOBILE_NETWORK_TYPE_NAME_EVDO_1xRTT
                    case TelephonyManager.NETWORK_TYPE_IDEN:    ret = 1;  break; // No code in Asimov for this
                    /* Since: API Level 11 */
                    // case TelephonyManager.NETWORK_TYPE_LTE:
                    // case TelephonyManager.NETWORK_TYPE_EHRPD:
                    default: break;
                }
            } else if( ret == ConnectivityManager.TYPE_WIFI ) {
                ret = 2; // IF_WIFI
                //WifiManager wifiManager = (WifiManager)Z7Shared.context.getSystemService(Context.WIFI_SERVICE);
            } else if( ret == ConnectivityManager.TYPE_WIMAX ) {
                ret = 16; // IF_WIMAX
            }
            // NOTE, there could be also TYPE_BLUETOOTH, TYPE_DUMMY, TYPE_ETHERNET, TYPE_MOBILE_DUN,
            //                            TYPE_MOBILE_HIPRI, TYPE_MOBILE_MMS, TYPE_MOBILE_SUPL
        } catch( Exception e ) {
            HttpHelper.sendMessage("Exception in getActiveNetworkType");
            e.printStackTrace();
        }
        return ret;
    }
}

