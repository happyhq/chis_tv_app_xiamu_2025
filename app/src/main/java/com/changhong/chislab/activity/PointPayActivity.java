package com.changhong.chislab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.chislab.R;
import com.example.shopdemo.BaseActivity;
import com.example.shopdemo.BuyDeviceInfo;
import com.example.shopdemo.MainActivity;
import com.example.shopdemo.PayActivity2;
import com.example.shopdemo.VoiceBuyDeviceInfo;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class PointPayActivity extends BaseActivity {



    ImageView imageView, backhome;
    TextView textView,textViewprice;
    TextView Score1;

    boolean isScoreEnough;
    int totalScore;
    String xdomain = "http://124.70.85.59:20210";

    String imageprod, nameprod;

    static final String TAG = "chis:PointPayActivity";

    String Nameprod,prodimage, voicepr, voiceprodname;
    int totprice;

    @Override
    protected  void onResume() {
        super.onResume();
        Log.d(TAG, "in PointPayActivity onResume");
        sendUIChangeMsg("PointPayActivity.png");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay_success);
        getSupportActionBar().hide();


        imageView = findViewById(R.id.device);
        textView = findViewById(R.id.deviceName);
        //textViewprice = findViewById(R.id.pricesSuccess);

        Score1 = findViewById(R.id.score_remain);
        backhome = findViewById(R.id.back);
        Log.e(TAG,"Arrived in Pointpay activity ");

        Intent intent = getIntent();
//        String aaa = intent.getStringExtra("mSuccessIcon");
        Nameprod = intent.getStringExtra("mSuccessNum");

        prodimage = intent.getStringExtra("mSuccessIcon");
        Log.e(TAG,"Arrived in Pointpay activity and passed settext  "+ prodimage);
//        Nameprod = BuyDeviceInfo.product_name;
        voiceprodname = VoiceBuyDeviceInfo.voice_product_name;
//        prodimage = BuyDeviceInfo.imageUI;

        String pdname, pdimage;

        pdname = prodimage.substring(prodimage.indexOf("#")).replace("#", "");;
        pdimage = prodimage.substring(0, prodimage.indexOf("#"));;

//        Nameprod = "智能面板";
//        prodimage = String.valueOf(R.mipmap.panel_rec);

        Log.e(TAG,"Product name: "+ pdname);
        Log.e(TAG,"Product image: "+ pdimage);

//        if(voiceprodname == null){
            textView.setText(pdname);

//            Log.e(TAG,"Arrived in Pointpay activity and passed settext  "+ aaa);

            imageView.setImageResource(Integer.parseInt(pdimage));

            Log.e(TAG,"Arrived in Pointpay activity and passed settext and setimage ");

//         if(voiceprodname != null)
//        {
//            if(voiceprodname.equals("智能面板"))
//            {
//                textView.setText(voiceprodname);
//                imageView.setImageResource(R.mipmap.panel_rec);
//            } else {
//                textView.setText(voiceprodname);
//                imageView.setImageResource(R.mipmap.bulblight_rec);
//            }
//
//        }
//


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PointPayActivity.this, MainActivity.class));

            }
        });

            totprice = 5500;

            isScoreEnough = false;

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
            Request request = new Request.Builder()
                    .url(xdomain + "/getScore")
                    .post(requestBody)
                    .build();

//        Log.e(TAG,"Passed stage 1 "+ request);
            //异步请求
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "获取积分失败" , Toast.LENGTH_SHORT ).show();
                        }
                    });

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Log.e(String.valueOf(PointPayActivity.this), "onResponse: here");
                        JSONObject jsonObj = new JSONObject(response.body().string());
                        int resCode = jsonObj.getInt("code");
                        if(resCode==200){
                            JSONArray data = jsonObj.getJSONArray("data");
                            totalScore = data.getInt(0);
                            //Log.e(TAG,"Total passed: "+ totalScore);
                            //Score1.setText(String.valueOf(totalScore));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Score1.setText(String.valueOf(totalScore));
                                    Toast.makeText(getBaseContext(), "剩余积分:" + totalScore, Toast.LENGTH_SHORT ).show();
                                }
                            });

                            if (totalScore>= totprice){
                                isScoreEnough = true;
                            }else{
                                isScoreEnough = false;
                            }
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "获取积分失败", Toast.LENGTH_SHORT ).show();
                                }
                            });

                        }
                    }catch (JSONException e) {

                    }
                    if(isScoreEnough ==true){
                        //积分足够，进入购买流程,并将使用积分上链
                        buyRequest(totprice);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "购买成功！", Toast.LENGTH_SHORT ).show();
                            }
                        });


                    }else{
                        //积分不够，提示充值
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "剩余积分不足！", Toast.LENGTH_SHORT ).show();
                            }
                        });

                    }
                }
            });
            //Toast.makeText(getBaseContext(), "You clicked PAY button! You need to pay: "+ totprice, Toast.LENGTH_SHORT ).show();
            //Looper.loop();



    }
    //登录验证S
    private void buyRequest(int score) {
        JSONObject json = new JSONObject();
        try {
            json.put("score", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));

        //Log.e(TAG,"res full ");
        Request request = new Request.Builder()
                .url(xdomain + "/buyProduct")
                .post(requestBody)
                .build();
        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "积分更新失败！", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObj = new JSONObject(response.body().string());
                    int resCode = jsonObj.getInt("code");
                    if (resCode == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "积分更新成功！", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 更新UI的操作
                                Toast.makeText(getBaseContext(), "积分更新失败！", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {

                }
            }
        });
    }
}


