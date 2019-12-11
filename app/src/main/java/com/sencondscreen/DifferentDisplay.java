package com.sencondscreen;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;


/**
 *
 */
public class DifferentDisplay extends Presentation {
    private static final String TAG = "DifferentDisplay";
    private static Context mContext;
    private TextView tvData;

    public DifferentDisplay(Context outerContext, Display display) {
        super(outerContext, display);
        mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_second_screen);
        tvData = findViewById(R.id.tv_data);
    }


    public void setData2Second(){
        tvData.setText("我是测试添加的数据");
    }
}
