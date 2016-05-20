package com.midway.mobileinspector.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

import com.midway.mobileinspector.common.HttpHelper;

import junit.framework.Assert;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by ilya on 23.02.16.
 */
public final class TestUtils {

    private static final String OC_CONTROL_COMMAND_BASE_URL = "http://localhost/oc/";

    /**
     * Hidden constructor.
     */
    private TestUtils() {
        // Empty
    }

    public static void sleep(long millis) {
        if (millis >= 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                HttpHelper.sendMessage("Exception in sleep");
                e.printStackTrace();
            }
        }
    }


    /**
     * Sleeps <code>millis</code> milliseconds since some moment (<code>msSince</code>)<br>
     * Example: <br>
     * <code>
     *  long ms = System.currentTimeMillis(); // initial moment <br>
     *  someMethod(); // some method executed, duration 2 sec. <br>
     *  TestUtils.sleep(5000, ms); // sleep 3 sec. only (5 sec. since initial moment)
     * </code>
     *
     * @param millis
     * @param msSince
     */
    public static void sleep(long millis, long msSince) {
        sleep(millis - (System.currentTimeMillis() - msSince));
    }

    /**
     * Sleeps <code>millis</code> milliseconds since some moment (<code>msSince</code>)<br>
     * Throws AssertionFailedError when end of sleeping is already in the past
     * @throws InterruptedException
     */
    public static void sleepChecked(long millis, long msSince) {
        long now = System.currentTimeMillis();
        Assert.assertTrue(now <= msSince + millis);
        sleep(millis - (now - msSince));
    }

    public static InputStream getAssetAsStream(Context context, String name) throws IOException {
        return context.getResources().getAssets().open(name);
    }

    public static Context getTestSuiteContext(Context context) throws PackageManager.NameNotFoundException {
        return context.createPackageContext("com.seven.asimov.test", Context.CONTEXT_IGNORE_SECURITY);
    }

    public static void resetDeviceState(Context context) throws IOException, InterruptedException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        resetIptables();
        resetDataConnection(context);
    }

    public static void resetIptables() throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec("su -c iptables -t nat -F");
        proc.waitFor();
    }

    /**
     * Enables custom ports (for testing certificates) for OC
     * @throws Exception
     */
    public static void addCustomPortsToIptables() throws Exception {
        String cmd = "system/bin/iptables -t nat -A OUTPUT -p 6 --destination-port 11400:11500 -j DNAT --to 127.0.0.1:8074";
        System.out.println("Executing command: " + cmd);
        doCmd(cmd);
    }

    /**
     * Disables custom ports (for testing certificates) for OC
     * @throws Exception
     */
    public static void deleteCustomPortsFromIptables() throws Exception {
        String cmd = "system/bin/iptables -t nat -D OUTPUT -p 6 --destination-port 11400:11500 -j DNAT --to 127.0.0.1:8074";
        System.out.println("Executing command: " + cmd);
        doCmd(cmd);
    }

    public static void resetDataConnection(Context context) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method method = ConnectivityManager.class.getMethod("setMobileDataEnabled", boolean.class);
        try {
            method.invoke(connMgr, false);
            method.invoke(connMgr, true);
        } catch (InvocationTargetException ex) {
        }
    }

    public static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static String generateString(int length)
    {
        Random random = new Random();
        return generateString(random, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz", length);

    }



    public static void doCmd(String cmd) throws Exception {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());

        os.writeBytes(cmd + "\n");
        os.writeBytes("exit\n");
        os.flush();
        os.close();
    }


}