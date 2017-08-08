package com.example.asus.downloaddemo.common.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.asus.downloaddemo.common.bean.AppInfo;
import com.example.asus.downloaddemo.common.dao.DaoManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 东 on 2017/8/2.
 * 下载管理者
 * <p>
 * 下载进度通过EventBus利用DownLoadProgressEvent进行发送和接受
 * 下载进度事件，可读取进度或者下载状态改变
 *
 */

public class DownLoadManager {
    private static final String TAG = "DownLoadManager";

    public static final int DOWNLOAD_STATUS_FINISHEDED = 1;
    public static final int DOWNLOAD_STATUS_UNCOMPLETED = 2;
    public static final int DOWNLOAD_STATUS_DOWNLOADING = 0;
    public static final int DOWNLOAD_STATUS_PAUSE = 3;
    public static final int DOWNLOAD_STATUS_CANCEL = 4;
    public static final int DOWNLOAD_STATUS_ERROR = 5;
    public static final int DOWNLOAD_STATUS_WAITING = 6;


    public static final String SAVE_PATH = Environment.getExternalStorageDirectory()
            + "/downloadtest/";

    private static DownLoadManager INSTANCE = null;

    private ConcurrentHashMap<String, DownLoader> mDownLoaderMap;//下载任务列表
    private Context mContext;
    private List<AppInfo> mAppInfoList;//所有app文件信息


    private DownLoadManager(Context context) {
        this.mContext = context;
        DaoManager.getInstance().init(context);
        mDownLoaderMap = new ConcurrentHashMap<>();
        mAppInfoList = new ArrayList<>();
        init();

    }

    private void init() {
        //获取全部未下载完成的下载任务
        mAppInfoList = DaoManager.getInstance().getAllAppInfo();
        for (int i = 0; i < mAppInfoList.size(); i++) {
            if (mAppInfoList.get(i).getStatus() == DownLoadManager.DOWNLOAD_STATUS_PAUSE) {
                mDownLoaderMap.put(mAppInfoList.get(i).getUrl(), new DownLoader(mContext, mAppInfoList.get(i)));
            }
        }
    }


    public List<AppInfo> getAllAppInfoList() {
        return mAppInfoList;
    }

    public static DownLoadManager getInstance(Context context) {
        if (null == INSTANCE) {
            synchronized (DownLoadManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new DownLoadManager(context);
                }
            }
        }
        return INSTANCE;
    }

    //判断某个任务是否正在下载中
    public boolean isDownLoading(String url){
        return mDownLoaderMap.get(url)!=null;
    }


    //添加下载任务
    public void addDownLoad(String url, String fileName) {
        //如果下载任务不存在
        DownLoader downLoader;
        AppInfo appInfo = DaoManager.getInstance().getAppInfoByUrl(url);
        if (appInfo == null) {
            appInfo = new AppInfo(fileName, url, 0, 0, DownLoadManager.DOWNLOAD_STATUS_PAUSE);
            mAppInfoList.add(appInfo);
        }
        if (mDownLoaderMap.get(url) == null) {
            downLoader = new DownLoader(mContext, appInfo);
            mDownLoaderMap.put(url, downLoader);
        } else {
            downLoader = mDownLoaderMap.get(url);
        }

        Log.d(TAG, "addDownLoad: 添加下载任务:" + url);
        downLoader.initDownLoad();
    }


    //暂停某个下载任务
    public void removeDownLoad(String url) {
        Log.d(TAG, "removeDownLoad: 移除下载任务:" + url);
        DownLoader downLoader = mDownLoaderMap.get(url);
        downLoader.pauseDownLoad();
        mDownLoaderMap.remove(url);//移除下载队列
    }

    //暂停所有下载任务
    public void pauseAllDownLoad() {
        Set<String> set = mDownLoaderMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            removeDownLoad(iterator.next());
        }
    }


}
