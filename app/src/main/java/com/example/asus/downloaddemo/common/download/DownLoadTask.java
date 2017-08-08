package com.example.asus.downloaddemo.common.download;

import android.os.AsyncTask;
import android.util.Log;

import com.example.asus.downloaddemo.common.bean.DownLoadInfo;
import com.example.asus.downloaddemo.common.dao.DaoManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by asus on 2017/8/2.
 * 每个线程的任务类
 */

public class DownLoadTask extends AsyncTask<Void, Long, Void> {

    private static final String TAG = "DownLoadTask";


    private String mUrl;
    private int mTaskId;
    private long mStart;
    private long mEnd;
    private long mFinished = 0;
    private DownLoader mDownLoader;

    private boolean isFinished = false;



    public DownLoadTask(String url, int mTaskId, long mStart, long mEnd, DownLoader mDownLoader) {
        this.mUrl = url;
        this.mTaskId = mTaskId;
        this.mStart = mStart;
        this.mEnd = mEnd;
        this.mDownLoader = mDownLoader;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "doInBackground: ");
        DownLoadInfo mDownLoadInfo = null;

        if (mUrl == null) {
            return null;
        }
        File file = new File(mDownLoader.getSavePath(), mDownLoader.getFileName());

        mDownLoadInfo = DaoManager.getInstance().getDownLoadInfoByUrlAndTaskId(mUrl, mTaskId);

        if (file.exists() && mDownLoadInfo != null) {
            if (mDownLoadInfo.getIsFinish()) {
                //下载完成直接结束
                isFinished = true;
                Log.d(TAG, "doInBackground: 子线程下载完成");
                if (mDownLoader.checkAllFinished()) {
                    mDownLoader.finishDownLoad();
                }
                return null;
            }
        }
        if (mDownLoadInfo != null) {
            mStart = mStart + mDownLoadInfo.getFinished();
            mFinished = mDownLoadInfo.getFinished();
        }

        //如果文件不存在
        if (!file.exists()) {

        }

        HttpURLConnection connection = null;
        InputStream inputStream;
        try {
            //设置下载位置
            URL url = new URL(mUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Range", "bytes=" + mStart + "-" + mEnd);
            //设置文件写入位置
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(mStart);

            connection.connect();
            Log.d(TAG, "run: 网络请求返回码：" + connection.getResponseCode());
            //开始下载
            /**
             * 注意这里添加了Range请求头的返回码是206
             */
            if (connection.getResponseCode() == 206) {
                inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024 * 4];
                int len;//每次读写的长度
                long currentTime = 0;
                int temp = 0;//一段时间内完成了多少
                int sum = 0;//整个线程下载了多少

                while ((len = inputStream.read(buffer)) != -1) {
                    ///写入文件
                    randomAccessFile.write(buffer, 0, len);
                    mFinished = mFinished + len;
                    //反馈下载进度给主线程
                    temp = temp + len;
                    sum = sum + len;
                    //更新线程信息

                    if (System.currentTimeMillis() - currentTime > 1000) {
                        currentTime = System.currentTimeMillis();
                        mDownLoader.updateDownLoadProgress(temp);
                        temp = 0;
                    }
                    //下载暂停的时候保存线程信息,保存文件信息
                    if (isCancelled()) {
                        if (mDownLoadInfo == null) {
                            mDownLoadInfo = new DownLoadInfo();
                            mDownLoadInfo.setId(null);
                        }
                        mDownLoadInfo.setUrl(mUrl);
                        mDownLoadInfo.setTaskId(mTaskId);
                        mDownLoadInfo.setFinished(mFinished);
                        mDownLoadInfo.setIsFinish(false);

                        DaoManager.getInstance().UpdateDownLoadInfo(mDownLoadInfo);
                        return null;
                    }
                }

                mDownLoader.updateDownLoadProgress(temp);
                // 下载完成后，更新线程信息
                if (mDownLoadInfo == null) {
                    mDownLoadInfo = new DownLoadInfo();
                    mDownLoadInfo.setId(null);
                }
                mDownLoadInfo.setIsFinish(true);
                mDownLoadInfo.setFinished(mFinished);
                mDownLoadInfo.setTaskId(mTaskId);
                mDownLoadInfo.setUrl(mUrl);
                //保存下载信息到数据库
                DaoManager.getInstance().UpdateDownLoadInfo(mDownLoadInfo);


                synchronized (this){
                    isFinished = true;
                    if (mDownLoader.checkAllFinished()) {
                        mDownLoader.finishDownLoad();
                    }
                }



            } else {
                Log.d(TAG, "run: 网络请求失败");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute: ");
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG, "onPostExecute: ");
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d(TAG, "onCancelled: ");
    }

    //通知增加已下载的大小
    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
    }


}

