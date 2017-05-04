package com.bruce.leanote.ui.widgets;

import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bruce.leanote.utils.L;
import com.bruce.leanote.utils.ScreenUtils;

/**
 * Created by Bruce on 2017/5/4.
 */

public class BookScaleHelper extends RecyclerView.OnScrollListener {

    private RecyclerView mRecyclerView;
    private float mScaleFactor = 0.85f;
    private int mShowSideItemWidth = BookAdapterHelper.SHOW_SIDE_ITEM_WIDTH;
    private int mItemMargin = BookAdapterHelper.ITEM_MARGIN;

    private int mRecyclerViewWidth;
    private int mItemViewWidth;
    private int mOnePagerWidth;

    private int mCurrentItemPos;
    private int mCurrentItemOffset;

    private BookLinearSnapHelper mBookLinearSnapHelper;

    public void attachRecyclerView(final RecyclerView recyclerView) {

        mRecyclerView = recyclerView;
        mRecyclerView.addOnScrollListener(this);

        initItemView();

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRecyclerView);

//        mBookLinearSnapHelper = new BookLinearSnapHelper();
//        mBookLinearSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initItemView() {

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewWidth = mRecyclerView.getWidth();
                mItemViewWidth = mRecyclerViewWidth - ScreenUtils.dip2px(mRecyclerView.getContext(), (mShowSideItemWidth + mItemMargin) * 2);
                mOnePagerWidth = mItemViewWidth;
                L.i("mOnePagerWidth = " + mOnePagerWidth);
                onScrollChangeCallback();
            }
        });
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //newState三种状态
        //SCROLL_STATE_IDLE 静止，没有滚动。
        //SCROLL_STATE_DRAGGING 正在被拖拽，一般为用户用手指在滚动
        //SCROLL_STATE_SETTLING 自动滚动

//        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//            mBookLinearSnapHelper.mNoNeedToScroll = mCurrentItemOffset == 0 || mCurrentItemOffset == getDestItemOffset(mRecyclerView.getAdapter().getItemCount() - 1);
//        } else {
//            mBookLinearSnapHelper.mNoNeedToScroll = false;
//        }
    }

    private int getDestItemOffset(int destPos) {
        return mOnePagerWidth * destPos;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // dx > 0 表示从右向左滑
        // dx < 0 表示从左向右滑
        // dy < 0 表示从上向下滑
        // dy > 0 表示从下向上滑
        mCurrentItemOffset += dx;
        L.i("dx = " + dx + ", mCurrentItemOffset = " + mCurrentItemOffset);
        computeCurrentItemPos();
        onScrollChangeCallback();
    }

    private void computeCurrentItemPos() {
        if(mOnePagerWidth <= 0) {
            return;
        }
        if(Math.abs(mCurrentItemOffset - mCurrentItemPos * mOnePagerWidth) >= mOnePagerWidth) {
            mCurrentItemPos = mCurrentItemOffset / mOnePagerWidth;
            L.i("mCurrentItemPos = " + mCurrentItemPos);
        }
    }

    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private void onScrollChangeCallback() {
        int offset = mCurrentItemOffset - mCurrentItemPos * (mOnePagerWidth + ScreenUtils.dip2px(mRecyclerView.getContext(), mItemMargin * 2));
        float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePagerWidth, 0.0001);
        L.i("percent = " + percent + ", offset = " + offset + ", mOnePagerWidth = " + mOnePagerWidth);

        View leftView = null;
        View rightView = null;
        View currentView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos);

        if(mCurrentItemPos > 0) {
            leftView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos - 1);
        }
        if(mCurrentItemPos < mRecyclerView.getAdapter().getItemCount() - 1) {
            rightView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos + 1);
        }

        float sideScale = (1 - mScaleFactor) * percent + mScaleFactor;
        float currentScale = (mScaleFactor - 1) * percent + 1;
        L.i("sideScale = " + sideScale + ", currentScale = " + currentScale);

        if(leftView != null) {
            leftView.setScaleY(sideScale);
        }

        if(currentView != null) {
            currentView.setScaleY(currentScale);
        }

        if(rightView != null) {
            rightView.setScaleY(sideScale);
        }
    }
}
