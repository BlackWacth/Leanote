package com.bruce.leanote.ui.widgets;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bruce.leanote.utils.L;
import com.bruce.leanote.utils.ScreenUtils;

/**
 * 动态修改item的大小和margin
 * Created by Bruce on 2017/4/14.
 */
public class BookAdapterHelper {

    /**item 间距 */
    public static final int ITEM_MARGIN = 10;

    /**屏幕两端显示出的item宽度 */
    public static final int SHOW_SIDE_ITEM_WIDTH = 30;

    /**中间item高宽比：height/width */
    public static final float WIDTH_HEIGHT_RATE = 1.5f;

    public void onCreateViewHolder(ViewGroup parent, View itemView) {
//        L.i("parentWidth = " + parent.getWidth() + ", parent = " + parent.toString());
        int width = parent.getWidth() - ScreenUtils.dip2px(parent.getContext(), 2 * (ITEM_MARGIN + SHOW_SIDE_ITEM_WIDTH));
        int height = (int) (width * WIDTH_HEIGHT_RATE);
//        L.i("width = " + width + ", height = " + height);

        final RecyclerView.LayoutParams itemParams = new RecyclerView.LayoutParams(width, height);
        itemParams.topMargin = (parent.getHeight() - height) / 3;
        itemView.setLayoutParams(itemParams);
    }

    public void onBindViewHolder(View itemView, int position, int itemCount) {
        int margin = ScreenUtils.dip2px(itemView.getContext(), ITEM_MARGIN);
        int leftMargin = position == 0 ? margin + ScreenUtils.dip2px(itemView.getContext(), SHOW_SIDE_ITEM_WIDTH) : margin;
        int rightMargin = position == itemCount - 1 ? margin + ScreenUtils.dip2px(itemView.getContext(), SHOW_SIDE_ITEM_WIDTH) : margin;

        RecyclerView.LayoutParams itemParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        itemParams.leftMargin = leftMargin;
        itemParams.rightMargin = rightMargin;
        itemView.setLayoutParams(itemParams);
    }
}
