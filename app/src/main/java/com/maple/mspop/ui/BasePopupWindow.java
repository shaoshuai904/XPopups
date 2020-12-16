package com.maple.mspop.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.maple.mspop.R;


public abstract class BasePopupWindow {
    protected PopupWindow mPopupWindow;
    protected View contentView;
    protected Context mContext;
    public boolean isShow = false;

    public BasePopupWindow(Context context) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public BasePopupWindow(Context context, int w, int h) {
        initView(context, w, h);
    }

    public abstract View getContentView();

    private void initView(Context context, int w, int h) {
        mContext = context;
        contentView = getContentView();
        // create PopupWindow
        mPopupWindow = new PopupWindow(contentView, w, h);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        contentView.setFocusableInTouchMode(true);
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU) {
                    if (isShow) {
                        dismiss();
                    } else {
                        isShow = true;
                    }
                    return true;
                }
                return false;
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDismissStatusChangeListener != null) {
                    onDismissStatusChangeListener.onDismissStatusChange(false);
                }
                setAlpha(1f);
            }
        });
    }

    // wei zhi
    public enum AZIMUTH {
        Top, Bottom, Left, Right
    }

    public void showTop(View anchor, int margin) {
        toShowPopup(anchor, AZIMUTH.Top, dp2px(mContext, margin));
    }

    public void showBottom(View anchor, int margin) {
        toShowPopup(anchor, AZIMUTH.Bottom, dp2px(mContext, margin));
    }

    public void showLeft(View anchor, int margin) {
        toShowPopup(anchor, AZIMUTH.Left, dp2px(mContext, margin));
    }

    public void showRight(View anchor, int margin) {
        toShowPopup(anchor, AZIMUTH.Right, dp2px(mContext, margin));
    }

    public void showPopupWindow(View anchor, AZIMUTH wz, float marginSize) {
        toShowPopup(anchor, wz, dp2px(mContext, marginSize));
    }

    private void toShowPopup(View parent, AZIMUTH mWz, int margin) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        View contentView = mPopupWindow.getContentView();
        if (contentView == null) {
            Log.e("BasePopWindow", "contentView is null!");
            return;
        }
        // POW width height
        contentView.measure(0, 0);
        int powWidth = contentView.getMeasuredWidth();
        int powHeight = contentView.getMeasuredHeight();

        int x = 0;
        int y = 0;
        int anim = 0; // no animation
        switch (mWz) {
            case Top:
                anim = R.style.WeiZhi_top;
                x = location[0] + (parent.getWidth() - powWidth) / 2;
                y = location[1] - powHeight - margin;// margin top 2dp
                break;
            case Bottom:
                anim = R.style.WeiZhi_bottom;
                x = location[0] + (parent.getWidth() - powWidth) / 2;
                y = location[1] + parent.getHeight() + margin;// margin button 2dp
                break;
            case Left:
                anim = R.style.WeiZhi_left;
                x = location[0] - powWidth - margin;// margin left 2dp
                y = location[1] + (parent.getHeight() - powHeight) / 2;// 竖直居中
                break;
            case Right:
                anim = R.style.WeiZhi_right;
                x = location[0] + parent.getWidth() + margin;// margin right 2dp
                y = location[1] + (parent.getHeight() - powHeight) / 2;
                break;
            default:
                break;
        }
        if (onDismissStatusChangeListener != null) {
            onDismissStatusChangeListener.onDismissStatusChange(true);
        }
        mPopupWindow.setAnimationStyle(anim);
        // Gravity.NO_GRAVITY == Gravity.LEFT | Gravity.TOP
        mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);
        setAlpha(defAlpha);
    }

    // --------------------------------------------------------------------

    OnDismissStatusChangeListener onDismissStatusChangeListener;

    public void setOnDismissStatusChangeListener(OnDismissStatusChangeListener listener) {
        this.onDismissStatusChangeListener = listener;
    }

    interface OnDismissStatusChangeListener {
        void onDismissStatusChange(boolean isShow);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
        isShow = false;
        setAlpha(1f);
    }

    // -----------------------------透明度变化------------------------------------
    private Window mWindow;// 全局的透明度变化
    private View dimView;// 特定View的变化
    float defAlpha = 0.7f;

    public BasePopupWindow setAlphaStyle(Activity activity, float defAlpha) {
        return setAlphaStyle(activity.getWindow(), defAlpha);
    }

    public BasePopupWindow setAlphaStyle(Window window, float defAlpha) {
        this.mWindow = window;
        this.defAlpha = defAlpha;
        return this;
    }

    public BasePopupWindow setAlphaStyle(View view, float defAlpha) {
        this.dimView = view;
        this.defAlpha = defAlpha;
        return this;
    }

    private void setAlpha(float newAlpha) {
        if (mWindow != null) {
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = newAlpha;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }
        if (dimView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int alpha = (int) ((1.0f - newAlpha) * 255.0f + 0.5f);
                dimView.setForeground(new ColorDrawable(Color.argb(alpha, 0, 0, 0)));
            }
        }
    }

    // ---------------------------工具方法-----------------------------------

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
    }
}

