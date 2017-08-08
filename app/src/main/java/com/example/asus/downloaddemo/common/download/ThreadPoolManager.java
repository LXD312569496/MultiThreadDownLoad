package com.example.asus.downloaddemo.common.download;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by asus on 2017/8/2.
 * 线程池的封装类
 */

public class ThreadPoolManager {
    private static ThreadPoolManager Instance;

    private static ThreadPoolExecutor sExecutor;



    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime;
    private TimeUnit unit;

    private ThreadPoolManager(){
        //当前设备可用处理器核心数*2 + 1,能够让cpu的效率得到最大程度执行
        corePoolSize = Runtime.getRuntime().availableProcessors()*2+1;
        maximumPoolSize=corePoolSize;
        keepAliveTime=1;
        unit=TimeUnit.HOURS;
        sExecutor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,
                new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    //单例模式，获取线程池的实例
    public  static ThreadPoolManager getInstance(){
        if(Instance == null){
            synchronized (ThreadPoolManager.class){
                if(Instance == null){
                    int threadCount = Runtime.getRuntime().availableProcessors();
                    Instance = new ThreadPoolManager();
                }
            }
        }
        return Instance;
    }


    /**
     * 执行任务
     * @param runnable
     */
    public static void execute(Runnable runnable){
        sExecutor.execute(runnable);
    }

    /**
     * 从线程池中移除任务
     */
    public void remove(Runnable runnable){
        if(runnable==null)return;
        sExecutor.remove(runnable);
    }


}
