package com.example.maple.mspopupwindow.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.maple.mspopupwindow.R;


public abstract class BasePopupWindow {
    protected PopupWindow mPopupWindow;
    protected View contentView;
    protected View parentView;

    protected Context mContext;
    private AZIMUTH mWz = AZIMUTH.Bottom;
    public boolean isShow = false;
    private int margin = 0;


    public BasePopupWindow(Context context, View anchor, int w, int h) {
        initView(context, anchor, w, h);
    }

    public abstract View getContentView();

    private void initView(Context context, View anchor, int w, int h) {
        mContext = context;
        parentView = anchor;

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
        setOnDismissListener(null);
    }

    private void toShowPopup(View parent) {
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
        mPopupWindow.setAnimationStyle(anim);
        // Gravity.NO_GRAVITY == Gravity.LEFT | Gravity.TOP
        mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);
        setAlpha(defAlpha);
    }

    // ---------------------------------------------------------------------------------------
    // wei zhi
    public enum AZIMUTH {
        Top, Bottom, Left, Right
    }

    public void showPopupWindow() {
        showPopupWindow(AZIMUTH.Top);
    }

    public void showPopupWindow(AZIMUTH wz) {
        mWz = wz;
        toShowPopup(parentView);
    }

    public BasePopupWindow setMarginSize(float marginSize) {
        margin = dp2px(mContext, marginSize);
        return this;
    }

    public BasePopupWindow setOnDismissListener(final PopupWindow.OnDismissListener listener) {
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (listener != null)
                    listener.onDismiss();
                setAlpha(1f);
            }
        });
        return this;
    }

    public void dismiss() {
        mPopupWindow.dismiss();
        isShow = false;
        setAlpha(1f);
    }

    public boolean isShow() {
        return mPopupWindow.isShowing();
    }

    // ---------------------------------------------------------------------------------------
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
    }

    Activity activity;
    float defAlpha = 0.7f;

    public BasePopupWindow setAlphaStyle(Activity activity, float defAlpha) {
        this.activity = activity;
        this.defAlpha = defAlpha;
        return this;
    }

    private void setAlpha(float alpha) {
        if (activity != null) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            params.alpha = alpha;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            activity.getWindow().setAttributes(params);
        }
    }
}

