package com.oms.networkutil;

public interface Monitor extends LifecycleListener {

    interface ConnectivityListener {
        void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast);
    }
}
