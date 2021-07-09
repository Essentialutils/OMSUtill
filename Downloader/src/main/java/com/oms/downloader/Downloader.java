package com.oms.downloader;

import android.content.Context;

import com.oms.downloader.core.Core;
import com.oms.downloader.internal.ComponentHolder;
import com.oms.downloader.internal.DownloadRequestQueue;
import com.oms.downloader.request.DownloadRequestBuilder;
import com.oms.downloader.utils.Utils;

public class Downloader {

    private Downloader() {
    }

    public static void initialize(Context context) {
        initialize(context, DownloaderConfig.newBuilder().build());
    }

    public static void initialize(Context context, DownloaderConfig config) {
        ComponentHolder.getInstance().init(context, config);
        DownloadRequestQueue.initialize();
    }

    public static DownloadRequestBuilder download(String url, String dirPath, String fileName) {
        return new DownloadRequestBuilder(url, dirPath, fileName);
    }

    public static void pause(int downloadId) {
        DownloadRequestQueue.getInstance().pause(downloadId);
    }

    public static void resume(int downloadId) {
        DownloadRequestQueue.getInstance().resume(downloadId);
    }

    public static void cancel(int downloadId) {
        DownloadRequestQueue.getInstance().cancel(downloadId);
    }

    public static void cancel(Object tag) {
        DownloadRequestQueue.getInstance().cancel(tag);
    }

    public static void cancelAll() {
        DownloadRequestQueue.getInstance().cancelAll();
    }

    public static Status getStatus(int downloadId) {
        return DownloadRequestQueue.getInstance().getStatus(downloadId);
    }

    public static void cleanUp(int days) {
        Utils.deleteUnwantedModelsAndTempFiles(days);
    }

    public static void shutDown() {
        Core.shutDown();
    }

}
