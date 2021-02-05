package com.maple.demo.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maple.demo.R;


/**
 * Created by maple on 2018/3/13.
 */

public class MorePopWindow extends BasePopupWindow {

    @Override
    public View getContentView() {
        return LayoutInflater.from(mContext).inflate(R.layout.pop_more, null);
    }

    public MorePopWindow(Context context) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public MorePopWindow(Context context, int w, int h) {
        super(context, w, h);
    }

    public MorePopWindow setOnItemClickListener(final View.OnClickListener listener) {
        contentView.findViewById(R.id.tvClearPagePen).setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(v);
            dismiss();
        });
        contentView.findViewById(R.id.tvClearPagePenAndContents).setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(v);
            dismiss();
        });
        contentView.findViewById(R.id.tvClearPageRecords).setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(v);
            dismiss();
        });
        contentView.findViewById(R.id.tvDelPage).setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(v);
            dismiss();
        });
        return this;
    }
}
