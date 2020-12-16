package com.maple.popups.lib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.maple.popups.IWindowInsetKeyboardConsumer;
import com.maple.popups.R;
import com.maple.popups.utils.DensityUtils;
import com.maple.popups.weight.QMUIViewOffsetHelper;
import com.maple.popups.weight.QMUIWindowInsetLayout2;

import java.util.ArrayList;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

/**
 * 全屏样式
 *
 * @author : shaoshuai
 * @date ：2020/11/26
 */
public class MsFullScreenPopup extends MsBasePopup<MsFullScreenPopup> {
    private static OnKeyBoardListener sOffsetKeyboardHeightListener;
    private static OnKeyBoardListener sOffsetHalfKeyboardHeightListener;

    public static OnKeyBoardListener getOffsetKeyboardHeightListener() {
        if (sOffsetKeyboardHeightListener == null) {
            sOffsetKeyboardHeightListener = new KeyboardPercentOffsetListener(1f);
        }
        return sOffsetKeyboardHeightListener;
    }

    public static OnKeyBoardListener getOffsetHalfKeyboardHeightListener() {
        if (sOffsetHalfKeyboardHeightListener == null) {
            sOffsetHalfKeyboardHeightListener = new KeyboardPercentOffsetListener(0.5f);
        }
        return sOffsetHalfKeyboardHeightListener;
    }


    private OnBlankClickListener mOnBlankClickListener;
    private boolean mAddCloseBtn = false;
    private Drawable mCloseIcon = ContextCompat.getDrawable(mContext, android.R.drawable.ic_menu_close_clear_cancel);
    private ConstraintLayout.LayoutParams mCloseIvLayoutParams;
    private int mAnimStyle = NOT_SET;
    private ArrayList<ViewInfo> mViews = new ArrayList<>();

    public MsFullScreenPopup(Context context) {
        super(context);
        mPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dimAmount(0.6f);
    }

    public MsFullScreenPopup onBlankClick(OnBlankClickListener onBlankClickListener) {
        mOnBlankClickListener = onBlankClickListener;
        return this;
    }

    public MsFullScreenPopup closeBtn(boolean close) {
        mAddCloseBtn = close;
        return this;
    }

    public MsFullScreenPopup closeIcon(Drawable drawable) {
        mCloseIcon = drawable;
        return this;
    }

    public MsFullScreenPopup closeLp(ConstraintLayout.LayoutParams contentLayoutParams) {
        mCloseIvLayoutParams = contentLayoutParams;
        return this;
    }

    public int getCloseBtnId() {
        return R.id.ms_popup_close_btn_id;
    }

    public MsFullScreenPopup animStyle(int animStyle) {
        mAnimStyle = animStyle;
        return this;
    }

    public MsFullScreenPopup addView(View view, ConstraintLayout.LayoutParams lp, OnKeyBoardListener onKeyBoardListener) {
        mViews.add(new ViewInfo(view, lp, onKeyBoardListener));
        return this;
    }

    public MsFullScreenPopup addView(View view, ConstraintLayout.LayoutParams lp) {
        return addView(view, lp, null);
    }

    public MsFullScreenPopup addView(View view, OnKeyBoardListener onKeyBoardListener) {
        mViews.add(new ViewInfo(view, defaultContentLp(), onKeyBoardListener));
        return this;
    }

    public MsFullScreenPopup addView(View view) {
        return addView(view, defaultContentLp());
    }

    private ConstraintLayout.LayoutParams defaultContentLp() {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        return lp;
    }

    private ConstraintLayout.LayoutParams defaultCloseIvLp() {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.bottomMargin = DensityUtils.dp2px(mContext, 48);
        return lp;
    }

    private AppCompatImageButton createCloseIv() {
        AppCompatImageButton closeBtn = new AppCompatImageButton(mContext);
        closeBtn.setPadding(0, 0, 0, 0);
        closeBtn.setScaleType(ImageView.ScaleType.CENTER);
        closeBtn.setId(R.id.ms_popup_close_btn_id);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        closeBtn.setFitsSystemWindows(true);
        closeBtn.setImageDrawable(mCloseIcon);
        return closeBtn;
    }

    public boolean isShowing() {
        return mPopup.isShowing();
    }

    public void show(View parent) {
        if (isShowing()) {
            return;
        }
        if (mViews.isEmpty()) {
            throw new RuntimeException("you should call addView() to add content view");
        }
        ArrayList<ViewInfo> views = new ArrayList<>(mViews);
        RootView rootView = new RootView(mContext);
        for (int i = 0; i < views.size(); i++) {
            ViewInfo info = mViews.get(i);
            View view = info.view;
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

            rootView.addView(view, info.lp);
        }
        if (mAddCloseBtn) {
            if (mCloseIvLayoutParams == null) {
                mCloseIvLayoutParams = defaultCloseIvLp();
            }
            rootView.addView(createCloseIv(), mCloseIvLayoutParams);
        }
        mPopup.setContentView(rootView);
        if (mAnimStyle != NOT_SET) {
            mPopup.setAnimationStyle(mAnimStyle);
        }

        showAtLocation(parent, 0, 0);
    }

    @Override
    protected void modifyWindowLayoutParams(WindowManager.LayoutParams lp) {
        lp.flags |= FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_INSET_DECOR;
        super.modifyWindowLayoutParams(lp);
    }

    public interface OnBlankClickListener {
        void onBlankClick(MsFullScreenPopup popup);
    }

    class RootView extends QMUIWindowInsetLayout2 implements IWindowInsetKeyboardConsumer {
        private int mLastKeyboardShowHeight = 0;
        private boolean mShouldInvokeBlackClickWhenTouchUp = false;

        public RootView(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
            if (mOnBlankClickListener == null) {
                return true;
            }
            if (action == MotionEvent.ACTION_DOWN) {
                mShouldInvokeBlackClickWhenTouchUp = isTouchInBlack(event);
            } else if (action == MotionEvent.ACTION_MOVE) {
                mShouldInvokeBlackClickWhenTouchUp = mShouldInvokeBlackClickWhenTouchUp && isTouchInBlack(event);
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                mShouldInvokeBlackClickWhenTouchUp = mShouldInvokeBlackClickWhenTouchUp && isTouchInBlack(event);
                if (mShouldInvokeBlackClickWhenTouchUp) {
                    mOnBlankClickListener.onBlankClick(MsFullScreenPopup.this);
                }
            }
            return true;
        }

        private boolean isTouchInBlack(MotionEvent event) {
            View childView = findChildViewUnder(event.getX(), event.getY());
            boolean isBlank = childView == null;
            if (!isBlank
//                    && (childView instanceof IBlankTouchDetector)
            ) {
                MotionEvent e = MotionEvent.obtain(event);
                int offsetX = getScrollX() - childView.getLeft();
                int offsetY = getScrollY() - childView.getTop();
                e.offsetLocation(offsetX, offsetY);
//                isBlank = ((IBlankTouchDetector) childView).isTouchInBlank(e);
                e.recycle();
            }
            return isBlank;
        }


        private View findChildViewUnder(float x, float y) {
            final int count = getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                final View child = getChildAt(i);
                final float translationX = child.getTranslationX();
                final float translationY = child.getTranslationY();
                if (x >= child.getLeft() + translationX
                        && x <= child.getRight() + translationX
                        && y >= child.getTop() + translationY
                        && y <= child.getBottom() + translationY) {
                    return child;
                }
            }
            return null;
        }

        @Override
        public boolean applySystemWindowInsets19(Rect insets) {
            super.applySystemWindowInsets19(insets);
            return true;
        }

        @Override
        public WindowInsetsCompat applySystemWindowInsets21(WindowInsetsCompat insets) {
            return super.applySystemWindowInsets21(insets).consumeStableInsets();
        }

        @Override
        public void onHandleKeyboard(int keyboardInset) {
            if (keyboardInset > 0) {
                mLastKeyboardShowHeight = keyboardInset;
                for (ViewInfo viewInfo : mViews) {
                    if (viewInfo.onKeyBoardListener != null) {
                        viewInfo.onKeyBoardListener.onKeyboardToggle(viewInfo.view, true, keyboardInset, getHeight());
                    }
                }
            } else {
                for (ViewInfo viewInfo : mViews) {
                    if (viewInfo.onKeyBoardListener != null) {
                        viewInfo.onKeyBoardListener.onKeyboardToggle(viewInfo.view, false, mLastKeyboardShowHeight, getHeight());
                    }
                }
            }
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            for (ViewInfo viewInfo : mViews) {
                View view = viewInfo.view;
                QMUIViewOffsetHelper offsetHelper = (QMUIViewOffsetHelper) view.getTag(R.id.ms_view_offset_helper);
                if (offsetHelper != null) {
                    offsetHelper.onViewLayout();
                }
            }
        }
    }

    class ViewInfo {
        private OnKeyBoardListener onKeyBoardListener;
        private View view;
        private ConstraintLayout.LayoutParams lp;

        public ViewInfo(View view, ConstraintLayout.LayoutParams lp, @Nullable OnKeyBoardListener onKeyBoardListener) {
            this.view = view;
            this.lp = lp;
            this.onKeyBoardListener = onKeyBoardListener;
        }
    }

    public static QMUIViewOffsetHelper getOrCreateViewOffsetHelper(View view) {
        QMUIViewOffsetHelper offsetHelper = (QMUIViewOffsetHelper) view.getTag(R.id.ms_view_offset_helper);
        if (offsetHelper == null) {
            offsetHelper = new QMUIViewOffsetHelper(view);
            view.setTag(R.id.ms_view_offset_helper, offsetHelper);
        }
        return offsetHelper;
    }

    public interface OnKeyBoardListener {
        void onKeyboardToggle(View view, boolean toShow, int keyboardHeight, int rootViewHeight);
    }

    public static class KeyboardPercentOffsetListener implements OnKeyBoardListener {
        private float mPercent;
        private ValueAnimator mAnimator;

        public KeyboardPercentOffsetListener(float percent) {
            mPercent = percent;
        }

        @Override
        public void onKeyboardToggle(View view, boolean toShow, int keyboardHeight, int rootViewHeight) {
            final QMUIViewOffsetHelper offsetHelper = MsFullScreenPopup.getOrCreateViewOffsetHelper(view);
            if (mAnimator != null) {
                clearValueAnimator(mAnimator);
            }
            int target = toShow ? (int) (-keyboardHeight * mPercent) : 0;
            mAnimator = ValueAnimator.ofInt(offsetHelper.getTopAndBottomOffset(), target);
            mAnimator.setInterpolator(new FastOutSlowInInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    offsetHelper.setTopAndBottomOffset((Integer) animation.getAnimatedValue());
                }
            });
            mAnimator.start();
        }

        public void clearValueAnimator(Animator animator) {
            if (animator != null) {
                animator.removeAllListeners();
                if (animator instanceof ValueAnimator) {
                    ((ValueAnimator) animator).removeAllUpdateListeners();
                }

                if (Build.VERSION.SDK_INT >= 19) {
                    animator.pause();
                }
                animator.cancel();
            }
        }

    }
}
