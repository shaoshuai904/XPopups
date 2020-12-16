package com.maple.popups.lib;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.maple.popups.weight.QMUIWrapContentListView;


public class MsPopups {

    public static MsPopup popup(Context context) {
        return new MsPopup(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static MsPopup popup(Context context, int width) {
        return new MsPopup(context, width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static MsPopup popup(Context context, int width, int height) {
        return new MsPopup(context, width, height);
    }

    /**
     * show a list with popup
     *
     * @param context             activity context
     * @param width               the with for the popup content
     * @param maxHeight           the max height of popup, it is scrollable if the content is higher then maxHeight
     * @param adapter             the adapter for the list view
     * @param onItemClickListener the onItemClickListener for list item view
     * @return QMUIPopup
     */
    public static MsPopup listPopup(
            Context context, int width, int maxHeight,
            BaseAdapter adapter, AdapterView.OnItemClickListener onItemClickListener
    ) {
        ListView listView = new QMUIWrapContentListView(context, maxHeight);
        listView.setAdapter(adapter);
        listView.setVerticalScrollBarEnabled(false);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setDivider(null);
        return popup(context, width).setContextView(listView);
    }

    public static MsFullScreenPopup fullScreenPopup(Context context) {
        return new MsFullScreenPopup(context);
    }

    public static MsQuickActionPopup quickAction(Context context, int actionWidth, int actionHeight) {
        return new MsQuickActionPopup(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .actionWidth(actionWidth)
                .actionHeight(actionHeight);
    }
}
