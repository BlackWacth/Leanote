package com.bruce.notebooklist.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.notebooklist.R;
import com.bruce.notebooklist.bean.Notebook;

import java.util.ArrayList;

/**
 *
 * Created by Bruce on 2017/7/6.
 */
public class PopupRecyclerView extends RelativeLayout implements RecyclerAdapter.OnItemClickListener {

    private static final String TAG = "PopupRecyclerView";

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private LinearLayout mExtendLayout = null;
    private View mExtendTopView = null;
    private View mExtendContentView = null;

    private int mStartY;
    private int mTopHeight;
    private boolean isExtended = false;
    private int mScreenHeight;

    private ObjectAnimator mObjectAnimator;

    private OnPopupItemClickListener mOnPopupItemClickListener;

    public PopupRecyclerView(Context context) {
        super(context);
        init();
    }

    public PopupRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PopupRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTopHeight = getTopHeight();
        mScreenHeight = getScreenHeight(getContext());
        mRecyclerView = new RecyclerView(getContext());
        RecyclerView.LayoutParams recyclerViewParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        mRecyclerView.setLayoutParams(recyclerViewParams);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addView(mRecyclerView);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        mRecyclerView.setLayoutAnimation(controller);


        LinearLayout.LayoutParams extendParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mExtendLayout = new LinearLayout(getContext());
        mExtendLayout.setLayoutParams(extendParams);
        mExtendLayout.setOrientation(LinearLayout.VERTICAL);
        mExtendLayout.setVisibility(GONE);
        addView(mExtendLayout);

        mExtendTopView = LayoutInflater.from(getContext()).inflate(R.layout.item_popup_recycler_view_layout, mExtendLayout, false);
        mExtendContentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_content_layout, mExtendLayout, false);
    }

    public void bindRecyclerAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return;
        }
        mRecyclerView.setAdapter(adapter);
        mRecyclerAdapter = (RecyclerAdapter) adapter;
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(RecyclerAdapter adapter, View view, int position) {
        if(adapter.getList().get(position).type.equals(Notebook.TYPE_FOLDER)) {
            int[] pos = new int[2];
            view.getLocationOnScreen(pos);
            Log.i(TAG, pos[0] + " : " + pos[1]);
            mStartY = pos[1] - mTopHeight;
            zoomIn(adapter.getList().get(position), mStartY);
            if(mOnPopupItemClickListener != null) {
                mOnPopupItemClickListener.onItemClick(adapter.getList().get(position).title, false);
            }
        } else {
            if(mOnPopupItemClickListener != null) {
                mOnPopupItemClickListener.onItemClick(adapter.getList().get(position).title, true);
            }
        }
    }

    private void zoomIn(Notebook notebook, int startY) {
        Log.i(TAG, "zoomIn");
        mRecyclerView.setVisibility(GONE);
        setupExtendTopView(notebook);
        mExtendTopView.setY(startY);
        mExtendLayout.removeAllViews();
        mExtendLayout.addView(mExtendTopView);
        mExtendLayout.setVisibility(VISIBLE);
        transitionYAnimation(mExtendTopView, mStartY, 0, 100, null, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExtended = true;
            }
        });
        mExtendLayout.addView(mExtendContentView);
        mExtendContentView.setY(mScreenHeight);
        setupExtendContentView();
        transitionYAnimation(mExtendContentView, mScreenHeight, mTopHeight, 100, null, null);
    }

    private void setupExtendContentView() {
        RecyclerView recyclerView = (RecyclerView) mExtendContentView.findViewById(R.id.rv_popup_content_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Notebook> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new Notebook(Notebook.TYPE_FILE, "file " + i, "/Android"));
        }
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(list);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapter adapter, View view, int position) {
                if(mOnPopupItemClickListener != null) {
                    mOnPopupItemClickListener.onItemClick("内部文件夹 ： " + adapter.getList().get(position).title, true);
                }
            }
        });
    }

    public void zoomOut() {
        if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
            mObjectAnimator.cancel();
        }
        transitionYAnimation(mExtendTopView, 0, mStartY, 0, null, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExtended = false;
                mExtendLayout.removeAllViews();
                mExtendLayout.setVisibility(GONE);
                mExtendLayout.setAlpha(1);
                mRecyclerView.setVisibility(VISIBLE);
            }
        });
        transitionYAnimation(mExtendContentView, mTopHeight, mScreenHeight, 0, null, null);
    }

    private void transitionYAnimation(View view, int startY, int endY, int delay, ValueAnimator.AnimatorUpdateListener updateListener, ValueAnimator.AnimatorListener animatorListener) {
        mObjectAnimator = ObjectAnimator.ofFloat(view, View.Y, startY, endY);
        mObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mObjectAnimator.setDuration(300);
        mObjectAnimator.setStartDelay(delay);
        if (updateListener != null) {
            mObjectAnimator.addUpdateListener(updateListener);
        }
        if (animatorListener != null) {
            mObjectAnimator.addListener(animatorListener);
        }
        mObjectAnimator.start();
    }

    private int getTopHeight() {
        int actionBarHeight = 0;
        TypedValue value = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, value, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(value.data, getResources().getDisplayMetrics());
        }
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "actionBarHeight = " + actionBarHeight + " ---- statusBarHeight = " + statusBarHeight);
        return actionBarHeight + statusBarHeight;
    }


    public void setupExtendTopView(Notebook notebook) {
        ImageView type = (ImageView) mExtendTopView.findViewById(R.id.iv_item_type);
        TextView title = (TextView) mExtendTopView.findViewById(R.id.tv_item_title);
        TextView path = (TextView) mExtendTopView.findViewById(R.id.tv_item_path);
        if (notebook.type.equals(Notebook.TYPE_FILE)) {
            type.setImageResource(R.drawable.ic_file);
        } else if (notebook.type.equals(Notebook.TYPE_FOLDER)) {
            type.setImageResource(R.drawable.ic_folder);
        }
        title.setText(notebook.title);
        path.setText(notebook.path);
    }

    public boolean isExtended() {
        return isExtended;
    }

    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point p = new Point();
        wm.getDefaultDisplay().getSize(p);
        return p.y;
    }

    public OnPopupItemClickListener getOnPopupItemClickListener() {
        return mOnPopupItemClickListener;
    }

    public void setOnPopupItemClickListener(OnPopupItemClickListener onPopupItemClickListener) {
        mOnPopupItemClickListener = onPopupItemClickListener;
    }

    public interface OnPopupItemClickListener {
        void onItemClick(String text, boolean isFile);
    }
}
