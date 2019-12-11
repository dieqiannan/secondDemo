package com.sencondscreen;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 第二个屏幕的操作服务
 */
public class SecondScreenService extends Service {
    private final static String TAG = "SecondScreenService";

    private DataBinder binder = new DataBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
    }

    /**
     * 启动时，启动组件传递过来的Intent，如Activity可利用Intent封装所需要的参数并传递给Service
     * https://blog.csdn.net/hdhhd/article/details/80612726
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 解除绑定时调用
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "Service is invoke onUnbind");
        return super.onUnbind(intent);
    }


    public class DataBinder extends Binder{

        public DataBinder() {
        }

        public  SecondScreenService getService() {
            // 返回当前对象LocalService,这样我们就可在客户端端调用Service的公共方法了
            return SecondScreenService.this;
        }
    }
}
