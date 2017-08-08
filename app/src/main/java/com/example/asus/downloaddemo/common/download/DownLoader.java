package com.example.asus.downloaddemo.common.download;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.asus.downloaddemo.common.bean.AppInfo;
import com.example.asus.downloaddemo.common.bean.DownLoadInfo;
import com.example.asus.downloaddemo.common.dao.DaoManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

/**
 * Created by asus on 2017/8/2.
 *
 */

public class DownLoader {

    private static final String TAG = "DownLoader";

    public static final int THRAED_NUM = 3;

    public static final int GET_LENGTH_SUCCUSS = 0;

    private List<DownLoadTask> mTaskList;
    private AppInfo mAppInfo;


    private Context mContext;
    private long mDownLoadLength = 0;//已经下载的长度
    private long mFileLength;//文件的总长度

    private DownLoadHandler mHandler = new DownLoadHandler();


    public DownLoader(Context context, AppInfo appInfo) {
        this.mContext = context;
        this.mAppInfo = appInfo;
        mTaskList = new ArrayList<>();
    }


    public String getSavePath() {
        return DownLoadManager.SAVE_PATH;
    }

    public String getFileName() {
        return mAppInfo.getFileName();
    }

    //初始化下载，读取文件大小，计算文件已经完成的进度
    public void initDownLoad() {
        Log.d(TAG, "initDownLoad: ");
        if (mAppInfo == null) {
            throw new IllegalArgumentException("下载内容不能为空");
        }
        if (mAppInfo.getStatus() == DownLoadManager.DOWNLOAD_STATUS_FINISHEDED) {
            Log.d(TAG, "initDownLoad: 初始化失败，文件之前已经下载成功");
            EventBus.getDefault().post(new DownLoadProgressEvent(mAppInfo));
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    //连接网络文件
                    URL url = new URL(mAppInfo.getUrl());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int length = -1;
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        //获得文件的长度
                        length = connection.getContentLength();
                        Log.d(TAG, "run: 获取下载内容长度:" + length);
                    } else {
                        Log.d(TAG, "run: 获取下载内容长度，网络请求出错:" + connection.getResponseCode());
                        netWorkError();
                        return;
                    }
                    if (length <= 0) {
                        return;
                    }
                    mFileLength = length;
                    //计算出该文件已经下载的总长度
                    List<DownLoadInfo> list = DaoManager.getInstance().getDownLoadInfoByUrl(mAppInfo.getUrl());
                    for (DownLoadInfo info : list) {
                        mDownLoadLength = mDownLoadLength + info.getFinished();
                    }
                    mAppInfo.setLength(mFileLength);
                    DaoManager.getInstance().insertOrUpdateAppInfo(mAppInfo);


                    File dir = new File(getSavePath());
                    if (!dir.exists()) {
                        dir.mkdir();
                        Log.d(TAG, "run: 文件目录不存在，创建文件目录成功");
                    } else {
                        Log.d(TAG, "run: 文件目录已经存在");
                    }
                    File file = new File(getSavePath(), getFileName());
                    if (!file.exists()) {
                        file.createNewFile();
                        Log.d(TAG, "run: 文件不存在，创建下载文件成功");
                    } else {
                        Log.d(TAG, "run: 文件已经存在");
                    }

                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");//可读可写可删除
                    raf.setLength(length);

                    mHandler.sendEmptyMessage(GET_LENGTH_SUCCUSS);


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mAppInfo.setStatus(DownLoadManager.DOWNLOAD_STATUS_ERROR);
                    EventBus.getDefault().post(new DownLoadProgressEvent(mAppInfo));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }).start();

    }

    //开始创建线程进行下载
    public void downLoad() {
//        mAppInfo.setStatus(DownLoadManager.DOWNLOAD_STATUS_WAITING);
        long length = mFileLength / THRAED_NUM;

//        Log.d(TAG, "downLoad: 第一次下载该文件");
        //如果是第一次下载这个任务
        for (int i = 0; i < THRAED_NUM; i++) {
            long start = length * i;
            long end = length * (i + 1) - 1;
            if (i == THRAED_NUM - 1) {
                end = mFileLength;
            }
            DownLoadTask task = new DownLoadTask(mAppInfo.getUrl(), i, start, end, this);
            task.executeOnExecutor(THREAD_POOL_EXECUTOR);
            if (mTaskList == null) {
                mTaskList = new ArrayList<>();
            }
            mTaskList.add(task);
        }
        mAppInfo.setStatus(DownLoadManager.DOWNLOAD_STATUS_DOWNLOADING);
    }

    //停止下载
    public void pauseDownLoad() {
        for (int i = 0; i < mTaskList.size(); i++) {
            DownLoadTask task = mTaskList.get(i);
            if (task != null && (task.getStatus() == AsyncTask.Status.RUNNING || !task.isCancelled())) {
                task.cancel(true);
                task.onCancelled();
                task.setFinished(true);
                Log.d(TAG, "pauseDownLoad: 线程" + i + "暂停下载");
            }
        }
        Log.d(TAG, "pauseDownLoad: 任务已经暂停下载");
        mTaskList.clear();
        mAppInfo.setStatus(DownLoadManager.DOWNLOAD_STATUS_PAUSE);
        EventBus.getDefault().post(new DownLoadProgressEvent(mAppInfo));
        DaoManager.getInstance().insertOrUpdateAppInfo(mAppInfo);
    }


    //更新下载进度
    public synchronized void updateDownLoadProgress(long size) {
        mDownLoadLength = mDownLoadLength + size;
        mAppInfo.setFinished(mDownLoadLength);
        if (mAppInfo.getLength()>mAppInfo.getFinished()){
            mAppInfo.setStatus(DownLoadManager.DOWNLOAD_STATUS_DOWNLOADING);
        }else if (mAppInfo.getLength()==mAppInfo.getFinished()){
            mAppInfo.setStatus(DownLoadManager.DOWNLOAD_STATUS_FINISHEDED);
        }
        Log.d(TAG, "updateDownLoadProgress: 文件总长度;" + mFileLength + "已下载大小" + mDownLoadLength);
        EventBus.getDefault().post(new DownLoadProgressEvent(mAppInfo));
    }

    //网络请求错误
    public  void netWorkError(){
        mAppInfo.setStatus(DownLoadManager.DOWNLOAD_STATUS_ERROR);
        EventBus.getDefault().post(new DownLoadProgressEvent(mAppInfo));
    }

    //文件下载成功
    public void finishDownLoad() {
        Log.d(TAG, "finishDownLoad: 文件下载完成");
        DaoManager.getInstance().deleteDownLoadInfo(mAppInfo.getUrl());
        mAppInfo.setStatus(DownLoadManager.DOWNLOAD_STATUS_FINISHEDED);
        mAppInfo.setFinished(mAppInfo.getLength());
//        EventBus.getDefault().post(new DownLoadProgressEvent(mAppInfo));

        DaoManager.getInstance().insertOrUpdateAppInfo(mAppInfo);
    }


    public synchronized boolean checkAllFinished() {
        boolean flag = true;
        for (int i = 0; i < mTaskList.size(); i++) {
            if (!mTaskList.get(i).isFinished()) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    private class DownLoadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_LENGTH_SUCCUSS:
                    Log.d(TAG, "handleMessage: 初始化完毕，开始进行下载");
                    downLoad();
                    break;
            }
            super.handleMessage(msg);
        }
    }


}
