package com.example.shopdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.changhong.chislab.R;
import com.google.gson.Gson;

import java.util.Map;

public class AuthActivity extends BaseActivity {
    String devname, devid, lightname, lightid;
    ImageView imageView;
    TextView textView, textView2;
    Button btn_agree,btn_reject;
    static final String TAG = "chislab::AuthActivity";

    @Override
    protected  void onResume() {
        super.onResume();
        Log.d(TAG, "in AuthActivity Activity onResume");
        sendUIChangeMsg("AuthActivity_activity.png");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportActionBar().hide();

        Intent intent = getIntent();

//        if(intent.getExtras() != null){
        devname = intent.getStringExtra("padname");
        devid = intent.getStringExtra("padid");



        lightname = intent.getStringExtra("nickx");
        lightid = intent.getStringExtra("deviceIDx");



//        devname = "智能面板";

        imageView = findViewById(R.id.device);
        textView = findViewById(R.id.deviceName);
        textView2 = findViewById(R.id.address);


        if(devname != null && devid != null){
            Log.v(TAG, "Pad added, \n pad_ip:" + devid + "\nPad_name:" + devname);
        textView.setText(devname);
        textView2.setText(devid);
        imageView.setImageResource(R.drawable.panel);
        } else if(lightname != null && lightid != null){
            Log.v(TAG, "Light added, \n Light_id:" + lightid + "\nLight_name:" + lightname);
            textView.setText(lightname);
            textView2.setText(lightid);
            imageView.setImageResource(R.drawable.bulblight);
        }



        btn_agree =  findViewById(R.id.agree);
        btn_reject = findViewById(R.id.reject);
        btn_agree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    btn_agree.setTextColor(Color.WHITE);
                }else {
                    btn_agree.setTextColor(Color.parseColor("#00C4FF"));
                }
            }
        });
        btn_reject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    btn_reject.setTextColor(Color.WHITE);
                }else {
                    btn_reject.setTextColor(Color.parseColor("#00C4FF"));
                }
            }
        });


        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(AuthActivity.this,device_loc.class); intent.putExtra("imagegreen",R.drawable.panel_green);
//                startActivity(intent);
                Intent intentData = new Intent();
                intentData.putExtra("result", "true");//封装需要返回的数据
                setResult(RESULT_OK,intentData);//设置返回的数据，无数据返回时，可直接写成：setResult(RESULT_OK,null);
                finish();//销毁当前Activity

            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(AuthActivity.this,device_loc.class);
                Intent intentData = new Intent();
                intentData.putExtra("result", "false");//封装需要返回的数据
                setResult(RESULT_OK,intentData);//设置返回的数据，无数据返回时，可直接写成：setResult(RESULT_OK,null);
                finish();//销毁当前Activity
            }
        });


        Log.e(TAG,"Product "+ devname + devid);





    }
}