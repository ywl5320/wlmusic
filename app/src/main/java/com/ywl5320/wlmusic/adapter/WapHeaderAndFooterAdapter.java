package com.ywl5320.wlmusic.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * Created by ywl on 2017-7-23.
 */

public class WapHeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int headtype = 0x11111;
    private int normaltype = 0x11112;
    private int foottype = 0x11113;
    private View headerView;
    private View footerView;
    private RecyclerView.Adapter badapter;//目标adapter
    private int mOrientation = -1;
    private OnLoadMoreListener onloadMoreListener;

    public WapHeaderAndFooterAdapter(RecyclerView.Adapter badapter) {
        this.badapter = badapter;
    }

    public void addHeader(View header)
    {
        headerView = header;
    }

    public void removeHeader()
    {
        headerView = null;
    }

    public void addFooter(View footer)
    {
        footerView = footer;
    }

    private boolean loadMore = false;

    //实现加载更多接口
    public void setOnloadMoreListener(RecyclerView recyclerView, final OnLoadMoreListener onloadMoreListener) {

        this.onloadMoreListener = onloadMoreListener;
        if(recyclerView != null && onloadMoreListener != null)
        {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        int lastVisiblePosition = 0;
                        if (layoutManager instanceof LinearLayoutManager)
                        {
                            lastVisiblePosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                        }
                        else if(layoutManager instanceof GridLayoutManager)
                        {
                            lastVisiblePosition = ((GridLayoutManager)layoutManager).findLastVisibleItemPosition();
                        }
                        if(lastVisiblePosition >= layoutManager.getItemCount() - 1 && loadMore){
                            loadMore = false;
                            if(onloadMoreListener != null)
                            {
                                onloadMoreListener.onLoadMore();
                            }
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(dy > 0)
                    {
                        loadMore = true;
                    }
                    else
                    {
                        loadMore = false;
                    }
                }
            });
        }
    }

    /**
     * 根据头部尾部返回相应的type，这里if else没有简写，方便看逻辑
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if(headerView != null && footerView != null)//同时加了头部和尾部
        {
            if(position == 0)//当position为0时，展示header
            {
                return headtype;
            }
            else if(position == getItemCount() - 1)//当position为最后一个时，展示footer
            {
                return foottype;
            }
            else//其他时候就展示原来adapter的
            {
                if(badapter.getItemCount() > 1) {
                    return badapter.getItemViewType(position);
                }
                return normaltype;
            }
        }
        else if(headerView != null) {//只有头部
            if (position == 0)
                return headtype;
            if(badapter.getItemCount() > 1) {
                return badapter.getItemViewType(position);
            }
            return normaltype;
        }
        else if(footerView != null)//只有尾部
        {
            if(position == getItemCount() - 1)
            {
                return foottype;
            }
            else
            {
                if(badapter.getItemCount() > 1) {
                    return badapter.getItemViewType(position);
                }
                return normaltype;
            }
        }
        else {
            if(badapter.getItemCount() > 1) {
                return badapter.getItemViewType(position);
            }
            return normaltype;
        }
    }

    /**
     * 这里就根据getItemViewType返回的值来返回相应的ViewHolder
     * 头部和尾部的ViewHolder只是一个集成RecyclerView.ViewHolder的简单默认类，里面并没有任何处理。
     * 这样就完成了类型的返回了（需注意为什么这样做）
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == headtype)//返回头部的ViewHolder
            return new HeaderViewHolder(headerView);
        else if(viewType == foottype)//返回尾部的ViewHolder
            return new FoogerViewHolder(footerView);//其他就直接返回传入的adapter的ViewHolder
        return badapter.onCreateViewHolder(parent, viewType);
    }

    /**
     * 绑定ViewHolder，当时header或footer时，直接返回，因为不用绑定，
     * 当是传入的adapter时，就直接调用adapter.onBindViewHolder就行了
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(footerView != null)
        {
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onloadMoreListener != null)
                    {
                        onloadMoreListener.onClickLoadMore();
                    }
                }
            });
        }

        if(headerView != null && footerView != null)//有头部和尾部
        {
            if(position == 0)//头部直接返回，无需绑定
            {
                return;
            }
            else if(position == getItemCount() -1)//尾部直接返回，也无需绑定
            {
                return;
            }
            else
            {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onloadMoreListener != null)
                        {
                            onloadMoreListener.onItemClick(v, position - 1);
                        }
                    }
                });
                badapter.onBindViewHolder(holder, position - 1);//其他就调用adapter的绑定方法
            }
        }
        else if(headerView != null)
        {
            if(position == 0)
            {
                return;
            }
            else
            {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onloadMoreListener != null)
                        {
                            onloadMoreListener.onItemClick(v, position - 1);
                        }
                    }
                });
                badapter.onBindViewHolder(holder, position - 1);
            }
        }
        else if(footerView != null)
        {
            if(position == getItemCount() - 1)
            {
                return;
            }
            else
            {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onloadMoreListener != null)
                        {
                            onloadMoreListener.onItemClick(v, position);
                        }
                    }
                });
                badapter.onBindViewHolder(holder, position);
            }
        }
        else
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onloadMoreListener != null)
                    {
                        onloadMoreListener.onItemClick(v, position);
                    }
                }
            });
            badapter.onBindViewHolder(holder, position);
        }
    }

    /**
     * 返回item的数量，
     * @return
     */
    @Override
    public int getItemCount() {

        if(headerView != null && footerView != null)//有头部和尾部，就多了2
        {
            return badapter.getItemCount() + 2;
        }
        else if(headerView != null)//只有头部多了1
        {
            return badapter.getItemCount() + 1;
        }
        else if(footerView != null)//只有尾部也多了1
        {
            return badapter.getItemCount() + 1;
        }
        return badapter.getItemCount();//其他就是默认的值， 不多也不少
    }

    /**
     * 头部的ViewHolder
     */
    private class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 尾部的ViewHolder
     */
    private class FoogerViewHolder extends RecyclerView.ViewHolder
    {
        public FoogerViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 处理当时Gridview类型的效果时，也把头部和尾部设置成一整行（这就是RecyclerView的其中一个优秀之处，列表的每行可以不同数量的列）
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        mOrientation = getOrientation(layoutManager);
        if(layoutManager instanceof GridLayoutManager)
        {
            /**
             * getSpanSize的返回值的意思是：position位置的item的宽度占几列
             * 比如总的是4列，然后头部全部显示的话就应该占4列，此时就返回4
             * 其他的只占一列，所以就返回1，剩下的三列就由后面的item来依次填充。
             */
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(headerView != null && footerView != null)
                    {
                        if(position == 0)
                        {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        else if(position == getItemCount() - 1) {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        else
                        {
                            return 1;
                        }
                    }
                    else if(headerView != null) {
                        if (position == 0) {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        return 1;
                    }
                    else if(footerView != null)
                    {
                        if(position == getItemCount() - 1)
                        {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        return 1;
                    }
                    return 1;
                }
            });

        }
    }

    /**
     * 判断是否到底部了
     * @param recyclerView
     * @return
     */
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if(mOrientation == LinearLayoutManager.VERTICAL) {
            if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
                return true;
        }
        else
        {
            if (recyclerView.computeHorizontalScrollExtent() + recyclerView.computeHorizontalScrollOffset() >= recyclerView.computeHorizontalScrollRange())
                return true;
        }
        return false;
    }

    /**
     * 加载更多回调接口
     */
    public interface OnLoadMoreListener
    {
        void onLoadMore();

        void onClickLoadMore();

        void onItemClick(View view, int position);
    }

    private int getOrientation(RecyclerView.LayoutManager layoutManager)
    {
        int mOrientation = 0;
        Class<?> clazz = null;
        try {
            clazz = Class.forName("android.support.v7.widget.LinearLayoutManager");
            Field field = clazz.getDeclaredField("mOrientation");
            field.setAccessible(true);
            mOrientation = field.getInt(layoutManager);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return mOrientation;
    }

}
