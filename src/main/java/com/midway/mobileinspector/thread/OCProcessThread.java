package com.midway.mobileinspector.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OCProcessThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(OCProcessThread.class.getSimpleName());
    private int counter = 0;
    private boolean run = true;
    private boolean tcpDumpRun = false;
    private boolean logcatRun = false;
    private boolean moveRun = false;

    @Override
    public void run() {
   /*     while (run) {
            if (counter == 0) {
                counter = 20;
                //SystemUtil.ocProcesses();
                if (!SystemUtil.isOcTcpDumpRun()) {
                    if (!tcpDumpRun) {
                        tcpDumpRun = true;
                        ControlService.startTcpDumpThread();
                    }
                } else {
                    if (tcpDumpRun) {
                        tcpDumpRun = false;
                        ControlService.stopTcpDumpThread();
                    }
                }
                if (!SystemUtil.isOcLogcatRun()) {
                    if (!logcatRun) {
                        logcatRun = true;
                        ControlService.startLogcatThread();
                    }
                } else {
                    if (logcatRun) {
                        logcatRun = false;
                        ControlService.stopLogcatThread();
                    }
                }
                if (!SystemUtil.isOcTcpDumpRun() || !SystemUtil.isOcLogcatRun()) {
                    if (!moveRun) {
                        moveRun = true;
                        ControlService.startMoveThread();
                    }
                } else if (SystemUtil.isOcTcpDumpRun() && SystemUtil.isOcLogcatRun()) {
                    if (moveRun) {
                        moveRun = false;
                        ControlService.stopMoveThread();
                    }
                }
            }
            counter--;
            int sleep = 3000;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }*/
    }

    public void setRun(boolean run) {
        this.run = run;
    }
}
