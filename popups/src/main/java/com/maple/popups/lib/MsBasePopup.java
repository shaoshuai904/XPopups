package com.maple.popups.lib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
    protected final PopupWindow mPopup;
    protected Context mContext;
    protected WeakReference<View> mAttachedViewRf;
    private PopupWindow.OnDismissListener mDismissListener;
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
        setAlpha(defAlpha);
        updateDimAmount(mDimAmount);
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

    protected void onDismiss() {
        setAlpha(1f);
    }

    public final void dismiss() {
        removeOldAttachStateChangeListener();
        mAttachedViewRf = null;
        mPopup.dismiss();
        setAlpha(1f);
    }

    // -----------------------------透明度------------------------------------
    private Window mWindow;// 全局的透明度变化
    private View dimView;// 特定View的变化
    float defAlpha = 0.7f;

    public T setAlphaStyle(Activity activity, float defAlpha) {
        return setAlphaStyle(activity.getWindow(), defAlpha);
    }

    public T setAlphaStyle(Window window, float defAlpha) {
        this.mWindow = window;
        this.defAlpha = defAlpha;
        return (T) this;
    }

    // 可以指定 特定View的透明度 变化
    public T setAlphaStyle(View view, float defAlpha) {
        this.dimView = view;
        this.defAlpha = defAlpha;
        return (T) this;
    }

    private void setAlpha(float newAlpha) {
        if (mWindow != null) {
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = newAlpha;
            // mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }
        if (dimView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int alpha = (int) ((1.0f - newAlpha) * 255.0f + 0.5f);
                dimView.setForeground(new ColorDrawable(Color.argb(alpha, 0, 0, 0)));
            }
        }
    }

    // -----------------------------黑暗度------------------------------------
    private float mDimAmount = -1f;

    public T dimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return (T) this;
    }

    // 更新 暗淡量
    private void updateDimAmount(float dimAmount) {
        View decorView = getDecorView();
        if (mDimAmount != -1f && decorView != null) {
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) decorView.getLayoutParams();
            params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = dimAmount;
            modifyWindowLayoutParams(params);

            WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            mWindowManager.updateViewLayout(decorView, params);
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

}
