package com.maple.popups.lib;

import android.graphics.Rect;
import android.view.View;

/**
 * 正常的 PopupWindow
 *
 * @author : shaoshuai
 * @date ：2020/11/26
 */
public class ShowInfo {
    int[] anchorRootLocation = new int[2];// 父View的位置
    int[] anchorLocation = new int[2];// 目标view的位置
    Rect visibleWindowFrame = new Rect();// 可显示窗体的范围
    int width; // 视图宽
    int height;
    int x;// 显示位置
    int y;
    View anchor;// 锚点View
    int anchorCenter;// 锚点中心X
    int contentWidthMeasureSpec;
    int contentHeightMeasureSpec;
    int decorationLeft = 0;// 阴影的装饰范围
    int decorationRight = 0;
    int decorationTop = 0;
    int decorationBottom = 0;

    ShowInfo(View anchor) {
        this.anchor = anchor;
        // for muti window
        anchor.getRootView().getLocationOnScreen(anchorRootLocation);
        anchor.getLocationOnScreen(anchorLocation);
        anchorCenter = anchorLocation[0] + anchor.getWidth() / 2;
        anchor.getWindowVisibleDisplayFrame(visibleWindowFrame);
    }

    float anchorProportion() {
        return (anchorCenter - x) / (float) width;
    }

    // 弹窗宽度
    int windowWidth() {
        return decorationLeft + width + decorationRight;
    }

    int windowHeight() {
        return decorationTop + height + decorationBottom;
    }

    // 最大显示范围的宽度
    int getVisibleWidth() {
        return visibleWindowFrame.width();
    }

    // 最大显示范围的高度
    int getVisibleHeight() {
        return visibleWindowFrame.height();
    }

    // 弹窗 左上角 对应X位置
    int getWindowX() {
        return x - anchorRootLocation[0];
    }

    int getWindowY() {
        return y - anchorRootLocation[1];
    }
}
