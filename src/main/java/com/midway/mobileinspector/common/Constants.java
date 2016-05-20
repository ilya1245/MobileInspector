package com.midway.mobileinspector.common;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by ilya on 13.02.16.
 */
public interface Constants {
    public static final String MAIN_DIR = File.separator + "sdcard" + File.separator + "MobileInspector";

    public static final String TCPDUMP_FILE_DIR = MAIN_DIR + File.separator + "tcpdump_files";
    public static final String TCPDUMP_FILE_NAME = "capture";
    public static final String TCPDUMP_PERMISSIONS = "664";
    public static final String TCPDUMP_FILES_REGEXP = "capture(\\d)+";
    public static final String TCPDUMP_EXTENSION = "pcap";

    public static final String LOGCAT_FILE_DIR = MAIN_DIR + File.separator + "logcat_files";
    public static final String LOGCAT_FILE_NAME = "logcat";
    public static final String LOGCAT_PERMISSIONS = "664";
    public static final String LOGCAT_FILES_REGEXP = "logcat.(\\d)+";
    public static final String LOGCAT_EXTENSION = "log";

    public static final String DESTINATION_DIR = File.separator + "sdcard" + File.separator + "OpenChannel" +
            File.separator + "Logs";
    public static final long MIN_FREE_MEMORY_MEG = 20;
    public static final String TCPDUMP = "tcpdump";
    public static final String LOGCAT = "logcat";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
    public static final int TRANSFER_INTERVAL = 5000;

    public static final String PID_REGEXP = "([a-zA-Z]*)\\ *([0-9]*)\\ *([0-9]*)\\ *.*(%s)";

    public static final String SECURITY_STATUS_OK = "ok";
    public static final String[] CLONE_ARRAY = {"com.evoc1", "com.evoc2", "com.evoc3"} ;
    public static final String SEQURITY_STATUS_DONE = "done";
    public static final String SEQURITY_STATUS_NOT_DONE = "not done";

    //Thread config
    public static final int INSPECTOR_THREAD_SLEEP = 60000;
    public static final String INSPECTOR_THREAD_SLEEP_FIELD_NAME = "inspectorSleepTime";
    public static final int CONFIG_THREAD_SLEEP = 60000;
    public static final String CONFIG_THREAD_SLEEP_FIELD_NAME = "configSleepTime";

    public static final String INSPECTOR = "inspector";
    public static final int LOCATION_TIME_ADVANCE = 30000;
    public static final int INITIAL_CONFIG_ID = 60;

}
