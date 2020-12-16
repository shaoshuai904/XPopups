package com.maple.popups.lib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import java.lang.ref.WeakReference;

/**
 * Base PopupWindow
 *
 * @author : shaoshuai
 * @date ：2020/11/26
 */
public abstract class MsBasePopup<T extends MsBasePopup> {
    public static final float DIM_AMOUNT_NOT_EXIST = -1f;
    public static final int NOT_SET = -1;
    protected final PopupWindow mPopup;
    protected Context mContext;
    protected WeakReference<View> mAttachedViewRf;
    private PopupWindow.OnDismissListener mDismissListener;
    private float mDimAmount = DIM_AMOUNT_NOT_EXIST;
    private boolean mDismissIfOutsideTouch = true;// 点击外部消失

    public MsBasePopup(Context context) {
        mContext = context;
        mPopup = new PopupWindow(mContext);
        mPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopup.setFocusable(true);
        mPopup.setTouchable(true);
        mPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MsBasePopup.this.onDismiss();
                if (mDismissListener != null) {
                    mDismissListener.onDismiss();
                }
            }
        });
        dismissIfOutsideTouch(mDismissIfOutsideTouch);
    }

    // 设置消失监听
    public T setDismissListener(PopupWindow.OnDismissListener listener) {
        mDismissListener = listener;
        return (T) this;
    }

    // 点击外部消失
    public T dismissIfOutsideTouch(boolean dismissIfOutsideTouch) {
        mDismissIfOutsideTouch = dismissIfOutsideTouch;
        mPopup.setOutsideTouchable(dismissIfOutsideTouch);
        if (dismissIfOutsideTouch) {
            mPopup.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        mPopup.dismiss();
                        return true;
                    }
                    return false;
                }
            });
        } else {
            mPopup.setTouchInterceptor(null);
        }
        return (T) this;
    }

    protected void showAtLocation(@NonNull View parent, int x, int y) {
        if (!ViewCompat.isAttachedToWindow(parent)) {
            return;
        }
        removeOldAttachStateChangeListener();
        parent.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
        mAttachedViewRf = new WeakReference<>(parent);
        mPopup.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);
        if (mDimAmount != DIM_AMOUNT_NOT_EXIST) {
            updateDimAmount(mDimAmount);
        }
    }

    private void removeOldAttachStateChangeListener() {
        if (mAttachedViewRf != null) {
            View oldAttachedView = mAttachedViewRf.get();
            if (oldAttachedView != null) {
                oldAttachedView.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }
        }
    }

    private View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {

        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            dismiss();
        }
    };

    // 设置阴影
    public T dimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return (T) this;
    }

    // 更新 暗淡量
    private void updateDimAmount(float dimAmount) {
        View decorView = getDecorView();
        if (decorView != null) {
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) decorView.getLayoutParams();
            p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = dimAmount;
            modifyWindowLayoutParams(p);

            WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            mWindowManager.updateViewLayout(decorView, p);
        }
    }

    public View getDecorView() {
        View decorView = null;
        try {
            if (mPopup.getBackground() == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView = (View) mPopup.getContentView().getParent();
                } else {
                    decorView = mPopup.getContentView();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView = (View) mPopup.getContentView().getParent().getParent();
                } else {
                    decorView = (View) mPopup.getContentView().getParent();
                }
            }
        } catch (Exception ignore) {

        }
        return decorView;
    }

    protected void modifyWindowLayoutParams(WindowManager.LayoutParams lp) {

    }

    protected void onDismiss() {

    }

    public final void dismiss() {
        removeOldAttachStateChangeListener();
        mAttachedViewRf = null;
        mPopup.dismiss();
    }
}
