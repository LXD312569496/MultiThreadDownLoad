package com.example.asus.downloaddemo.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.downloaddemo.R;
import com.example.asus.downloaddemo.common.bean.AppInfo;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by asus on 2017/8/8.
 */

public class FinishAdapter extends RecyclerView.Adapter<FinishAdapter.FinishViewHolder> {

    private List<AppInfo> appInfoList;

    public FinishAdapter(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    @Override
    public FinishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FinishViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finish, parent, false));
    }

    @Override
    public void onBindViewHolder(FinishViewHolder holder, int position) {
        AppInfo info = appInfoList.get(position);
        holder.tvFileName.setText(info.getFileName());
        holder.tvLength.setText(FormetFileSize(info.getLength()));

    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }

    public static class FinishViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvFileName;
        TextView tvLength;
        Button btnInstall;

        public FinishViewHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.finish_iv);
            tvFileName= (TextView) itemView.findViewById(R.id.item_tv_name);
            tvLength= (TextView) itemView.findViewById(R.id.item_tv_length);
            btnInstall= (Button) itemView.findViewById(R.id.item_btn_install);
        }
    }


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
