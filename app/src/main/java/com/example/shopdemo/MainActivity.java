package com.example.shopdemo;

import androidx.appcompat.app.AlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.chislab.R;
import com.example.shopdemo.mufis.MufisLinkin;
import com.example.shopdemo.mufis.RegisterService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class MainActivity extends BaseActivity implements DataReceivedListener {

    public ImageView btn_a, btn_b, btn_c, btn_d, btn_e;
    public TextView txt_a, txt_b, txt_c, txt_d, txt_e, txt_a_score, txt_b_score, txt_c_score, txt_d_score, txt_e_score, Scoreinit, login_did;
    boolean ivFlag_a = false, ivFlag_b = false, ivFlag_c = false, ivFlag_d = false, ivFlag_e = false;

    public Button redirect_to_map, modify_ip;

    private String ipAddress = ""; // Variable to store entered IP address

    private String appid = "02609ea49f3940bf889deeae98f3c882";
    private String packageName = "com.changhong.cbgpay";
    static final String ACTION_START_paySDKReceiver = "simulate.usercenter.paySDKReceiver";
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String LOGIN_KEY = "login_key";

    //private static MainActivity instance; // Static instance variable

    private String mGlobalVarValue;


    public String getGlobalVarValue() {
        return mGlobalVarValue;
    }

    public void setGlobalVarValue(String str) {
        mGlobalVarValue = str;
    }

    // Static method to obtain the current instance of MainActivity
    //public static MainActivity getInstance() {
    //    return instance;
    //}

    //private MyNanoHTTPD httpdServer;

    int totalScore,totprice;
    boolean isScoreEnough;
    String xdomain = "http://124.70.85.59:20210";

    static final String TAG = "chislab::MainActivity";



    @Override
    protected  void onResume() {
        super.onResume();
        Log.d(TAG, "in MainActivity onResume");
        sendUIChangeMsg("MainActivity.png");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        MyNanoHTTPD nanoHTTPD = new MyNanoHTTPD();
        nanoHTTPD.startServer();
        nanoHTTPD.setDataReceivedListener(this);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);



        Intent intent = new Intent(MainActivity.this, RegisterService.class);
        startService(intent);

        btn_a = (ImageView) findViewById(R.id.btn_a);
        btn_b = (ImageView) findViewById(R.id.btn_b);
        btn_c = (ImageView) findViewById(R.id.btn_c);
        btn_d = (ImageView) findViewById(R.id.btn_d);
        btn_e = (ImageView) findViewById(R.id.btn_e);

        txt_a =(TextView) findViewById(R.id.text_a);
        txt_b =(TextView) findViewById(R.id.text_b);
        txt_c =(TextView) findViewById(R.id.text_c);
        txt_d =(TextView) findViewById(R.id.text_d);
        txt_e = (TextView) findViewById(R.id.text_e);

        txt_a_score=(TextView) findViewById(R.id.text_a_score);
        txt_b_score=(TextView) findViewById(R.id.text_b_score);
        txt_c_score=(TextView) findViewById(R.id.text_c_score);
        txt_d_score=(TextView) findViewById(R.id.text_d_score);
        txt_e_score=(TextView) findViewById(R.id.text_e_score);

        login_did = (TextView) findViewById(R.id.score);

        redirect_to_map = (Button) findViewById(R.id.button3);

        login_did.setText(getSavedLoginString());
        

        //String.valueOf(R.mipmap.btn_as)
        //String prod_a[] = { "球泡灯\n5500积分", String.valueOf(R.mipmap.btn_as), "球泡灯", String.valueOf(R.mipmap.bulblight_rec)};

        String prod_a[] = { "球泡灯\n5500积分", String.valueOf(R.mipmap.btn_as), "球泡灯", String.valueOf(R.mipmap.bulblight_rec)};
        String prod_b[] = { "蜡烛灯\n5500积分", String.valueOf(R.mipmap.btn_bs),"蜡烛灯", String.valueOf(R.mipmap.candle_rec)};
        String prod_c[] = { "智能墙插\n5500积分", String.valueOf(R.mipmap.btn_cs),"智能墙插", String.valueOf(R.mipmap.wallplug_rec)};
        String prod_d[] = { "智能面板\n5500积分", String.valueOf(R.mipmap.btn_ds), "智能面板", String.valueOf(R.mipmap.panel_rec)};
        String prod_e[] = { "双色筒灯\n5500积分", String.valueOf(R.mipmap.btn_es),"双色筒灯", String.valueOf(R.mipmap.bicolor_rec)};

        btn_a.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    txt_a_score.setTextColor(Color.WHITE);
                }else{
                    txt_a_score.setTextColor((Color.parseColor("#0097EF")));
                }
            }

        });

        btn_b.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    txt_b_score.setTextColor(Color.WHITE);
                }else{
                    txt_b_score.setTextColor((Color.parseColor("#0097EF")));
                }
            }
        });

        btn_c.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    txt_c_score.setTextColor(Color.WHITE);
                }else{
                    txt_c_score.setTextColor((Color.parseColor("#0097EF")));
                }
            }
        });

        btn_d.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    txt_d_score.setTextColor(Color.WHITE);
                }else{
                    txt_d_score.setTextColor((Color.parseColor("#0097EF")));
                }
            }
        });

        btn_e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    txt_e_score.setTextColor(Color.WHITE);
                }else{
                    txt_e_score.setTextColor((Color.parseColor("#0097EF")));
                }
            }
        });

        btn_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ivFlag_a){
                    ivFlag_a = true;
                    btn_a.setSelected(true);
                    //jump to new activity

                    startActivity(new Intent(MainActivity.this,PayActivity2.class).putExtra("name",prod_a[0]).putExtra("image",prod_a[1]).putExtra("prodname",prod_a[2]).putExtra("paysuccessimg",prod_a[3]));
                    //finish();

                    Log.e(TAG,"Product data sent "+ prod_a[0] + prod_a[1]);
                    //.getClass().getName()




                } else {
                    ivFlag_a = false;
                    btn_a.setSelected(false);
                }
            }
        });

        btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ivFlag_b){
                    ivFlag_b = true;
                    btn_b.setSelected(true);
                    //jump to new activity

                    startActivity(new Intent(MainActivity.this,PayActivity2.class).putExtra("name",prod_b[0]).putExtra("image",prod_b[1]).putExtra("prodname",prod_b[2]).putExtra("paysuccessimg",prod_b[3]));
                    //finish();
                    Log.e(TAG,"Product "+ prod_b[0] + prod_b[1]);


                } else {
                    ivFlag_b = false;
                    btn_b.setSelected(false);
                }
            }
        });

        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ivFlag_c){
                    ivFlag_c = true;
                    btn_c.setSelected(true);
                    //jump to new activity

                    startActivity(new Intent(MainActivity.this,PayActivity2.class).putExtra("name",prod_c[0]).putExtra("image",prod_c[1]).putExtra("prodname",prod_c[2]).putExtra("paysuccessimg",prod_c[3]));
                    //finish();
                    Log.e(TAG,"Product "+ prod_c[0] + prod_c[1]);

                } else {
                    ivFlag_c = false;
                    btn_c.setSelected(false);
                }
            }
        });

        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ivFlag_d){
                    ivFlag_d = true;
                    btn_d.setSelected(true);
                    //jump to new activity

                    startActivity(new Intent(MainActivity.this,PayActivity2.class).putExtra("name",prod_d[0]).putExtra("image",prod_d[1]).putExtra("prodname",prod_d[2]).putExtra("paysuccessimg",prod_d[3]));
                    //finish();
                    Log.e(TAG,"Product "+ prod_d[0] + prod_d[1]);


                } else {
                    ivFlag_d = false;
                    btn_d.setSelected(false);
                }
            }
        });

        btn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ivFlag_e){
                    ivFlag_e = true;
                    btn_e.setSelected(true);
                    //jump to new activity

                    startActivity(new Intent(MainActivity.this,PayActivity2.class).putExtra("name",prod_e[0]).putExtra("image",prod_e[1]).putExtra("prodname",prod_e[2]).putExtra("paysuccessimg",prod_e[3]));
//                    finish();
//                    startActiMvity(new Intent(MainActivity.this,device_loc.class));
//                    startActivity(new Intent(MainActivity.this, PointPayActivity.class));
                    Log.e(TAG,"Product "+ prod_e[0] + prod_e[1]);
                    //finish();

                } else {
                    ivFlag_e = false;
                    btn_e.setSelected(false);
                }
            }
        });

        redirect_to_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showSwitchDialog();

                startActivity(new Intent(MainActivity.this,device_loc.class));
                // Handle button click
                //Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        redirect_to_map.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Handle button selection (focus) change
                if (hasFocus) {
                    // Button is selected (focused)
                    redirect_to_map.setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    // Button is deselected (unfocused)
                    redirect_to_map.setTextColor(getResources().getColor(android.R.color.black));
                }
            }
        });




//        finish();


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
                    Log.e(String.valueOf(MainActivity.this), "onResponse: here");
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





    private void showSwitchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_switch);

        builder.setTitle("设置");

        AlertDialog dialog = builder.create();
        dialog.show();

        Switch featureSwitch = dialog.findViewById(R.id.featureSwitch);
        TextView switchStateText = dialog.findViewById(R.id.switchStateText);

        featureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchStateText.setText("ON");
                    MufisLinkin.switchOnLight();
                } else {
                    switchStateText.setText("OFF");
                    MufisLinkin.switchOffLight();
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the switch state here
                if (featureSwitch.isChecked()) {
                    Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onDataReceived(String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("允许使用以下DID登陆吗？");
                builder.setMessage(data);

                // Add a "Confirm" button
                builder.setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle confirmation action here
                        //login_did.setText(data);
                        saveLoginString(data);
                        login_did.setText(getSavedLoginString());
                        Toast.makeText(MainActivity.this, "DID:CTID 登录成功", Toast.LENGTH_SHORT).show();
                        //showSwitchDialog();
                        dialog.dismiss();

                        //light1.setOnClickListener(new View.OnClickListener() {
                        //    @Override
                        //    public void onClick(View v) {
                                // Check if light1 is set to light_green
                         //       if (light1.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.light_green).getConstantState())) {
                                    // If image is light_green, execute the action
                           //         showSwitchDialog();
                            //    } else {
                                    // If image is not light_green, show a toast message
                              //      Toast.makeText(getApplicationContext(), "您无权控制该设备!", Toast.LENGTH_SHORT).show();
                               // }
                            //}
                        //});

                    }
                });

                // Add a "Reject" button
                builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle rejection action here
                        Toast.makeText(MainActivity.this, "DID:CTID 登录失败", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    private void saveLoginString(String loginString) {
        // Get SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store the login string
        editor.putString(LOGIN_KEY, loginString);

        // Apply changes
        editor.apply();
    }

    // Method to retrieve the stored login string
    private String getSavedLoginString() {
        return sharedPreferences.getString(LOGIN_KEY, null);
    }




    //private void startBaseActivity(String ipAddress) {
    //    Intent intent = new Intent(MainActivity.this, BaseActivity.class);
    //    intent.putExtra("IP_ADDRESS", ipAddress);
      //  startActivity(intent);

        //MufisLinkin.initialize(ipAddress);
    //}









}


