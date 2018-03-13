package com.example.maple.mspopupwindow.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.example.maple.mspopupwindow.R;


/**
 * 通用的popupWindow
 */
public abstract class BasePopupWindow {
    protected PopupWindow mPopupWindow;
    protected View contentView;
    protected View parentView;

    protected Context mContext;
    private WeiZhi mWz = WeiZhi.Bottom;
    public boolean isShow = false;

    public BasePopupWindow(Context context, View anchor, int w, int h) {
        initView(context, anchor, w, h);
    }

    public abstract View getContentView();

    private void initView(Context context, View anchor, int w, int h) {
        mContext = context;
        parentView = anchor;

        contentView = getContentView();
        // create PopupWindow
        mPopupWindow = new PopupWindow(contentView, w, h);// 默认占满全屏
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());// 设置popupWindow弹出窗体的背景
        mPopupWindow.setAnimationStyle(0);// 无需动画
        mPopupWindow.setFocusable(true);// 是否具有获取焦点的能力
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);// 外部触摸
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
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
                        mPopupWindow.dismiss();
                        isShow = false;
                    } else {
                        isShow = true;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void toShowPopup(View parent) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);// 得到v的位置
        // POW宽高
        View contentView = mPopupWindow.getContentView();
        if (contentView == null) {
            Log.e("BasePopWindow", "contentView is null!");
            return;
        }
        contentView.measure(0, 0);
        int powWidth = contentView.getMeasuredWidth();
        int powHeight = contentView.getMeasuredHeight();

        int x = 0;
        int y = 0;
        int anim = R.style.WeiZhi_bottom;
        switch (mWz) {
            case Top:// 上边
                anim = R.style.WeiZhi_top;
                x = location[0] + (parent.getWidth() - powWidth) / 2;// 水平居中
                y = location[1] - powHeight - dp2px(mContext, 2);// 上偏2dp
                break;
            case Bottom:// 下边
                anim = R.style.WeiZhi_bottom;
                x = location[0] + (parent.getWidth() - powWidth) / 2;// 水平居中
                y = location[1] + parent.getHeight() + dp2px(mContext, 2);// 下偏2dp
                break;
            case Left:// 左侧
                anim = R.style.WeiZhi_left;
                x = location[0] - powWidth - dp2px(mContext, 2);// 左偏2dp
                y = location[1] + (parent.getHeight() - powHeight) / 2;// 竖直居中
                break;
            case Right:// 右侧
                anim = R.style.WeiZhi_right;
                x = location[0] + parent.getWidth() + dp2px(mContext, 2);// 右偏2dp
                y = location[1] + (parent.getHeight() - powHeight) / 2;
                break;
            default:
                break;
        }
        mPopupWindow.setAnimationStyle(anim);
        // Gravity.NO_GRAVITY == Gravity.LEFT | Gravity.TOP
        mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);

    }

    // ---------------------------------------------------------------------------------------
    public enum WeiZhi {
        Top, Bottom, Left, Right
    }

    public void showPopupWindow() {
        showPopupWindow(WeiZhi.Bottom);
    }

    public void showPopupWindow(WeiZhi wz) {
        mWz = wz;
        toShowPopup(parentView);
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mPopupWindow.setOnDismissListener(listener);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
        isShow = false;
        // mPopupWindow = null;
    }

    // ---------------------------------------------------------------------------------------
    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
    }
}
