package com.example.asus.downloaddemo.common.dao;

import android.content.Context;

import com.example.asus.downloaddemo.common.bean.AppInfo;
import com.example.asus.downloaddemo.common.bean.AppInfoDao;
import com.example.asus.downloaddemo.common.bean.DaoMaster;
import com.example.asus.downloaddemo.common.bean.DaoSession;
import com.example.asus.downloaddemo.common.bean.DownLoadInfo;
import com.example.asus.downloaddemo.common.bean.DownLoadInfoDao;
import com.example.asus.downloaddemo.common.download.DownLoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/3.
 */

public class DaoManager {

    private static final String DB_NAME = "downloadDB";//数据库名称

    private static DaoManager INSTANCE = null;

    private static DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private Context mContext;


    private DaoManager() {
    }

    //暂时先用懒汉式来实现单例模式
    public static DaoManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoManager();
        }
        return INSTANCE;
    }


    public void init(Context context) {
        mContext = context;
        mDaoMaster = getDaoMaster();
    }


    /**
     * 判断数据库是否存在，如果不存在则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {
            mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME);
            mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoMaster;
    }


    //获取某个下载文件的全部线程
    public List<DownLoadInfo> getDownLoadInfoByUrl(String url) {

        List<DownLoadInfo> downLoadInfoList = new ArrayList<>();
        downLoadInfoList.addAll(mDaoSession.getDownLoadInfoDao()
                .queryBuilder()
                .where(DownLoadInfoDao.Properties.Url.eq(url))
                .list());
        return downLoadInfoList;
    }


    //获取之前某个下载线程的信息
    public DownLoadInfo getDownLoadInfoByUrlAndTaskId(String url, int taskId) {
        if (mDaoSession.getDownLoadInfoDao().queryBuilder()
                .where(DownLoadInfoDao.Properties.Url.eq(url), DownLoadInfoDao.Properties.TaskId.eq(taskId))
                .count() == 0) {
            return null;
        } else {
            return mDaoSession.getDownLoadInfoDao().queryBuilder()
                    .where(DownLoadInfoDao.Properties.Url.eq(url), DownLoadInfoDao.Properties.TaskId.eq(taskId))
                    .list()
                    .get(0);
        }

    }





    //更新线程信息
    public void UpdateDownLoadInfo(DownLoadInfo downLoadInfo) {
        if (downLoadInfo == null) {
            return;
        }
        DownLoadInfo info = getDownLoadInfoByUrlAndTaskId(downLoadInfo.getUrl(), downLoadInfo.getTaskId());
        if (info == null) {
            downLoadInfo.setId(null);
        } else {
            downLoadInfo.setId(info.getId());
        }
        mDaoSession.getDownLoadInfoDao().insertOrReplace(downLoadInfo);
    }

    //删除线程信息
    public void deleteDownLoadInfo(String url) {

        List<DownLoadInfo> downLoadInfoList = mDaoSession.getDownLoadInfoDao().queryBuilder()
                .where(DownLoadInfoDao.Properties.Url.eq(url))
                .list();
        mDaoSession.getDownLoadInfoDao().deleteInTx(downLoadInfoList);
    }


    //根据url获取文件信息
    public AppInfo getAppInfoByUrl(String url) {
        if (mDaoSession.getAppInfoDao().queryBuilder()
                .where(AppInfoDao.Properties.Url.eq(url))
                .count() == 0) {
            return null;
        } else {
            return mDaoSession.getAppInfoDao().queryBuilder()
                    .where(AppInfoDao.Properties.Url.eq(url))
                    .list()
                    .get(0);
        }
    }


    //插入或者更新文件信息
    public void insertOrUpdateAppInfo(AppInfo appInfo) {
        if (appInfo == null) {
            return;
        }
        mDaoSession.getAppInfoDao().insertOrReplace(appInfo);
    }

    //获取全部的文件信息
    public List<AppInfo> getAllAppInfo() {
        List<AppInfo> infoList = new ArrayList<>();
        infoList = mDaoSession.getAppInfoDao().loadAll();
        return infoList;
    }

    //获取未完成（完成）的文件
    public List<AppInfo> getFinishedAppInfo(boolean isFinished) {
        List<AppInfo> infoList = new ArrayList<>();
        if (isFinished) {
            infoList = mDaoSession.getAppInfoDao().queryBuilder()
                    .where(AppInfoDao.Properties.Status.eq(DownLoadManager.DOWNLOAD_STATUS_FINISHEDED))
                    .list();
        } else {
            infoList = mDaoSession.getAppInfoDao().queryBuilder()
                    .where(AppInfoDao.Properties.Status.notEq(DownLoadManager.DOWNLOAD_STATUS_FINISHEDED))
                    .list();
        }
        return infoList;
    }


}
