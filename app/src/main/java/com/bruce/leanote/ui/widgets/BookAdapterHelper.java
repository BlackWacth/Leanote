package com.bruce.leanote.ui.widgets;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bruce.leanote.utils.L;
import com.bruce.leanote.utils.ScreenUtils;

/**
 *
 * Created by Bruce on 2017/4/14.
 */
public class BookAdapterHelper {

    public static final int ITEM_MARGIN = 15;
    public static final int SHOW_SIDE_ITEM_WIDTH = 30;
    public static final float WIDTH_HEIGHT_RATE = 1.5f;

    public void onCreateViewHolder(ViewGroup parent, View itemView) {
//        final FrameLayout.LayoutParams itemParams = (FrameLayout.LayoutParams) parent.getLayoutParams();
//        itemParams.width = parent.getWidth() - ScreenUtils.dip2px(parent.getContext(), 2 * (ITEM_MARGIN + SHOW_SIDE_ITEM_WIDTH));
//        itemParams.height = (int) (itemParams.width * WIDTH_HEIGHT_RATE);
//        itemView.setLayoutParams(itemParams);

        int width = parent.getWidth() - ScreenUtils.dip2px(parent.getContext(), 2 * (ITEM_MARGIN + SHOW_SIDE_ITEM_WIDTH));
        int height = (int) (width * WIDTH_HEIGHT_RATE);
        final FrameLayout.LayoutParams itemParams = new FrameLayout.LayoutParams(width, height, Gravity.CENTER_VERTICAL);
        itemView.setLayoutParams(itemParams);

        final FrameLayout.LayoutParams parentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, itemParams.height, Gravity.CENTER_VERTICAL);
        parent.setLayoutParams(parentParams);
    }

    public void onBindViewHolder(View itemView, int position, int itemCount) {
        int margin = ScreenUtils.dip2px(itemView.getContext(), ITEM_MARGIN);
        int leftMargin = position == 0 ? margin + ScreenUtils.dip2px(itemView.getContext(), SHOW_SIDE_ITEM_WIDTH) : 0;
        int rightMargin = position == itemCount - 1 ? margin + ScreenUtils.dip2px(itemView.getContext(), SHOW_SIDE_ITEM_WIDTH) : margin;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        params.setMargins(leftMargin, 0, rightMargin, 0);
        itemView.setLayoutParams(params);
    }
}
