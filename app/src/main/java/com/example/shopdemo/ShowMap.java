package com.example.shopdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.changhong.chislab.R;
import com.changhong.chislab.activity.PointPayActivity;
import com.changhong.voiceprintregistration.pay.PayCallBack;
import com.google.gson.Gson;

public class ShowMap extends BaseActivity {

    static final String TAG = "chislab::Showmap test";

    class VoicePaySecurityCallBack extends PayCallBack
    {
        @Override
        public void notifyPayCall(String code, String msg) {
            code = "sdfs";
            msg = "sdfsdf";
//            PayResult response = new Gson().fromJson(msg, PayResult.class);
//            Log.e(TAG,"res full "+ msg);
//            Log.e(TAG,"res is: "+ response.getres_code());
//            if (response.getres_code().equals("200")){
                Toast.makeText(ShowMap.this,"pay success!", Toast.LENGTH_LONG).show();

                startActivity(new Intent(ShowMap.this, PointPayActivity.class));

//            }
        }
    }

    @Override
    protected  void onResume() {
        super.onResume();
        Log.d(TAG, "in ShowMap Activity onResume");
        sendUIChangeMsg("Show_map_activity.png");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapditu);
        Log.e(TAG,"Passed initial call");
        startActivity(new Intent(ShowMap.this, PointPayActivity.class));
        String re = null,ui = null;
//        VoicePaySecurityCallBack();
        Log.e(TAG,"Passed second call");
    }
}