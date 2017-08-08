package com.example.asus.downloaddemo.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.asus.downloaddemo.R;
import com.example.asus.downloaddemo.common.bean.AppInfo;
import com.example.asus.downloaddemo.common.download.DownLoadManager;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by asus on 2017/8/4.
 */

public class DownLoadAdapter extends RecyclerView.Adapter<DownLoadAdapter.DownLoadViewHolder> {

    private static final String TAG = "DownLoadAdapter";

    private List<AppInfo> mAppInfoList;
    private Context mContext;


    public DownLoadAdapter(List<AppInfo> mAppInfoList) {
        this.mAppInfoList = mAppInfoList;
//        EventBus.getDefault().register(this);
    }


    @Override
    public DownLoadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new DownLoadAdapter.DownLoadViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_download, parent, false));
    }

    @Override
    public void onBindViewHolder(final DownLoadViewHolder holder, final int position) {
        final AppInfo appInfo = mAppInfoList.get(position);
        if (appInfo.getStatus() == DownLoadManager.DOWNLOAD_STATUS_FINISHEDED) {
            holder.progressBar.setProgress(100);
            holder.tvFinished.setText("下载完成");
        } else if (appInfo.getStatus() == DownLoadManager.DOWNLOAD_STATUS_ERROR) {
            holder.tvFinished.setText("网络请求出错");
        } else if (appInfo.getStatus() == DownLoadManager.DOWNLOAD_STATUS_WAITING) {
            holder.tvFinished.setText("等待下载");
        } else if (appInfo.getStatus() == DownLoadManager.DOWNLOAD_STATUS_PAUSE) {
            holder.tvFinished.setText("暂停下载");
        } else if (appInfo.getStatus() == DownLoadManager.DOWNLOAD_STATUS_DOWNLOADING) {
            int progress = (int) (appInfo.getFinished() * 100.0 / appInfo.getLength());
            holder.progressBar.setProgress(progress);
            holder.tvFinished.setText(FormetFileSize(appInfo.getFinished()) + "/"
                    + FormetFileSize(appInfo.getLength()) +
                    "\n已下载" + progress + "%");
        }

        int progress = (int) (appInfo.getFinished() * 100.0 / appInfo.getLength());
        holder.progressBar.setProgress(progress);

        holder.tvProgress.setVisibility(View.GONE);

        holder.tvFileName.setText(appInfo.getFileName());
        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 添加下载任务：" + mAppInfoList.get(position).getFileName());
                DownLoadManager.getInstance(mContext).addDownLoad(appInfo.getUrl(), appInfo.getFileName());
            }
        });

        holder.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 移除任务：" + mAppInfoList.get(position).getFileName());
                DownLoadManager.getInstance(mContext).removeDownLoad(appInfo.getUrl());
            }
        });


    }


    @Override
    public int getItemCount() {
        return mAppInfoList.size();
    }

    public static class DownLoadViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        TextView tvProgress;
        TextView tvFinished;
        Button btnStart;
        Button btnPause;
        ProgressBar progressBar;
        CheckBox checkBox;

        public DownLoadViewHolder(View itemView) {
            super(itemView);
            tvFileName = (TextView) itemView.findViewById(R.id.item_tv_file_name);
            btnStart = (Button) itemView.findViewById(R.id.item_btn_start);
            btnPause = (Button) itemView.findViewById(R.id.item_btn_pause);
            progressBar = (ProgressBar) itemView.findViewById(R.id.item_pb_progress);
            tvProgress = (TextView) itemView.findViewById(R.id.item_tv_progress);
            tvFinished = (TextView) itemView.findViewById(R.id.item_tv_finished);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_cb);
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(DownLoadProgressEvent event){
//        AppInfo info=event.getAppInfo();
//        for (int i = 0; i <mAppInfoList.size() ; i++) {
//            if (info.getUrl().endsWith(mAppInfoList.get(i).getUrl())){
//                mAppInfoList.set(i,info);
//                notifyItemChanged(i);
//                break;
//            }
//        }
//    }


    //将单位B格式成MB或者KB

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

}
