package com.midway.mobileinspector.common;

/**
 * Created by ilya on 13.03.16.
 */
public class Configuration {
    private static long inspectorSleepTime = Constants.INSPECTOR_THREAD_SLEEP;
    private static int configId = Constants.INITIAL_CONFIG_ID;

    public static long getInspectorSleepTime() {
        return inspectorSleepTime;
    }

    public static void setInspectorSleepTime(long ist) {
        inspectorSleepTime = ist;
    }

    public static int getConfigId() {
        return configId;
    }

    public static void setConfigId(int configId) {
        Configuration.configId = configId;
    }
}
