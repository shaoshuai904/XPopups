package com.maple.popups.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.maple.popups.R;
import com.maple.popups.utils.BaseQuickAdapter;
import com.maple.popups.utils.DensityUtils;
import com.maple.popups.utils.QuickActionAdapter;
import com.maple.popups.utils.SheetItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 快捷功能 PopupWindow
 *
 * @author : shaoshuai
 * @date ：2020/11/26
 */
public class MsQuickActionPopup extends MsNormalPopup<MsQuickActionPopup> {
    private ArrayList<SheetItem> mActions = new ArrayList<>();
    private int mActionWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mActionHeight;

    private boolean mShowMoreArrowIfNeeded = true;// 显示左滑右滑箭头
    private int mMoreArrowWidth = DensityUtils.dp2px(mContext, 36);// 宽度
    private int mPaddingHor = DensityUtils.dp2px(mContext, 1);// 水平左右间距
    private QuickActionAdapter mAdapter = new QuickActionAdapter(mContext);

    public MsQuickActionPopup(Context context, int width, int height) {
        super(context, width, height);
        mActionHeight = height;
    }

    public MsQuickActionPopup moreArrowWidth(int moreArrowWidth) {
        mMoreArrowWidth = moreArrowWidth;
        return this;
    }

    public MsQuickActionPopup paddingHor(int paddingHor) {
        mPaddingHor = paddingHor;
        return this;
    }

    public MsQuickActionPopup actionWidth(int actionWidth) {
        mActionWidth = actionWidth;
        return this;
    }

    public MsQuickActionPopup actionHeight(int actionHeight) {
        mActionHeight = actionHeight;
        return this;
    }

    public MsQuickActionPopup addAction(SheetItem action) {
        mActions.add(action);
        mAdapter.refreshData(mActions);
        return this;
    }

    public MsQuickActionPopup addActions(List<SheetItem> actions) {
        mActions.addAll(actions);
        mAdapter.refreshData(mActions);
        return this;
    }

    public MsQuickActionPopup showMoreArrowIfNeeded(boolean showMoreArrowIfNeeded) {
        mShowMoreArrowIfNeeded = showMoreArrowIfNeeded;
        return this;
    }

    private BaseQuickAdapter.OnItemClickListener<SheetItem> onItemClickListener = null;

    public MsQuickActionPopup setOnItemClickListener(BaseQuickAdapter.OnItemClickListener<SheetItem> listener) {
        onItemClickListener = listener;
        return this;
    }

    @Override
    protected int proxyWidth(int width) {
        if (width > 0 && mActionWidth > 0) {
            if (width >= mActionWidth * mActions.size() + 2 * mPaddingHor) {
                return super.proxyWidth(width);
            }
            width = width - mPaddingHor - mMoreArrowWidth;
            return mActionWidth * (width / mActionWidth) + mPaddingHor + mMoreArrowWidth;
        }
        return super.proxyWidth(width);
    }

    @Override
    public MsQuickActionPopup show(@NonNull View anchor) {
        setContextView(createContentView());
        return super.show(anchor);
    }

    private ConstraintLayout createContentView() {
        ConstraintLayout wrapper = new ConstraintLayout(mContext);
        final RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new MsLayoutManager(mContext));
        recyclerView.setId(View.generateViewId());
        recyclerView.setPadding(mPaddingHor, 0, mPaddingHor, 0);
        recyclerView.setClipToPadding(false);
        mAdapter.setOnItemClickListener((item, position) -> {
            dismiss();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item, position);
            }
        });
        mAdapter.refreshData(mActions);
        recyclerView.setAdapter(mAdapter);
        wrapper.addView(recyclerView);
        if (mShowMoreArrowIfNeeded) {
            AppCompatImageView leftMoreArrow = createMoreArrowView(true);
            AppCompatImageView rightMoreArrow = createMoreArrowView(false);

            leftMoreArrow.setOnClickListener(v -> recyclerView.smoothScrollToPosition(0));
            rightMoreArrow.setOnClickListener(v -> recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1));

            ConstraintLayout.LayoutParams leftLp = new ConstraintLayout.LayoutParams(mMoreArrowWidth, 0);
            leftLp.leftToLeft = recyclerView.getId();
            leftLp.topToTop = recyclerView.getId();
            leftLp.bottomToBottom = recyclerView.getId();
            wrapper.addView(leftMoreArrow, leftLp);

            ConstraintLayout.LayoutParams rightLp = new ConstraintLayout.LayoutParams(mMoreArrowWidth, 0);
            rightLp.rightToRight = recyclerView.getId();
            rightLp.topToTop = recyclerView.getId();
            rightLp.bottomToBottom = recyclerView.getId();
            wrapper.addView(rightMoreArrow, rightLp);

            recyclerView.addItemDecoration(new ItemDecoration(leftMoreArrow, rightMoreArrow));
        }
        return wrapper;
    }

    protected AppCompatImageView createMoreArrowView(boolean isLeft) {
        AppCompatImageView arrowView = new AppCompatImageView(mContext);
        if (isLeft) {
            arrowView.setPadding(mPaddingHor, 0, 0, 0);
            arrowView.setImageResource(R.drawable.ms_svg_arrow_left);
        } else {
            arrowView.setPadding(0, 0, mPaddingHor, 0);
            arrowView.setImageResource(R.drawable.ms_svg_arrow_right);
        }
        arrowView.setBackgroundColor(getContextBgColor());
        arrowView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        arrowView.setVisibility(View.GONE);
        arrowView.setAlpha(0f);
        return arrowView;
    }

    private class ItemDecoration extends RecyclerView.ItemDecoration {
        private AppCompatImageView leftMoreArrowView;
        private AppCompatImageView rightMoreArrowView;
        private boolean isLeftMoreShown = false;
        private boolean isRightMoreShown = false;
        private boolean isFirstDraw = true;
        private int TOGGLE_DURATION = 60;

        public ItemDecoration(AppCompatImageView leftMoreArrowView, AppCompatImageView rightMoreArrowView) {
            this.leftMoreArrowView = leftMoreArrowView;
            this.rightMoreArrowView = rightMoreArrowView;
        }

        private Runnable leftHideEndAction = new Runnable() {
            @Override
            public void run() {
                leftMoreArrowView.setVisibility(View.GONE);
            }
        };

        private Runnable rightHideEndAction = new Runnable() {
            @Override
            public void run() {
                rightMoreArrowView.setVisibility(View.GONE);
            }
        };


        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (parent.canScrollHorizontally(-1)) {
                if (!isLeftMoreShown) {
                    isLeftMoreShown = true;
                    leftMoreArrowView.setVisibility(View.VISIBLE);
                    if (isFirstDraw) {
                        leftMoreArrowView.setAlpha(1F);
                    } else {
                        leftMoreArrowView.animate()
                                .alpha(1f)
                                .setDuration(TOGGLE_DURATION)
                                .start();
                    }
                }
            } else {
                if (isLeftMoreShown) {
                    isLeftMoreShown = false;
                    leftMoreArrowView.animate()
                            .alpha(0f)
                            .setDuration(TOGGLE_DURATION)
                            .withEndAction(leftHideEndAction)
                            .start();
                }
            }
            if (parent.canScrollHorizontally(1)) {
                if (!isRightMoreShown) {
                    isRightMoreShown = true;
                    rightMoreArrowView.setVisibility(View.VISIBLE);
                    if (isFirstDraw) {
                        rightMoreArrowView.setAlpha(1F);
                    } else {
                        rightMoreArrowView.animate()
                                .setDuration(TOGGLE_DURATION)
                                .alpha(1f)
                                .start();
                    }
                }
            } else {
                if (isRightMoreShown) {
                    isRightMoreShown = false;
                    rightMoreArrowView.animate()
                            .alpha(0f)
                            .setDuration(TOGGLE_DURATION)
                            .withEndAction(rightHideEndAction)
                            .start();
                }
            }
            isFirstDraw = false;
        }
    }

    private class MsLayoutManager extends LinearLayoutManager {
        public MsLayoutManager(Context context) {
            super(context, LinearLayoutManager.HORIZONTAL, false);
        }

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return new RecyclerView.LayoutParams(mActionWidth, mActionHeight);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected int calculateTimeForScrolling(int dx) {
                    return 100;
                }
            };

            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

}