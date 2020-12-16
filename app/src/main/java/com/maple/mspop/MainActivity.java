package com.maple.mspop;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.maple.mspop.ui.BasePopupWindow;
import com.maple.mspop.ui.MorePopWindow;


public class MainActivity extends FragmentActivity {
    BasePopupWindow.AZIMUTH azimuth = BasePopupWindow.AZIMUTH.Bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.bt_left_top).setOnClickListener(v -> clickMore(v));
        findViewById(R.id.bt_left_bottom).setOnClickListener(v -> clickMore(v));
        findViewById(R.id.tv_right).setOnClickListener(v -> clickMore(v));
        findViewById(R.id.bt_center).setOnClickListener(v -> clickMore(v));
        findViewById(R.id.bt_right_bottom).setOnClickListener(v -> clickMore(v));
        ((RadioGroup) findViewById(R.id.rg_show_location)).setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_up) {
                azimuth = BasePopupWindow.AZIMUTH.Top;
            } else if (checkedId == R.id.rb_down) {
                azimuth = BasePopupWindow.AZIMUTH.Bottom;
            } else if (checkedId == R.id.rb_left) {
                azimuth = BasePopupWindow.AZIMUTH.Left;
            } else if (checkedId == R.id.rb_right) {
                azimuth = BasePopupWindow.AZIMUTH.Right;
            }
        });
    }

    private void clickMore(View v) {
        new MorePopWindow(getBaseContext())
                .setOnItemClickListener(v1 -> {
                    switch (v1.getId()) {
                        case R.id.tvClearPagePen:
                            Toast.makeText(getApplication(), "clear pen", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.tvClearPagePenAndContents:
                            Toast.makeText(getApplication(), "clear page", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.tvClearPageRecords:
                            Toast.makeText(getApplication(), "clear page all", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.tvDelPage:
                            Toast.makeText(getApplication(), "del page", Toast.LENGTH_SHORT).show();
                            break;
                    }
                })
                .setAlphaStyle(this, 0.6f)
                .showPopupWindow(v, azimuth, 2f);
    }
}
