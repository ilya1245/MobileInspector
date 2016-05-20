package com.midway.mobileinspector.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.midway.mobileinspector.service.ControlService;

/**
 * Created by ilya on 20.02.16.
 */
public class BootUpReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, Main.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
