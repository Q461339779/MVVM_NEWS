package com.xiangxue.news.homefragment.headlinenews;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.FragmentHomeBinding;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsChannelsBean;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HeadlineNewsFragment extends Fragment {
    public HeadlineNewsFragmentAdapter adapter;
    FragmentHomeBinding viewDataBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        adapter = new HeadlineNewsFragmentAdapter(getChildFragmentManager());
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewDataBinding.viewpager.setAdapter(adapter);
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager);
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
        load();
        return viewDataBinding.getRoot();
    }

    protected void load() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsChannels()
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsChannelsBean>() {
                    @Override
                    public void onSuccess(NewsChannelsBean newsChannelsBean) {
                        Log.e("MainActivity", new Gson().toJson(newsChannelsBean));
                        ArrayList<HeadlineNewsFragmentAdapter.Channel> channels = new ArrayList<>();
                        for (NewsChannelsBean.ChannelList source : newsChannelsBean.showapiResBody.channelList) {
                            HeadlineNewsFragmentAdapter.Channel channel = new HeadlineNewsFragmentAdapter.Channel();
                            channel.channelId = source.channelId;
                            channel.channelName = source.name;
                            channels.add(channel);
                        }
                        adapter.setChannels(channels);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }
}
