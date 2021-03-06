package com.midway.mobileinspector.common;

import android.net.ConnectivityManager;

import com.midway.mobileinspector.service.ControlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import static com.midway.mobileinspector.common.PropertyLoadUtil.getProperty;

/**
 * Created by ilya on 14.02.16.
 */
public class HttpHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class.getSimpleName());

    private static ConnectivityManager cm = (ConnectivityManager) ControlService.getContext().getSystemService(ControlService.getContext().CONNECTIVITY_SERVICE);
    //private static int i = 0;

    public static String getDeviceSecurity() {
        String strTemp = executeHttpReques("/device/get_security/" + ControlService.android_id);
        return strTemp;

    }

    public static void setDeviceSecurityStatus(String status) {
        if (cm.getActiveNetworkInfo() == null) return;
        try {
            String url = "/device/set_security_status/" + ControlService.android_id + "/" + status;
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(getAbsoluteUrl(url)).openStream()));

        } catch (Exception ex) {
            HttpHelper.sendMessage("Exception in setDeviceSecurityStatus");
            ex.printStackTrace();
        }
    }

    public static void sendMessage(String message) {
        if (StringUtils.isEmpty(message)) return;
        if (cm.getActiveNetworkInfo() == null) return;
        String message100 = message.length() >= 100 ? message.substring(0, 100) : message;
        try {
            String url = "/message/" + ControlService.android_id + "?message=" +  URLEncoder.encode(message100, "UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(getAbsoluteUrl(url)).openStream()));
        } catch (Exception ex) {
            //HttpHelper.sendMessage("Exception in sendMessage");
            ex.printStackTrace();
        }
    }

    /*public static void sendLocation(String location) {
        try {
            String url = "/device/set_location_message?deviceId=" + ControlService.android_id + "&location=" +  URLEncoder.encode(location, "UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(getAbsoluteUrl(url)).openStream()));
        } catch (Exception ex) {
            HttpHelper.sendMessage("Exception in sendLocation");
            ex.printStackTrace();
        }
    }*/


    public static void sendLocation(Place place) {
        if (cm.getActiveNetworkInfo() == null) return;
        try {
            StringBuilder url = new StringBuilder("/device/set_location").
                    append("?deviceId=").append(ControlService.android_id).
                    append("&latitude=").append(place.getLocation().getLatitude()).
                    append("&longitude=").append(place.getLocation().getLongitude()).
                    append("&provider=").append(place.getProvider()).
                    append("&address=").append(URLEncoder.encode(place.getStringAddress(), "UTF-8")).
                    append("&location_date=").append(place.getLocation().getTime());
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(getAbsoluteUrl(url.toString())).openStream()));
        } catch (Exception ex) {
            HttpHelper.sendMessage("Exception in sendLocation");
            ex.printStackTrace();
        }
    }

    public static void sendLocationNew(Place place) {
        if (cm.getActiveNetworkInfo() == null) return;
        try {
            StringBuilder url = new StringBuilder("/device/set_location_new").
                    append("?deviceId=").append(ControlService.android_id).
                    append("&latitude=").append(place.getLocation().getLatitude()).
                    append("&longitude=").append(place.getLocation().getLongitude()).
                    append("&provider=").append(place.getProvider()).
                    append("&address=").append(URLEncoder.encode(place.getStringAddress(), "UTF-8")).
                    append("&location_date=").append(place.getLocation().getTime()) .
                    append("&accuracy=").append(place.getLocation().getAccuracy()).
                    append("&speed=").append(place.getLocation().getSpeed())
                    ;
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(getAbsoluteUrl(url.toString())).openStream()));
        } catch (Exception ex) {
            HttpHelper.sendMessage("Exception in sendLocation");
            ex.printStackTrace();
        }
    }

    public static void sendDeviceData(String name, String value) {
        if (cm.getActiveNetworkInfo() == null) return;
        try {
            String url = "/device/set_data?deviceId=" + ControlService.android_id + "&name=" +  URLEncoder.encode(name, "UTF-8") + "&value=" +  URLEncoder.encode(value, "UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(getAbsoluteUrl(url)).openStream()));
        } catch (Exception ex) {
            HttpHelper.sendMessage("Exception in sendDeviceData");
            ex.printStackTrace();
        }
    }

    private static String executeHttpReques(String url) {
        if (cm.getActiveNetworkInfo() == null) return null;
        String strTemp = "";
        try {
            //logger.info("android_id=" + ControlService.android_id);
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(getAbsoluteUrl(url)).openStream()));
            //"/device/status/" + ControlService.android_id

            while (null != (strTemp = br.readLine())) {
                return strTemp;
                //logger.info("android_id=" + ControlService.android_id + "   status=" + strTemp);
            }


        } catch (Exception ex) {
            HttpHelper.sendMessage("Exception in executeHttpReques");
            ex.printStackTrace();
        }
        return null;
    }

    private static String getAbsoluteUrl(String url) {
        StringBuffer result = new StringBuffer("http://").append(getProperty("server.host")).append(":").
                append(getProperty("server.port")).append("/").append(getProperty("server.app_name")).append(url);
        return result.toString();
    }

    public static void pickupDeviceConfig() {
        String strTemp = executeHttpReques("/device/get_config?deviceId=" + ControlService.android_id);
        if (StringUtils.isNotEmpty(strTemp)) JsonUtil.updateConfigFromJson(strTemp);
    }

/*    private static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) ControlService.getContext().getSystemService(ControlService.getContext().CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }*/


}
