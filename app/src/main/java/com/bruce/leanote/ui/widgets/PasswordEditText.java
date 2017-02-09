package com.bruce.leanote.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import com.bruce.leanote.R;
import com.bruce.leanote.utils.L;

/**
 * 密码输入器，可切换是否可见
 * Created by Bruce on 2017/2/9.
 */
public class PasswordEditText extends AppCompatEditText{

    //默认密码可见图标
    private static final int DEFAULT_SHOW_PASSWORD_ICON = R.drawable.ic_visibility_black_24dp;
    //默认密码不可见图标
    private static final int DEFAULT_HIDE_PASSWORD_ICON = R.drawable.ic_visibility_off_black_24dp;

    private static final int TYPE_SHOW_PASSWORD = EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
    private static final int TYPE_HIDE_PASSWORD = EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD;

    /**是否显示图标 */
    private boolean isShowIcon = false;
    /**是否显示密码 */
    private boolean isShowPassword = false;

    private Drawable mShowPwdDrawable;
    private Drawable mHidePwdDrawable;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if(attrs != null) {
            L.i("loading attrs");
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
            try {
                mShowPwdDrawable = ta.getDrawable(R.styleable.PasswordEditText_icon_show_password);
                if(mShowPwdDrawable == null) {
                    mShowPwdDrawable = ContextCompat.getDrawable(getContext(), DEFAULT_SHOW_PASSWORD_ICON);
                }
                mHidePwdDrawable = ta.getDrawable(R.styleable.PasswordEditText_icon_hide_password);
                if(mHidePwdDrawable == null) {
                    mHidePwdDrawable = ContextCompat.getDrawable(getContext(), DEFAULT_HIDE_PASSWORD_ICON);
                }
            } finally {
                ta.recycle();
            }
        }
        L.i("PasswordEditText");
        setPasswordShowType(false);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                L.i("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                L.i("onTextChanged = " + s);
                if (s.length() > 0) {
                    isShowIcon = true;
                    setIconState(true);
                } else {
                    isShowIcon = false;
                    isShowPassword = false;
                    setIconState(false);
                    setPasswordShowType(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                L.i("afterTextChanged");
            }
        });
    }

    private void setIconState(boolean isShowIcon) {
        if(isShowIcon) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, isShowPassword ? mShowPwdDrawable : mHidePwdDrawable, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    /**
     * 改变密码显示状态
     * @param isShow
     */
    private void setPasswordShowType(boolean isShow) {
        if (isShow) {
            setInputType(TYPE_SHOW_PASSWORD);
        } else {
            setInputType(TYPE_HIDE_PASSWORD);
        }
        //移动光标
        setSelection(getText().length());
    }

    /**
     * 保存状态
     * @return
     */
    @Override
    public Parcelable onSaveInstanceState() {
        L.i("onSaveInstanceState");
        Parcelable state = super.onSaveInstanceState();
        return new PasswordSaveState(state, isShowPassword);
    }

    /**
     * 恢复状态
     * @param state
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        L.i("onRestoreInstanceState");
        PasswordSaveState saveState = (PasswordSaveState) state;
        super.onRestoreInstanceState(saveState.getSuperState());
        isShowPassword = saveState.isShowingPwd();
        setPasswordShowType(isShowPassword);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在图标不显示的情况，不做处理

        L.i("onTouchEvent isShowIcon = " + isShowIcon);
        if(!isShowIcon) {
            return super.onTouchEvent(event);
        }
        L.i("onTouchEvent");
        final Drawable drawable = isShowPassword ? mShowPwdDrawable : mHidePwdDrawable;
        final Rect bounds = drawable.getBounds();

        int iconX = (int) getTopRightPointF().x;
        int leftIconX = iconX - bounds.width();
        L.i("iconX = " + iconX + ", leftIconX = " + leftIconX);

        if(event.getRawX() >= leftIconX) {
            isShowPassword = !isShowPassword;
            setPasswordShowType(isShowPassword);
            setIconState(true);
            event.setAction(MotionEvent.ACTION_CANCEL);
            return false;
        }
        return super.onTouchEvent(event);
    }

    private PointF getTopRightPointF() {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, getWidth(), 0, 0, getHeight(), getWidth(), getHeight()};
        getMatrix().mapPoints(src, dst);
        return new PointF(getX() + src[2], getY() + src[3]);
    }

    private static class PasswordSaveState extends BaseSavedState {

        private final boolean isShowingPwd;

        private PasswordSaveState(Parcelable superState, boolean isShowingPwd) {
            super(superState);
            this.isShowingPwd = isShowingPwd;
        }

        private PasswordSaveState(Parcel source) {
            super(source);
            this.isShowingPwd = source.readByte() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (isShowingPwd ? 1 : 0));
        }

        public boolean isShowingPwd() {
            return isShowingPwd;
        }

        public static final Parcelable.Creator<PasswordSaveState> CREATOR = new Creator<PasswordSaveState>() {
            @Override
            public PasswordSaveState createFromParcel(Parcel source) {
                return new PasswordSaveState(source);
            }

            @Override
            public PasswordSaveState[] newArray(int size) {
                return new PasswordSaveState[size];
            }
        };
    }
}
