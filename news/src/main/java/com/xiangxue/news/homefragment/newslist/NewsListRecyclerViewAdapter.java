package com.xiangxue.news.homefragment.newslist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xiangxue.news.R;
import com.xiangxue.news.homefragment.api.NewsListBean;
import com.xiangxue.webview.WebviewActivity;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_PICTURE_TITLE = 1;
    private final int VIEW_TYPE_TITLE = 2;
    private List<NewsListBean.Contentlist> mItems;
    private Context mContext;

    /**
     * 1.构造适配器
     * @param context
     */
    NewsListRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * 2.设置数据
     * @param items
     */
    void setData(List<NewsListBean.Contentlist> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    /**
     * 3.获取item数量
     * @return
     */
    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    /**
     * 获取item类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mItems != null && mItems.get(position).imageurls != null && mItems.get(position).imageurls.size() > 1) {
            return VIEW_TYPE_PICTURE_TITLE;
        }
        return VIEW_TYPE_TITLE;
    }

    /**
     * 根据类型创建 viewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_PICTURE_TITLE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.picture_title_view, parent, false);
            return new PictureTitleViewHolder(view);
        } else if (viewType == VIEW_TYPE_TITLE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.title_view, parent, false);
            return new TitleViewHolder(view);
        }

        return null;
    }

    private class PictureTitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public AppCompatImageView picutureImageView;

        public PictureTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            picutureImageView = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebviewActivity.startCommonWeb(mContext, "News", v.getTag()+"");
                }
            });
        }
    }


    private class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebviewActivity.startCommonWeb(mContext, "News", v.getTag()+"");
                }
            });
        }
    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(mItems.get(position).link);
        if(holder instanceof PictureTitleViewHolder){
            ((PictureTitleViewHolder) holder).titleTextView.setText(mItems.get(position).title);
            Glide.with(holder.itemView.getContext())
                    .load(mItems.get(position).imageurls.get(0).url)
                    .transition(withCrossFade())
                    .into(((PictureTitleViewHolder) holder).picutureImageView);
        } else if(holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).titleTextView.setText(mItems.get(position).title);
        }
    }
}
