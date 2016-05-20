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
            Configuration.setInspectorSleepTime(obj.getInt(Constants.INSPECTOR_THREAD_SLEEP_FIELD_NAME));

        } catch (JSONException je) {
            //TODO
        }

        //return config;
    }
}
