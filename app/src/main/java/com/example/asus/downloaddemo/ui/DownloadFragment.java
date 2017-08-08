package com.example.asus.downloaddemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.downloaddemo.R;
import com.example.asus.downloaddemo.common.bean.AppInfo;
import com.example.asus.downloaddemo.common.dao.DaoManager;
import com.example.asus.downloaddemo.common.download.DownLoadManager;
import com.example.asus.downloaddemo.common.download.DownLoadProgressEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2017/7/29.
 * 下载中的页面
 */

public class DownloadFragment extends Fragment {
    private static final String TAG = "DownloadFragment";

    View mRootView;

    @BindView(R.id.download_rv)
    RecyclerView mRv_Download;
    @BindView(R.id.download_fab_add)
    FloatingActionButton mFabAdd;

    private List<AppInfo> mAppInfoList;
    private DownLoadAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_download, container, false);
        ButterKnife.bind(this, mRootView);
        EventBus.getDefault().register(this);

        initView();
        return mRootView;
    }

    private void initView() {
        mAppInfoList = new ArrayList<>();
        //加载未完成下载的文件信息
        mAppInfoList.addAll(DaoManager.getInstance().getFinishedAppInfo(false));

//        mAppInfoList.add(new AppInfo("测试.jpg","http://img0.imgtn.bdimg.com/it/u=1738728778,7298606&fm=26&gp=0.jpg"
//                ,0,0, DownLoadManager.DOWNLOAD_STATUS_PAUSE));
//        appInfoList.add(new AppInfo("测试1.jpg","http://pic49.nipic.com/file/20140927/19617624_230415502002_2.jpg"
//                ,0,0,DownLoadManager.DOWNLOAD_STATUS_PAUSE));
//        appInfoList.add(new AppInfo("测试2.jpg","http://img3.redocn.com/tupian/20150430/mantenghuawenmodianshiliangbeijing_3924704.jpg"
//                ,0,0,DownLoadManager.DOWNLOAD_STATUS_PAUSE));
//        appInfoList.add(new AppInfo("测试3.jpg","http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=60aeee5da74bd11310c0bf7132c6ce7a/72f082025aafa40fe3c0c4f3a164034f78f0199d.jpg"
//                ,0,0,DownLoadManager.DOWNLOAD_STATUS_PAUSE));
//        appInfoList.add(new AppInfo("测试.mp3","http://yinyueshiting.baidu.com/data2/music/9727ac62b42dc08f5460edd9e493d901/257538763/257535276216000320.mp3?xcode=32681d13d309be884394f800f7f2550e"
//                ,0,0, DownLoadManager.DOWNLOAD_STATUS_PAUSE));
//                mAppInfoList.add(new AppInfo("测试1.mp3","http://yinyueshiting.baidu.com/data2/music/bf41d712f0018a8585dae5c13487ce9c/123514697/1233390301501707661128.mp3?xcode=12b6dc713296e90a2e999d51bc758b81"
//                        ,0,0, DownLoadManager.DOWNLOAD_STATUS_PAUSE));




        adapter = new DownLoadAdapter(mAppInfoList);
        mRv_Download.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRv_Download.setAdapter(adapter);

    }


    //添加下载任务
    @OnClick(R.id.download_fab_add)
    public void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_download_add, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText etUrl = (EditText) view.findViewById(R.id.add_et_url);
        final EditText etFileName = (EditText) view.findViewById(R.id.add_et_file_name);
        Button btDownLoad = (Button) view.findViewById(R.id.add_btn_download);

        btDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString();
                String fileName = etFileName.getText().toString();

                if (!isHttpUrl(url)){
                    Toast.makeText(getActivity(), "添加任务失败，请输入正确的下载地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isFinish(url)) {
                    Toast.makeText(getActivity(), "添加任务失败，下载任务已经下载完成过", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DownLoadManager.getInstance(getActivity()).isDownLoading(url)){
                    Toast.makeText(getActivity(), "添加任务失败，下载任务已存在", Toast.LENGTH_SHORT).show();
                    return;
                }


                mAppInfoList.add(new AppInfo(fileName,url,0,0,DownLoadManager.DOWNLOAD_STATUS_PAUSE));
                adapter.notifyDataSetChanged();
              DownLoadManager.getInstance(getActivity()).addDownLoad(url,fileName);

                dialog.dismiss();
            }
        });
    }

    //判断某个文件是否已经下载成功
    public boolean isFinish(String url){
        AppInfo appInfo=DaoManager.getInstance().getAppInfoByUrl(url);
        if (appInfo==null){
            return false;
        }
        if (appInfo.getStatus()==DownLoadManager.DOWNLOAD_STATUS_FINISHEDED){
            return true;
        }
        return false;
    }


    public  boolean isHttpUrl(String url) {
        Pattern p = Pattern
                .compile("^^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        Matcher m = p.matcher(url);
        return m.matches();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownLoadProgressEvent event){
        AppInfo info=event.getAppInfo();
        for (int i = 0; i <mAppInfoList.size() ; i++) {
            if (info.getUrl().equals(mAppInfoList.get(i).getUrl())){
                mAppInfoList.set(i,info);
                adapter.notifyItemChanged(i);
                if (info.getLength()==info.getFinished()){
                    mAppInfoList.remove(i);
                    adapter.notifyItemRemoved(i);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }
        Log.d("test","DownLoadProgressEvent:"+event.getAppInfo().getFinished()+"/"+event.getAppInfo().getLength());
    }

}
