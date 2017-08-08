package com.example.asus.downloaddemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.downloaddemo.R;
import com.example.asus.downloaddemo.common.bean.AppInfo;
import com.example.asus.downloaddemo.common.dao.DaoManager;
import com.example.asus.downloaddemo.common.download.DownLoadProgressEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 2017/7/29.
 * 下载完成的页面
 */

public class FinishFragment extends Fragment {

    View mRootView;
    @BindView(R.id.finish_rv)
    RecyclerView mRvFinish;

    private List<AppInfo> mAppInfoList;
    private FinishAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_finish, container, false);
        ButterKnife.bind(this, mRootView);
        EventBus.getDefault().register(this);

        initView();

        return mRootView;
    }

    private void initView() {
        mAppInfoList = new ArrayList<>();
        mAppInfoList = DaoManager.getInstance().getFinishedAppInfo(true);

        mAdapter = new FinishAdapter(mAppInfoList);
        mRvFinish.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvFinish.setAdapter(mAdapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownLoadProgressEvent event) {
        AppInfo info = event.getAppInfo();
        if (info.getLength() == info.getFinished()) {
            mAppInfoList.add(info);
            mAdapter.notifyDataSetChanged();

        }
    }


}
