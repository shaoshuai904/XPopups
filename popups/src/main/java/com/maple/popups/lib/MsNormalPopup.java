package com.maple.popups.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AnimRes;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.maple.popups.R;
import com.maple.popups.utils.DensityUtils;
import com.maple.popups.weight.MsFrameLayout;

/**
 * 正常的 PopupWindow
 *
 * @author : shaoshuai
 * @date ：2020/11/26
 */
public class MsNormalPopup<T extends MsBasePopup> extends MsBasePopup<T> {
    private View mContentView;// 内容视图
    protected final int mInitWidth;
    protected final int mInitHeight;
    private int mContextBgColor = Color.WHITE;//Color.TRANSPARENT; // 内容填充背景色
    private int mRadius = DensityUtils.dp2px(mContext, 12);// 四周圆角;
    private Direction mPreferredDirection = Direction.BOTTOM;// 首选显示方向
    private int mOffsetAnchor = 0;// 与锚点的距离
    // 显示范围四周的安全边距
    private int mEdgeProtectionTop = 0;
    private int mEdgeProtectionLeft = 0;
    private int mEdgeProtectionRight = 0;
    private int mEdgeProtectionBottom = 0;
    // 阴影
    private boolean mAddShadow = false;// 是否显示阴影
    private int mShadowElevation = DensityUtils.dp2px(mContext, 4);// 阴影高度
    private float mShadowAlpha = 0.55f;// 阴影透明度
    // 箭头
    private boolean mShowArrow = true;// 是否显示箭头
    private int mArrowWidth = DensityUtils.dp2px(mContext, 18);// 箭头宽
    private int mArrowHeight = DensityUtils.dp2px(mContext, 10);// 箭头高
    // 边框
    private @ColorInt int mBorderColor = 0;//Color.TRANSPARENT; // 边框颜色
    private int mBorderWidth = 0; // 边框宽度
    // 动画
    protected AnimStyle mAnimStyle = AnimStyle.ANIM_AUTO;
    protected @AnimRes int mSpecAnimStyle;

    // 显示方向
    public enum Direction {
        TOP, BOTTOM, LEFT, RIGHT, CENTER_IN_SCREEN
    }

    // 显示动画
    public enum AnimStyle {
        ANIM_AUTO,
        ANIM_GROW_FROM_LEFT,
        ANIM_GROW_FROM_RIGHT,
        ANIM_GROW_FROM_CENTER,
        ANIM_CUSTOM // 自定义
    }

    public MsNormalPopup(Context context, int width, int height) {
        super(context);
        mInitWidth = width;
        mInitHeight = height;
    }

    // 设置内容视图
    public T setContextView(View contentView) {
        mContentView = contentView;
        return (T) this;
    }

    public T setContextView(@LayoutRes int contentViewResId) {
        return setContextView(LayoutInflater.from(mContext).inflate(contentViewResId, null));
    }

    // 内容背景色
    public T setContextBgColor(int bgColor) {
        mContextBgColor = bgColor;
        return (T) this;
    }

    public int getContextBgColor() {
        return mContextBgColor;
    }

    public T radius(int radius) {
        mRadius = radius;
        return (T) this;
    }

    // 设置边框宽度
    public T borderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
        return (T) this;
    }

    // 设置边框颜色
    public T borderColor(int borderColor) {
        mBorderColor = borderColor;
        return (T) this;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    // 是否显示箭头
    public T arrow(boolean showArrow) {
        mShowArrow = showArrow;
        return (T) this;
    }

    public T arrowSize(int width, int height) {
        mArrowWidth = width;
        mArrowHeight = height;
        return (T) this;
    }

    // 是否显示阴影
    public T shadow(boolean addShadow) {
        mAddShadow = addShadow;
        return (T) this;
    }

    // 设置阴影高度和透明度
    public T shadowElevation(int shadowElevation, float shadowAlpha) {
        mShadowElevation = shadowElevation;
        mShadowAlpha = shadowAlpha;
        return (T) this;
    }

    // 判断是否显示阴影
    private boolean shouldShowShadow() {
        boolean useFeature = Build.VERSION.SDK_INT >= 21;
        return mAddShadow && useFeature;
    }

    // 边缘保护
    public T edgeProtection(int distance) {
        mEdgeProtectionLeft = distance;
        mEdgeProtectionRight = distance;
        mEdgeProtectionTop = distance;
        mEdgeProtectionBottom = distance;
        return (T) this;
    }

    public T edgeProtection(int left, int top, int right, int bottom) {
        mEdgeProtectionLeft = left;
        mEdgeProtectionTop = top;
        mEdgeProtectionRight = right;
        mEdgeProtectionBottom = bottom;
        return (T) this;
    }

    // 距离目标view的间距
    public T offsetYIfTop(int y) {
        mOffsetAnchor = y;
        return (T) this;
    }

    // 默认显示方向
    public T preferredDirection(Direction preferredDirection) {
        mPreferredDirection = preferredDirection;
        return (T) this;
    }

    public T animStyle(AnimStyle animStyle) {
        mAnimStyle = animStyle;
        return (T) this;
    }

    public T customAnimStyle(@AnimRes int animStyle) {
        mAnimStyle = AnimStyle.ANIM_CUSTOM;
        mSpecAnimStyle = animStyle;
        return (T) this;
    }

    public T show(@NonNull View anchor) {
        if (mContentView == null) {
            throw new RuntimeException("you should call setContextView() to set your content view");
        }
        ShowInfo showInfo = new ShowInfo(anchor);
        calculateWindowSize(showInfo);
        calculateXY(showInfo);
        adjustShowInfo(showInfo);
        decorateContentView(showInfo);
        setAnimationStyle(showInfo.anchorProportion(), mPreferredDirection);
        mPopup.setWidth(showInfo.windowWidth());
        mPopup.setHeight(showInfo.windowHeight());
        showAtLocation(anchor, showInfo.getWindowX(), showInfo.getWindowY());
        return (T) this;
    }

    protected int proxyWidth(int width) {
        return width;
    }

    protected int proxyHeight(int height) {
        return height;
    }

    // 计算窗口大小
    private void calculateWindowSize(ShowInfo showInfo) {
        boolean needMeasureForWidth = false, needMeasureForHeight = false;
        if (mInitWidth > 0) {
            showInfo.width = proxyWidth(mInitWidth);
            showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(showInfo.width, View.MeasureSpec.EXACTLY);
        } else {
            int maxWidth = showInfo.getVisibleWidth() - mEdgeProtectionLeft - mEdgeProtectionRight;
            if (mInitWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                showInfo.width = proxyWidth(maxWidth);
                showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(showInfo.width, View.MeasureSpec.EXACTLY);
            } else {
                needMeasureForWidth = true;
                showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(proxyWidth(maxWidth), View.MeasureSpec.AT_MOST);
            }
        }
        if (mInitHeight > 0) {
            showInfo.height = proxyHeight(mInitHeight);
            showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(showInfo.height, View.MeasureSpec.EXACTLY);
        } else {
            int maxHeight = showInfo.getVisibleHeight() - mEdgeProtectionTop - mEdgeProtectionBottom;
            if (mInitHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                showInfo.height = proxyHeight(maxHeight);
                showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(showInfo.height, View.MeasureSpec.EXACTLY);
            } else {
                needMeasureForHeight = true;
                showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(proxyHeight(maxHeight), View.MeasureSpec.AT_MOST);
            }
        }

        if (needMeasureForWidth || needMeasureForHeight) {
            mContentView.measure(showInfo.contentWidthMeasureSpec, showInfo.contentHeightMeasureSpec);
            if (needMeasureForWidth) {
                showInfo.width = proxyWidth(mContentView.getMeasuredWidth());
            }
            if (needMeasureForHeight) {
                showInfo.height = proxyHeight(mContentView.getMeasuredHeight());
                Log.e("okhttp", "needMeasureForHeight height:" + showInfo.height);
            }
        }
    }

    // 计算 X Y
    private void calculateXY(ShowInfo showInfo) {
        int mOffsetX = 0;// x偏移量
        if (showInfo.anchorCenterX < showInfo.visibleWindowFrame.left + showInfo.getVisibleWidth() / 2) {
            // 目标view的中心在父View左侧
            showInfo.x = Math.max(
                    showInfo.visibleWindowFrame.left + mEdgeProtectionLeft,
                    showInfo.anchorCenterX - showInfo.width / 2 + mOffsetX);
        } else {
            // 目标view的中心在父View右侧
            showInfo.x = Math.min(
                    showInfo.visibleWindowFrame.right - mEdgeProtectionRight - showInfo.width,
                    showInfo.anchorCenterX - showInfo.width / 2 + mOffsetX);
        }
        int mOffsetY = 0;// y偏移量
        if (showInfo.anchorCenterY < showInfo.visibleWindowFrame.top + showInfo.getVisibleHeight() / 2) {
            // 目标view的中心在父View上侧
            showInfo.y = Math.max(
                    showInfo.visibleWindowFrame.top + mEdgeProtectionTop,
                    showInfo.anchorCenterY - showInfo.height / 2 + mOffsetY);
        } else {
            // 目标view的中心在父View下侧
            showInfo.y = Math.min(
                    showInfo.visibleWindowFrame.bottom - mEdgeProtectionBottom - showInfo.height,
                    showInfo.anchorCenterY - showInfo.height / 2 + mOffsetY);
        }
        Direction nextDirection = Direction.CENTER_IN_SCREEN;
        if (mPreferredDirection == Direction.TOP) {
            nextDirection = Direction.BOTTOM;
        } else if (mPreferredDirection == Direction.BOTTOM) {
            nextDirection = Direction.TOP;
        } else if (mPreferredDirection == Direction.LEFT) {
            nextDirection = Direction.RIGHT;
        } else if (mPreferredDirection == Direction.RIGHT) {
            nextDirection = Direction.LEFT;
        }
        handleDirection(showInfo, mPreferredDirection, nextDirection);
    }

    // 确定显示方向 和 显示位置
    private void handleDirection(ShowInfo showInfo, Direction currentDirection, Direction nextDirection) {
        int mOffsetY = mOffsetAnchor;// y偏移量
        if (currentDirection == Direction.CENTER_IN_SCREEN) {
            showInfo.x = showInfo.visibleWindowFrame.left + (showInfo.getVisibleWidth() - showInfo.width) / 2;
            showInfo.y = showInfo.visibleWindowFrame.top + (showInfo.getVisibleHeight() - showInfo.height) / 2;
            mPreferredDirection = Direction.CENTER_IN_SCREEN;
        } else if (currentDirection == Direction.TOP) {
            showInfo.y = showInfo.anchorLocation[1] - showInfo.height - mOffsetY;
            if (showInfo.y < mEdgeProtectionTop + showInfo.visibleWindowFrame.top) {
                // 往上显示不开，就往下显示，往下也显示不开，就居中显示
                handleDirection(showInfo, nextDirection, Direction.CENTER_IN_SCREEN);
            } else {
                mPreferredDirection = Direction.TOP;
            }
        } else if (currentDirection == Direction.BOTTOM) {
            showInfo.y = showInfo.anchorLocation[1] + showInfo.anchor.getHeight() + mOffsetY;
            if (showInfo.y > showInfo.visibleWindowFrame.bottom - mEdgeProtectionBottom - showInfo.height) {
                handleDirection(showInfo, nextDirection, Direction.CENTER_IN_SCREEN);
            } else {
                mPreferredDirection = Direction.BOTTOM;
            }
        } else if (currentDirection == Direction.LEFT) {
            showInfo.x = showInfo.anchorLocation[0] - showInfo.width - mOffsetY;
            if (showInfo.x < mEdgeProtectionLeft + showInfo.visibleWindowFrame.left) {
                handleDirection(showInfo, nextDirection, Direction.CENTER_IN_SCREEN);
            } else {
                mPreferredDirection = Direction.LEFT;
            }
        } else if (currentDirection == Direction.RIGHT) {
            showInfo.x = showInfo.anchorLocation[0] + showInfo.anchor.getWidth() + mOffsetY;
            if (showInfo.x > showInfo.visibleWindowFrame.right - mEdgeProtectionRight - showInfo.width) {
                handleDirection(showInfo, nextDirection, Direction.CENTER_IN_SCREEN);
            } else {
                mPreferredDirection = Direction.RIGHT;
            }
        }
    }

    // 调整显示信息
    private void adjustShowInfo(ShowInfo showInfo) {
        int mShadowInset = (int) (mShadowElevation * 3f);
        if (shouldShowShadow()) {
            int originX = showInfo.x, originY = showInfo.y;
            if (originX - mShadowInset < showInfo.visibleWindowFrame.left) {
                showInfo.decorationLeft = originX - showInfo.visibleWindowFrame.left;
                showInfo.x = showInfo.visibleWindowFrame.left;
            } else {
                showInfo.decorationLeft = mShadowInset;
                showInfo.x -= mShadowInset;
            }
            if (originX + showInfo.width + mShadowInset < showInfo.visibleWindowFrame.right) {
                showInfo.decorationRight = mShadowInset;
            } else {
                showInfo.decorationRight = showInfo.visibleWindowFrame.right - originX - showInfo.width;
            }
            if (originY - mShadowInset > showInfo.visibleWindowFrame.top) {
                showInfo.y -= mShadowInset;
                showInfo.decorationTop = mShadowInset;
            } else {
                showInfo.decorationTop = originY - showInfo.visibleWindowFrame.top;
                showInfo.y = showInfo.visibleWindowFrame.top;
            }
            if (originY + showInfo.height + mShadowInset < showInfo.visibleWindowFrame.bottom) {
                showInfo.decorationBottom = mShadowInset;
            } else {
                showInfo.decorationBottom = showInfo.visibleWindowFrame.bottom - originY - showInfo.height;
            }
        }

        if (mShowArrow && mPreferredDirection != Direction.CENTER_IN_SCREEN) {
            if (mPreferredDirection == Direction.TOP) {
                showInfo.decorationBottom = Math.max(showInfo.decorationBottom, mArrowHeight);
                showInfo.y -= mArrowHeight;
            } else if (mPreferredDirection == Direction.BOTTOM) {
                showInfo.decorationTop = Math.max(showInfo.decorationTop, mArrowHeight);
                if (shouldShowShadow()) {
                    showInfo.y += Math.min(mShadowInset, mArrowHeight);
                }
            } else if (mPreferredDirection == Direction.LEFT) {
                showInfo.decorationRight = Math.max(showInfo.decorationRight, mArrowWidth);
                showInfo.x -= mArrowWidth;
            } else if (mPreferredDirection == Direction.RIGHT) {
                showInfo.decorationLeft = Math.max(showInfo.decorationLeft, mArrowWidth);
                if (shouldShowShadow()) {
                    showInfo.x += Math.min(mShadowInset, mArrowWidth);
                }
            }
        }
    }

    // 装饰内容
    private void decorateContentView(ShowInfo showInfo) {
        ContentView contentView = ContentView.wrap(mContentView, mInitWidth, mInitHeight);
        contentView.setBackgroundColor(mContextBgColor);
        contentView.setBorderColor(mBorderColor);
        contentView.setBorderWidth(mBorderWidth);

        if (shouldShowShadow()) {
            contentView.setRadiusAndShadow(mRadius, mShadowElevation, mShadowAlpha);
        } else {
            contentView.setRadius(mRadius);
        }

        DecorRootView decorRootView = new DecorRootView(mContext, showInfo);
        // decorRootView.setBackground(new ColorDrawable(Color.parseColor("#dddddddd")));
        decorRootView.setContentView(contentView);
        mPopup.setContentView(decorRootView);
    }

    // 设置动画样式
    private void setAnimationStyle(float anchorProportion, Direction direction) {
        boolean onTop = direction == Direction.TOP;
        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mPopup.setAnimationStyle(onTop ? R.style.MS_Animation_PopUpMenu_Left : R.style.MS_Animation_PopDownMenu_Left);
                break;
            case ANIM_GROW_FROM_RIGHT:
                mPopup.setAnimationStyle(onTop ? R.style.MS_Animation_PopUpMenu_Right : R.style.MS_Animation_PopDownMenu_Right);
                break;
            case ANIM_GROW_FROM_CENTER:
                mPopup.setAnimationStyle(onTop ? R.style.MS_Animation_PopUpMenu_Center : R.style.MS_Animation_PopDownMenu_Center);
                break;
            case ANIM_AUTO:
                if (anchorProportion <= 0.25f) {
                    mPopup.setAnimationStyle(onTop ? R.style.MS_Animation_PopUpMenu_Left : R.style.MS_Animation_PopDownMenu_Left);
                } else if (anchorProportion > 0.25f && anchorProportion < 0.75f) {
                    mPopup.setAnimationStyle(onTop ? R.style.MS_Animation_PopUpMenu_Center : R.style.MS_Animation_PopDownMenu_Center);
                } else {
                    mPopup.setAnimationStyle(onTop ? R.style.MS_Animation_PopUpMenu_Right : R.style.MS_Animation_PopDownMenu_Right);
                }
                break;
            case ANIM_CUSTOM:
                mPopup.setAnimationStyle(mSpecAnimStyle);
                break;
        }
    }

    static class ContentView extends MsFrameLayout {
        private ContentView(Context context) {
            super(context);
        }

        static ContentView wrap(View businessView, int width, int height) {
            ContentView contentView = new ContentView(businessView.getContext());
            if (businessView.getParent() != null) {
                ((ViewGroup) businessView.getParent()).removeView(businessView);
            }
            contentView.addView(businessView, new FrameLayout.LayoutParams(width, height));
            return contentView;
        }
    }

    class DecorRootView extends FrameLayout {
        private ShowInfo mShowInfo;
        private View mContentView;
        private Runnable mUpdateWindowAction = new Runnable() {
            @Override
            public void run() {
                calculateXY(mShowInfo);
                adjustShowInfo(mShowInfo);
                mPopup.update(mShowInfo.getWindowX(), mShowInfo.getWindowY(), mShowInfo.windowWidth(), mShowInfo.windowHeight());
            }
        };

        private DecorRootView(Context context, ShowInfo showInfo) {
            super(context);
            mShowInfo = showInfo;
        }

        public void setContentView(View contentView) {
            if (mContentView != null) {
                removeView(mContentView);
            }
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
            mContentView = contentView;
            addView(contentView);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            removeCallbacks(mUpdateWindowAction);
            if (mContentView != null) {
                mContentView.measure(mShowInfo.contentWidthMeasureSpec, mShowInfo.contentHeightMeasureSpec);
                int measuredWidth = mContentView.getMeasuredWidth();
                int measuredHeight = mContentView.getMeasuredHeight();
                if (mShowInfo.width != measuredWidth || mShowInfo.height != measuredHeight) {
                    mShowInfo.width = measuredWidth;
                    mShowInfo.height = measuredHeight;
                    post(mUpdateWindowAction);
                }
            }
            setMeasuredDimension(mShowInfo.windowWidth(), mShowInfo.windowHeight());
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            if (mContentView != null) {
                mContentView.layout(mShowInfo.decorationLeft, mShowInfo.decorationTop,
                        mShowInfo.width + mShowInfo.decorationLeft,
                        mShowInfo.height + mShowInfo.decorationTop);
            }
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            removeCallbacks(mUpdateWindowAction);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (mShowArrow) {
                MyViewUtils.drawArrow(this, canvas, mPreferredDirection, mShowInfo,
                        mContextBgColor, mBorderWidth, mBorderColor, mArrowWidth, mArrowHeight);
            }
        }
    }
}
