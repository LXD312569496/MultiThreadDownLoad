package com.example.asus.downloaddemo.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by asus on 2017/8/2.
 */
@Entity
public class AppInfo {
    @Id
    @Unique
    String fileName;
    @Property
    String url;
    @Property
    long length;//文件总长度
    @Property
    long finished;//已完成总大小
    @Property
    int status;//下载状态
    @Generated(hash = 1587961731)
    public AppInfo(String fileName, String url, long length, long finished,
            int status) {
        this.fileName = fileName;
        this.url = url;
        this.length = length;
        this.finished = finished;
        this.status = status;
    }
    @Generated(hash = 1656151854)
    public AppInfo() {
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public long getLength() {
        return this.length;
    }
    public void setLength(long length) {
        this.length = length;
    }
    public long getFinished() {
        return this.finished;
    }
    public void setFinished(long finished) {
        this.finished = finished;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
}
