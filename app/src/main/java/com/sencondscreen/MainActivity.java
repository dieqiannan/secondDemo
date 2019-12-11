package com.sencondscreen;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Presentation;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    String[] mWINDOW = new String[]{
            Manifest.permission.SYSTEM_ALERT_WINDOW};
    private static final int MY_PERMISSIONS_REQUEST_WINDOW = 2;

    public DisplayManager mDisplayManager;
    public DifferentDisplay mPresentation;
    /**
     * 屏幕数组
     **/
    private Display[] displays;
    private SecondScreenService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplayManager = (DisplayManager) MainActivity.this.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        TextView tvDisplayNum = findViewById(R.id.tv_display_num);
        tvDisplayNum.setText(displays.length+"");

        findViewById(R.id.tv_request_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
                        Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_WINDOW);
                    } else {

                    }
                }


            }
        });

        findViewById(R.id.tv_way_Presentation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "屏幕数量=" + displays.length);

                mPresentation = new DifferentDisplay(MainActivity.this, displays[displays.length - 1]);
                mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                mPresentation.show();

            }
        });


        findViewById(R.id.tv_01_add_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresentation == null) {
                    return;
                }
                mPresentation.setData2Second();

            }
        });

        findViewById(R.id.tv_way_startActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    options = ActivityOptions.makeBasic();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    options.setLaunchDisplayId(1);        //这里一直display0是第一块屏；display1是第二块屏
                }
                Intent secondIntent = new Intent();
                ComponentName cn = new ComponentName("com.sencondscreen", "com.sencondscreen.SecondActivity");
                secondIntent.setComponent(cn);
                secondIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(secondIntent, options.toBundle());

            }
        });

        //绑定服务

        findViewById(R.id.tv_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent(MainActivity.this, SecondScreenService.class);
                bindService(intent, conn, Service.BIND_AUTO_CREATE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ServiceConnection conn = new ServiceConnection() {
        /**
         * 与服务器端交互的接口方法 绑定服务的时候被回调，在这个方法获取绑定Service传递过来的IBinder对象，
         * 通过这个IBinder对象，实现宿主和Service的交互。
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "绑定成功调用：onServiceConnected");
            // 获取Binder
            SecondScreenService.DataBinder binder = (SecondScreenService.DataBinder) service;
            mService = binder.getService();
        }
        /**
         * 当取消绑定的时候被回调。但正常情况下是不被调用的，它的调用时机是当Service服务被意外销毁时，
         * 例如内存的资源不足时这个方法才被自动调用。
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService=null;
        }

    };
}
