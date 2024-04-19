package com.example.shopdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.changhong.jedge.ap.ActivePage;
import com.changhong.qlib.util.sync.SystemUtils;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    static final String TAG = "WJG-TEST";
    private static ActivePage apApp = null;
    private static ActivePage.APDevice device = null;

    String ip="192.168.68.15";

    public static String ipAddress="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ipAddress = AppSettings.getInstance().getIpAddress();
        //apApp = ActivePage.getInstance("TVUISync", ipAddress);
        //Log.e(TAG, "Invalid IP address: "+ipAddress);

        //MainActivity mainActivity = MainActivity.getInstance();

        //ipAddress = mainActivity.getGlobalVarValue();

        //ipAddress = getIntent().getStringExtra("IP_ADDRESS");


        apApp = ActivePage.getInstance("TVUISync");

        //apApp = ActivePage.getInstance("TVUISync",ip);
        if(!apApp.start()) {
            Log.d(TAG, "Fail to start ap app : " + apApp.getName());
            return;
        }
        if(device == null)
            startRegisterDevice();
    }

    protected void sendUIChangeMsg(String pageName)
    {
        if(device != null) {
            Log.d(TAG, "send UI Change msg, pageName is:" + pageName);
            new Thread(() -> {
                device.updateProperty("currentUI", pageName);
            }).start();
        }

    }

    private void startRegisterDevice() {
        new Thread(() -> {
            while(!apApp.isActive()) {
                //等待接入成功
                Log.d(TAG, "Wait for apApp to connect to mgbus : " + apApp.getName());
                SystemUtils.tryWait(2000);
            }
            Log.d(TAG, "registerActivePageOpenDevice");
            device = apApp.registerActivePageOpenDevice(
                    "Controller",
                    "TVUIController",
                    "客厅电视",
                    "测试开发者",
                    "UI控制器",
                    "1.0");
        }).start();
    }


}

