package com.xiangxue.news.homefragment.newslist;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.FragmentNewsBinding;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListFragment extends Fragment {
    private NewsListRecyclerViewAdapter mAdapter;
    private FragmentNewsBinding viewDataBinding;

    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";
    private int mPage = 1;

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        mAdapter = new NewsListRecyclerViewAdapter(getContext());
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.listview.setAdapter(mAdapter);
        load();
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 0;
                load();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                load();
            }
        });
        return viewDataBinding.getRoot();
    }
    List<NewsListBean.Contentlist> contentlist = new ArrayList<>();
    @SuppressLint("CheckResult")
    protected void load() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID),
                        getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME), String.valueOf(mPage))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>() {
                    @Override
                    public void onSuccess(NewsListBean newsChannelsBean) {
                        if(mPage == 0) {
                            contentlist.clear();
                        }
                        contentlist.addAll(newsChannelsBean.showapiResBody.pagebean.contentlist);
                        mAdapter.setData(contentlist);
                        mPage ++;
                        viewDataBinding.refreshLayout.finishRefresh();
                        viewDataBinding.refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }
}
