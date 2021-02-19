package com.maple.popups.lib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * 正常的 PopupWindow，承接 MsNormalPopup 的实现
 *
 * @author : shaoshuai
 * @date ：2020/11/26
 */
public class MsPopup extends MsNormalPopup<MsPopup> {

    public MsPopup(Context context) {
        super(context);
    }

    public MsPopup(Context context, int width) {
        super(context, width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public MsPopup(Context context, int width, int height) {
        super(context, width, height);
    }

    @Override
    public MsPopup show(@NonNull View anchor) {
        return super.show(anchor);
    }
}