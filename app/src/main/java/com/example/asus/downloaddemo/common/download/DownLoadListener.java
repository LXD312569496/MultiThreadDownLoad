package com.example.asus.downloaddemo.common.download;

/**
 * Created by asus on 2017/8/2.
 * 下载的监听
 * todo:未实现
 */

public interface DownLoadListener {

    void onPrepare(String url,long progress, long total);
    void onStart(String url);
    void onDownloading(String url,long progress, long total);
    void onPause(String url);
    void onCancel(String url);
    void onCompleted(String url);
    void onError(Exception e);

}
