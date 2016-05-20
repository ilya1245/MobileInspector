package com.midway.mobileinspector.common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ilya on 13.03.16.
 */
public class JsonUtil {
    //final String

    public static void updateConfigFromJson(String jsonString) {
        //Map result = new HashMap<String, String>();
        //Configuration config = new Configuration();

        try {
            JSONObject obj = new JSONObject(jsonString);
            int configId = obj.getInt("id");

            if (Configuration.getConfigId() != configId) {
                Configuration.setConfigId(configId);
                int sleepTime = obj.getInt(Constants.INSPECTOR_THREAD_SLEEP_FIELD_NAME);
                Configuration.setInspectorSleepTime(sleepTime);
                HttpHelper.sendMessage(Constants.INSPECTOR_THREAD_SLEEP_FIELD_NAME + " has been set to " + sleepTime);
            }

        } catch (JSONException je) {
            HttpHelper.sendMessage("Exception in updateConfigFromJson");
            je.printStackTrace();
        }

        //return config;
    }
}
