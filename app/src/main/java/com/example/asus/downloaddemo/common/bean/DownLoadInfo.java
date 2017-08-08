package com.example.asus.downloaddemo.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by asus on 2017/8/2.
 */

@Entity
public class DownLoadInfo {
    @Id
    Long id;
    @Property
    private String url;
    @Property
    private int taskId;//比如3个线程下载一个任务，则为0,1,2
    @Property
    private long finished;
    @Property
    private boolean isFinish;
@Generated(hash = 1938013496)
    public DownLoadInfo(Long id, String url, int taskId, long finished,
            boolean isFinish) {
        this.id = id;
        this.url = url;
        this.taskId = taskId;
        this.finished = finished;
        this.isFinish = isFinish;
    }
    @Generated(hash = 1743687477)
    public DownLoadInfo() {
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getTaskId() {
        return this.taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public long getFinished() {
        return this.finished;
    }
    public void setFinished(long finished) {
        this.finished = finished;
    }
    public boolean getIsFinish() {
        return this.isFinish;
    }
    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    

}
