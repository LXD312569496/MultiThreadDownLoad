package com.example.asus.downloaddemo;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.asus.downloaddemo.FileInfo;
import com.example.asus.downloaddemo.ThreadInfo;

import com.example.asus.downloaddemo.FileInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig fileInfoDaoConfig;
    private final DaoConfig threadInfoDaoConfig;

    private final FileInfoDao fileInfoDao;
    private final ThreadInfoDao threadInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        fileInfoDaoConfig = daoConfigMap.get(FileInfoDao.class).clone();
        fileInfoDaoConfig.initIdentityScope(type);

        threadInfoDaoConfig = daoConfigMap.get(ThreadInfoDao.class).clone();
        threadInfoDaoConfig.initIdentityScope(type);

        fileInfoDao = new FileInfoDao(fileInfoDaoConfig, this);
        threadInfoDao = new ThreadInfoDao(threadInfoDaoConfig, this);

        registerDao(FileInfo.class, fileInfoDao);
        registerDao(ThreadInfo.class, threadInfoDao);
    }
    
    public void clear() {
        fileInfoDaoConfig.clearIdentityScope();
        threadInfoDaoConfig.clearIdentityScope();
    }

    public FileInfoDao getFileInfoDao() {
        return fileInfoDao;
    }

    public ThreadInfoDao getThreadInfoDao() {
        return threadInfoDao;
    }

}
