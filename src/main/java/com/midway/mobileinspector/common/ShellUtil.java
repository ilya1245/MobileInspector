package com.midway.mobileinspector.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public abstract class ShellUtil {
    private static final Logger logger = LoggerFactory.getLogger(ShellUtil.class);

    public static void runSuCommand(String command) throws IOException, InterruptedException {
        String[] commandLine = {"su", "-c", "\'", command, "\'"};
        Runtime.getRuntime().exec(commandLine).waitFor();
    }

    public static void runSuCommand1(String command) throws IOException, InterruptedException {
        String commandLine = "su " + "-c " + "\'" + command + "\'";
        Runtime.getRuntime().exec(commandLine).waitFor();
    }

    public static void runCommand(String command) throws IOException, InterruptedException {
        String[] commandLine = {command};
        Runtime.getRuntime().exec(commandLine).waitFor();
    }

    public static void uninstallApplication(String appPackage) throws IOException, InterruptedException {
        runSuCommand("pm uninstall " + appPackage);
    }

    public static void chmodCommand(String permission, String path, boolean recursion) throws IOException, InterruptedException {
        final String command = "chmod " + (recursion ? "-R " : "") + "%s %s";
        runSuCommand(String.format(command, permission, path));
    }

    public static void killProcess(int code, int killPid) throws IOException, InterruptedException {
        final String command = "kill %d %d";
        runSuCommand(String.format(command, code, killPid));
    }

    public static void killProcess(int killPid) throws IOException, InterruptedException {
        killProcess(-9, killPid);
    }

    public static void killProcesses(List<Integer> killPids) throws IOException, InterruptedException {
        for (int pid : killPids) {
            killProcess(pid);
        }
    }

    public static String execSimple(String command) {
        String result = null;
        Process process;

        try {
            process = Runtime.getRuntime().exec(command);
            String stderr = getStreamData(process.getErrorStream());
            String stdout = getStreamData(process.getInputStream());
            result = stdout + "\n" + stderr;
        } catch (IOException e) {
            logger.error("Not executed! Message: {}", e.getMessage());
        }
        return result;
    }

    private static String getStreamData(InputStream is) {
        try {
            BufferedReader rdr = new BufferedReader(new InputStreamReader(is), 4096);
            String line;
            StringBuilder sbuf = new StringBuilder();
            while ((line = rdr.readLine()) != null) {
                sbuf.append(line);
                sbuf.append("\n");
            }
            return sbuf.toString();
        } catch (Exception e) {
            logger.error("Error in getStreamData", e);
        }
        return "";
    }
}