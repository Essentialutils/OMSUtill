package com.oms.networkutil;

import android.content.Context;
import android.util.Log;

import com.oms.networkutil.internal.DefaultMonitorFactory;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;



public class NetWorkUtil {
    private static final String TAG = "networkUtil";
    private static final Object lock = new Object();

    private static volatile NetWorkUtil networkUtil;
    private WeakReference<Context> contextRef;
    private Set<Monitor> monitors;

    private NetWorkUtil(Context context) {
        monitors = new HashSet<>();
        this.contextRef = new WeakReference<>(context);
    }

    public static NetWorkUtil from(Context context) {
        if (networkUtil == null) {
            synchronized (lock) {
                if (networkUtil == null) {
                    networkUtil = new NetWorkUtil(context);
                }
            }
        }
        return networkUtil;
    }

    public NetWorkUtil monitor(int connectionType, Monitor.ConnectivityListener listener) {
        Context context = contextRef.get();
        if (context != null)
            monitors.add(new DefaultMonitorFactory().create(context, connectionType, listener));

        start();
        return networkUtil;
    }

    public NetWorkUtil monitor(Monitor.ConnectivityListener listener) {
        return monitor(-1, listener);
    }

    public void start() {
        for (Monitor monitor : monitors) {
            monitor.onStart();
        }

        if (monitors.size() > 0)
            Log.i(TAG, "started networkUtil");
    }

    public void stop() {
        for (Monitor monitor : monitors) {
            monitor.onStop();
        }
    }
}
