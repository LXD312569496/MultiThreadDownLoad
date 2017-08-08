package com.example.asus.downloaddemo.common.download;

import com.example.asus.downloaddemo.common.bean.AppInfo;

/**
 * Created by asus on 2017/8/7.
 * 下载进度事件，可读取进度或者下载状态改变
 */

public class DownLoadProgressEvent {
    AppInfo appInfo;

    public DownLoadProgressEvent(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }
    //    String url;
//    long fileLength;
//    long downLoadLength;
//
//    public DownLoadProgressEvent(String url, long fileLength, long downLoadLength) {
//        this.url = url;
//        this.fileLength = fileLength;
//        this.downLoadLength = downLoadLength;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public long getFileLength() {
//        return fileLength;
//    }
//
//    public void setFileLength(long fileLength) {
//        this.fileLength = fileLength;
//    }
//
//    public long getDownLoadLength() {
//        return downLoadLength;
//    }
//
//    public void setDownLoadLength(long downLoadLength) {
//        this.downLoadLength = downLoadLength;
//    }
}
