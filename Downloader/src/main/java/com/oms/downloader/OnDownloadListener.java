package com.oms.downloader;



public interface OnDownloadListener {

    void onDownloadComplete();

    void onError(Error error);

}
