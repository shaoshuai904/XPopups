package com.maple.popups.lib;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;


public class MsPopup extends MsNormalPopup<MsPopup> {

    public MsPopup(Context context, int width, int height) {
        super(context, width, height);
    }

    @Override
    public MsPopup show(@NonNull View anchor) {
        return super.show(anchor);
    }
}