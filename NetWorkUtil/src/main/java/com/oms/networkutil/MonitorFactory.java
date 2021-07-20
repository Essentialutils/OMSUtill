package com.oms.networkutil;

import android.content.Context;

import androidx.annotation.NonNull;

public interface MonitorFactory {

    @NonNull
    Monitor create(
            @NonNull Context context,
            int connectionType,
            @NonNull Monitor.ConnectivityListener listener);
}
