package com.midway.mobileinspector.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.midway.mobileinspector.R;
import com.midway.mobileinspector.service.ControlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ilya on 13.02.16.
 */
public class PropertyLoadUtil {
    private static Properties inspectorProperties;
    private static final Logger logger = LoggerFactory.getLogger(PropertyLoadUtil.class.getSimpleName());

    public static void init() {
        initProperties();
    }

    private static void initProperties() {
        if (inspectorProperties != null) {
            inspectorProperties.clear();
        }
        logger.debug("initProperties");
        inspectorProperties = getPropertiesFromAssets("inspector.properties");
    }

    public static String getProperty(String propertyName) {
        return inspectorProperties.getProperty(propertyName);
    }

    private static Properties getPropertiesFromAssets(String file) {
        Context context = ControlService.getContext();
        AssetManager assetManager = context.getAssets();
        Properties props = null;
        try {
            InputStream inputStream = assetManager.open(file);
            props = new Properties();
            props.load(inputStream);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return props;
    }
}
