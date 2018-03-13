package com.example.maple.mspopupwindow;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.maple.mspopupwindow.ui.BasePopupWindow;
import com.example.maple.mspopupwindow.ui.MorePopWindow;

public class MainActivity extends FragmentActivity {

    Button bt_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bt_more = findViewById(R.id.bt_more);
        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMore(v);
            }
        });
    }

    private void clickMore(View v) {
        new MorePopWindow(getBaseContext(), v)
                .setOnItemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.tvClearPagePen:
                                Toast.makeText(getApplication(),"clear pen",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tvClearPagePenAndContents:
                                Toast.makeText(getApplication(),"clear page",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tvClearPageRecords:
                                Toast.makeText(getApplication(),"clear page all",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tvDelPage:
                                Toast.makeText(getApplication(),"del page",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .showPopupWindow();
    }
}
