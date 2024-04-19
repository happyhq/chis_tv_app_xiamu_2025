package com.example.shopdemo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.chislab.R;
import com.changhong.chislab.activity.PointPayActivity;
import com.changhong.voiceprintregistration.pay.PayCallBack;
import com.changhong.voiceprintregistration.pay.PayTask;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PayActivity2 extends BaseActivity {

    ImageView imageView, backhome;
    TextView textView, Scoreinit;
    static String prodname, prodimage, namep, imageuni ;

    private String appid = "02609ea49f3940bf889deeae98f3c882";
    private String packageName = "com.changhong.cbgpay";
    static final String TAG = "chislab::PayActivity2";
    static final String ACTION_START_paySDKReceiver = "simulate.usercenter.paySDKReceiver";
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";

    Context context;
    Button btn_shop;
    public Context cxt;
    public PayTask payTask;
    int totalScore,totprice;
    boolean isScoreEnough;
    String xdomain = "http://124.70.85.59:20210";
//    Intent intent = getIntent();

    //ArrayList<String> mylist = new ArrayList<String>();

//    public ArrayList<String> getList() {
//        return mylist;
//    }

    class VoicePaySecurityCallBack extends PayCallBack
    {
        @Override
        public void notifyPayCall(String code, String msg) {
            PayResult response = new Gson().fromJson(msg, PayResult.class);
            Log.e(TAG,"res full "+ msg);
            Log.e(TAG,"res is: "+ response.getres_code());
            if (response.getres_code().equals("200")){
                Toast.makeText(PayActivity2.this,"pay success!", Toast.LENGTH_LONG).show();
//                Log.e(TAG,"Product name: "+ namep);
//                Log.e(TAG,"Product image: "+ imageuni);
//                startActivity(new Intent(PayActivity2.this, PointPayActivity.class).putExtra("prodname",namep).putExtra("paysuccessimg",imageuni));
                startActivity(new Intent(PayActivity2.this, PointPayActivity.class));
//                startPointPayActivity();

            }else{
                Toast.makeText(PayActivity2.this,"pay fail! ", Toast.LENGTH_LONG).show();
            }
        }
    }

//    public void startPointPayActivity()
//    {
//        startActivity(new Intent(PayActivity2.this, PointPayActivity.class).putExtra("prodname",namep).putExtra("paysuccessimg",imageuni));
//
////        startActivity(new Intent(PayActivity2.this, PointPayActivity.class));
//    }

    @Override
    protected  void onResume() {
        super.onResume();
        Log.d(TAG, "in PayActivity2 Activity onResume");
        sendUIChangeMsg("PayActivity2.png");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay2);
        getSupportActionBar().hide();

        imageView = findViewById(R.id.btn_a);
        textView = findViewById(R.id.text_a);
        backhome = findViewById(R.id.back);

        Intent intent = getIntent();

//        if(intent.getExtras() != null){
            prodname = intent.getStringExtra("name");
            prodimage = intent.getStringExtra("image");

            namep = intent.getStringExtra("prodname");
        Log.e(TAG,"Product name : "+ namep);
            imageuni = intent.getStringExtra("paysuccessimg");
//        startActivity(new Intent(PayActivity2.this,PointPayActivity.class).putExtra("prodname",namep).putExtra("paysuccessimg",imageuni));

//        Log.e(TAG,"Product name: "+ namep);
//        Log.e(TAG,"Product image: "+ imageuni);

//        BuyDeviceInfo.product_name = namep;
//        BuyDeviceInfo.imageUI = imageuni;

            textView.setText(prodname);
            imageView.setImageResource(Integer.parseInt(prodimage));

//        startActivity(new Intent(PayActivity2.this, PointPayActivity.class));




        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PayActivity2.this, MainActivity.class));

            }
        });

        cxt=getApplicationContext();
        payTask=new PayTask(cxt,appid,packageName,new PayActivity2.VoicePaySecurityCallBack());
        btn_shop = (Button) findViewById(R.id.buy);
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG,"Button buy clicked");

                showPaymentSuccessToast(prodname, Integer.parseInt(prodimage));

                startActivity(new Intent(PayActivity2.this, MainActivity.class));



                //startActivity(new Intent(PayActivity2.this, PointPayActivity.class));

                //payTask.startPay();
                //Map<String,Object> pay_msg = get_pay_msg();
                //Log.i(TAG,"order message is: "+new Gson().toJson(pay_msg));
                //try {
                //    payTask.VoicePay(new Gson().toJson(pay_msg));

                //} catch (Exception e) {
                 //   e.printStackTrace();
                //}

            }
        });



        Scoreinit = findViewById(R.id.score);

        totprice = 0;

        //post request
//                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                JSONObject json = new JSONObject();
//                try {
//                    json.put("serialNumber", serialNumber);
//                    json.put("pageNum", pageNum);
//                    json.put("pageSize", pageSize);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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
                        //Toast.makeText(getBaseContext(), "Get score failed!" , Toast.LENGTH_SHORT ).show();
                    }
                });

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Log.e(String.valueOf(PayActivity2.this), "onResponse: here");
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
                                Scoreinit.setText(String.valueOf(totalScore));

//                                for (int i=0; i < 10; i++)
//                                {
//                                    Toast.makeText(getBaseContext(), "总积分:" + totalScore, Toast.LENGTH_LONG ).show();
//                                }
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
                                //Toast.makeText(getBaseContext(), "Getting score failed", Toast.LENGTH_SHORT ).show();
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
                            //Toast.makeText(getBaseContext(), "The purchase is successful！", Toast.LENGTH_SHORT ).show();
                        }
                    });


                }else{
                    //积分不够，提示充值
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getBaseContext(), "Score is not enough!", Toast.LENGTH_SHORT ).show();
                        }
                    });

                }
            }
        });

        




        //imageid = selectedprice.toString();

//        }

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
                        //Toast.makeText(getBaseContext(), "Update score failed", Toast.LENGTH_SHORT).show();
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
                                //Toast.makeText(getBaseContext(), "Update score success!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 更新UI的操作
                                //Toast.makeText(getBaseContext(), "Update score failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        //Log.i(TAG,"outside scope is: "+ mylist);
        super.onDestroy();
        payTask.unbindService();
    }

    //    生成下单信息，为订单信息得content，主要是购买商品信息  业务参数
    public static Map<String,Object> get_pay_msg(){
        Map<String, Object> pay_msg = new LinkedHashMap<>();
        String orderID = genid();
        //Log.i(TAG,"outside scope is: "+ mylist);
        pay_msg.put("mOrderInfoNo",orderID);
        pay_msg.put("mBgRetUrl","http://47.112.10.111:8000/seller/callback/");
        pay_msg.put("mPrize","5500");
        pay_msg.put("mShoptitle","测试订单");
        pay_msg.put("mPaynumber",namep);
        pay_msg.put("mAtt","CNY");
        pay_msg.put("mIcon",imageuni+"#"+namep);
        return pay_msg;
    }
    //    订单号生成
    public static String genid(){
        //Log.i(TAG,"outside scope is: "+ mylist);
        String orderId = "6"+System.currentTimeMillis()+""+(System.nanoTime()+"").substring(7,10);
        Log.i(TAG,"orderID is: "+orderId);
        return orderId;
    }

    private void showPaymentSuccessToast(String productName, int productImageResId) {
        // Inflate custom toast layout
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                findViewById(R.id.custom_toast_container));

        // Set product name
        TextView textProductName = layout.findViewById(R.id.textProductName);
        textProductName.setText(productName);

        // Set product image
        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageResource(productImageResId);

        // Create and show custom toast
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        toast.show();
    }

}