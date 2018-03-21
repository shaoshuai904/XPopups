package com.example.maple.mspopupwindow.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maple.mspopupwindow.R;

/**
 * Created by maple on 2018/3/13.
 */

public class MorePopWindow extends BasePopupWindow {

    @Override
    public View getContentView() {
        return LayoutInflater.from(mContext).inflate(R.layout.pop_more, null);
    }

    public MorePopWindow(Context context, View anchor) {
        this(context, anchor, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public MorePopWindow(Context context, View anchor, int w, int h) {
        super(context, anchor, w, h);
    }

    public MorePopWindow setOnItemClickListener(final View.OnClickListener listener) {
        contentView.findViewById(R.id.tvClearPagePen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        contentView.findViewById(R.id.tvClearPagePenAndContents).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        contentView.findViewById(R.id.tvClearPageRecords).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        contentView.findViewById(R.id.tvDelPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }
}