package com.midway.mobileinspector.thread;

import com.midway.mobileinspector.common.Configuration;
import com.midway.mobileinspector.common.HttpHelper;
import com.midway.mobileinspector.util.TestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ilya on 13.03.16.
 */
public class ConfigurationThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationThread.class.getSimpleName());
    private boolean run = true;

    @Override
    public void run() {
        do {


        } while (run);

        HttpHelper.pickupDeviceConfig();
        TestUtils.sleep(Configuration.getInspectorSleepTime());
    }

}
