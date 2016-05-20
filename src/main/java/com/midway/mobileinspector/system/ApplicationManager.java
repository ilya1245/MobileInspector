package com.midway.mobileinspector.system;

import android.content.Context;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ilya on 18.02.16.
 */
public class ApplicationManager {
    //private PackageInstallObserver observer;
    private PackageDeleteObserver observerdelete;
    private PackageManager pm;
    private Method method;
    private Method uninstallmethod;

    class PackageDeleteObserver extends IPackageDeleteObserver.Stub {

        public void packageDeleted(String packageName, int returnCode) throws RemoteException {
        /*if (onInstalledPackaged != null) {
            onInstalledPackaged.packageInstalled(packageName, returnCode);
        }*/
        }
    }

    public ApplicationManager(Context context) throws SecurityException, NoSuchMethodException {

        //observer = new PackageInstallObserver();
        observerdelete = new PackageDeleteObserver();
        pm = context.getPackageManager();

        Class<?>[] types = new Class[]{Uri.class, IPackageInstallObserver.class, int.class, String.class};
        Class<?>[] uninstalltypes = new Class[]{String.class, IPackageDeleteObserver.class, int.class};

        method = pm.getClass().getMethod("installPackage", types);
        uninstallmethod = pm.getClass().getMethod("deletePackage", uninstalltypes);
    }

    public void uninstallPackage(String packagename) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        uninstallmethod.invoke(pm, new Object[]{packagename, observerdelete, 0});
    }
}
