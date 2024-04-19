package com.example.shopdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.chislab.R;
import com.example.shopdemo.mufis.MufisLinkin;
import com.example.shopdemo.mufis.RegisterService;
import com.google.gson.Gson;

import com.example.shopdemo.lightselectsettings;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static com.example.shopdemo.mufis.MufisLinkin.ACTION_ASK_RESULT;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_DEVICE_ADD;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_DEVICE_CHISID_ACCESS;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_SmartPad_ADD;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_VOICE_COMMAND;

public class device_loc extends BaseActivity{
    private Button btn_light1, btn_light2, btn_panel, resetbtn;
    public ImageView panel1, panel2, panel3, light1, light2, light3, panel_temp, light_temp;
    String padName, padIp, padModule, dvID, dvType, dvNickname, msglogg, paramlog;

    TextView logindid;
    public Context cxt;
    TextView tvSelectedItemPreview;
    static final String TAG = "Device_Loc_Activity";
    String devID_1, running_state_1;
//    boolean ivFlag_a = false, ivFlag_b = false, ivFlag_c = false, ivFlag_d = false, ivFlag_e = false, ivFlag_f = false;
    public final static int Auth_ACTIVITY = 0;
    public final static int Auth_Light_ACTIVITY2 = 0;

    static int panelList[] = new int[]{0,0,0}; // Create an ArrayList object
    static int panel_temp_num=0;
    static int lightList[] = new int[]{0,0,0}; // Create an ArrayList object

    ArrayList<Integer> activeLights = new ArrayList<>();
    static int light_temp_num=0;

    public Boolean Light1_passed = Boolean.FALSE, Light2_passed = Boolean.FALSE, Light3_passed = Boolean.FALSE;

    private static final String PREF_NAME = "ImageStatePref";
    private static final String KEY_LIGHT1_STATE = "light1_state";

    //private MyNanoHTTPD httpdServer;

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
                    Toast.makeText(device_loc.this, "ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(device_loc.this, "OFF", Toast.LENGTH_SHORT).show();
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
    protected  void onResume() {
        super.onResume();
        Intent intent = getIntent();
        dvID = intent.getStringExtra("deviceIDx");
        dvNickname = intent.getStringExtra("nickx");
        Log.d(TAG, "in Device_Loc Activity onResume");
        sendUIChangeMsg("device_loc_activity.png");
        if(panelList[0]==1){
            panel1.setImageResource(R.drawable.panel_green);
        }else{
            panel1.setImageResource(R.drawable.panel_bg);
        }
        if(panelList[1]==1){
            panel2.setImageResource(R.drawable.panel_green);
        }else{
            panel2.setImageResource(R.drawable.panel_bg);
        }
        if(panelList[2]==1){
            panel3.setImageResource(R.drawable.panel_green);
        }else{
            panel3.setImageResource(R.drawable.panel_bg);
        }

        if(lightList[0]==1){
            light1.setImageResource(R.drawable.light_green);

            // Save the image state to SharedPreferences
            //saveImageState1(true);

            //MyNanoHTTPD nanoHTTPD = new MyNanoHTTPD();
            //nanoHTTPD.startServer();
            //nanoHTTPD.setDataReceivedListener(this);

            light1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSwitchDialog();

                }
            });





        }else{
            light1.setImageResource(R.drawable.light_bg);
        }
        if(lightList[1]==1){
            light2.setImageResource(R.drawable.light_green);

            // Save the image state to SharedPreferences
            //saveImageState2(true);

            //MyNanoHTTPD nanoHTTPD = new MyNanoHTTPD();
            //nanoHTTPD.startServer();
            //nanoHTTPD.setDataReceivedListener(this);

            light2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSwitchDialog();

                }
            });



        }else{
            light2.setImageResource(R.drawable.light_bg);
        }
        if(lightList[2]==1){
            light3.setImageResource(R.drawable.light_green);

            // Save the image state to SharedPreferences
            //saveImageState3(true);

            //MyNanoHTTPD nanoHTTPD = new MyNanoHTTPD();
            //nanoHTTPD.startServer();
            //nanoHTTPD.setDataReceivedListener(this);

            light3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSwitchDialog();

                }
            });



        }else{
            light3.setImageResource(R.drawable.light_bg);
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        getSupportActionBar().hide();
        cxt=getApplicationContext();


        panel1 = (ImageView) findViewById(R.id.panel1);
        panel2 = (ImageView) findViewById(R.id.panel2);
        panel3 = (ImageView) findViewById(R.id.panel3);
        light1 = (ImageView) findViewById(R.id.light1);
        light2 = (ImageView) findViewById(R.id.light2);
        light3 = (ImageView) findViewById(R.id.light3);

        // Retrieve image state from SharedPreferences

        //loadSavedImageState1();
        //loadSavedImageState2();
        //loadSavedImageState3();

        logindid = (TextView) findViewById(R.id.profiledid);
        resetbtn = (Button) findViewById(R.id.buttonreset);

        //light1.setImageResource(R.drawable.light_red);

        Intent intent = getIntent();
        Intent intenti = new Intent(device_loc.this,AuthActivity.class);

        // if(intent.getExtras() != null){
        //Device_Add action
        dvID = intent.getStringExtra("deviceIDx");
        dvNickname = intent.getStringExtra("nickx");
        Bundle bundle = new Bundle();

        bundle.putString("deviceIDx", dvNickname);
        bundle.putString("nickx", dvID);

        //dvType = intent.getStringExtra("devitypex");

        lightselectsettings.getInstance().setlightmacAddress(dvID);
        MufisLinkin.initializelight();

        if(dvID != null && dvNickname != null){
            Log.v(TAG, "Device added, \n Device_id:" + dvID + "\nNickname:" + dvNickname);

            for (int i=0; i < 5; i++)
            {
            Toast.makeText(getBaseContext(), "发现新的设备:"+dvNickname+" "+dvID+"接入进网络", Toast.LENGTH_LONG ).show();
            }

                light1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        light1.setImageResource(R.drawable.light_red);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(5); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        light1.startAnimation(anim);

                        light1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                light1.clearAnimation();


                                light_temp = light1;
                                light_temp_num=0;
//                                startActivity(new Intent(device_loc.this,AuthActivity.class).putExtra("padname",padName).putExtra("padid",padIp));


                                intenti.putExtras(bundle);
                                intenti.putExtra("nickx",dvNickname);
                                intenti.putExtra("deviceIDx",dvID);
                                startActivityForResult(intenti,Auth_Light_ACTIVITY2);

                            }
                        });

                    }
                });

//            }

            light2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    light2.setImageResource(R.drawable.light_red);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(5); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    light2.startAnimation(anim);

                    light2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            light2.clearAnimation();

                            //toggleLightState(light2, 1); // Toggle state for light2

                            light_temp = light2;
                            light_temp_num=1;
//                                startActivity(new Intent(device_loc.this,AuthActivity.class).putExtra("padname",padName).putExtra("padid",padIp));


                            intenti.putExtras(bundle);
                            intenti.putExtra("nickx",dvNickname);
                            intenti.putExtra("deviceIDx",dvID);
                            startActivityForResult(intenti,Auth_Light_ACTIVITY2);

                        }
                    });

                }
            });

            light3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    light3.setImageResource(R.drawable.light_red);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(5); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    light3.startAnimation(anim);

                    light3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            light3.clearAnimation();

                            light_temp = light3;
                            light_temp_num=2;
//                                startActivity(new Intent(device_loc.this,AuthActivity.class).putExtra("padname",padName).putExtra("padid",padIp));


                            intenti.putExtras(bundle);
                            intenti.putExtra("nickx",dvNickname);
                            intenti.putExtra("deviceIDx",dvID);
                            startActivityForResult(intenti,Auth_Light_ACTIVITY2);

                        }
                    });

                }
            });

//            }

        }

        //Pad_Add action
        padName = intent.getStringExtra("padname");
        padIp = intent.getStringExtra("padip");
        //padModule = intent.getStringExtra("padmodule");
        bundle.putString("padname", padName);
        bundle.putString("padip", padIp);

        if(padIp != null){
            padName = "智能面板";
            //Log.v(TAG, "Pad added, \n Padname:" + padName + "\nPad_IP:" + padIp);

            for (int i=0; i < 5; i++)
            {
                Toast.makeText(getBaseContext(), "发现新的智能面板:"+padName+" "+padIp+"接入进网络", Toast.LENGTH_LONG ).show();
            }


                panel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //panel1.clearAnimation();


                        panel1.setImageResource(R.drawable.panel_red);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(5); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        panel1.startAnimation(anim);

                        panel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                panel1.clearAnimation();

                                panel_temp = panel1;
                                panel_temp_num=0;
//                                startActivity(new Intent(device_loc.this,AuthActivity.class).putExtra("padname",padName).putExtra("padid",padIp));


                                intenti.putExtras(bundle);
                                intenti.putExtra("padname",padName);
                                intenti.putExtra("padid",padIp);
                                startActivityForResult(intenti,Auth_ACTIVITY);




                            }
                        });

                        Log.v(TAG, "You clicked Panel 1");

                    }
                });


                panel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //panel2.clearAnimation();
                        panel2.setImageResource(R.drawable.panel_red);

                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(5); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        panel2.startAnimation(anim);

                        panel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                panel2.clearAnimation();

                                panel_temp = panel2;
                                panel_temp_num=1;
                                intenti.putExtras(bundle);
                                intenti.putExtra("padname",padName);
                                intenti.putExtra("padid",padIp);
                                startActivityForResult(intenti,Auth_ACTIVITY);

//                                startActivity(new Intent(device_loc.this,AuthActivity.class).putExtra("padname",padName).putExtra("padid",padIp));

                            }
                        });

                        Log.v(TAG, "You clicked Panel 2");

                    }
                });

                panel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        panel3.clearAnimation();
                        panel3.setImageResource(R.drawable.panel_red);

                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(5); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        panel3.startAnimation(anim);

                        panel3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                panel3.clearAnimation();

                                panel_temp = panel3;
                                panel_temp_num=2;

                                intenti.putExtras(bundle);
                                intenti.putExtra("padname",padName);
                                intenti.putExtra("padid",padIp);
                                startActivityForResult(intenti,Auth_ACTIVITY);

//                                startActivity(new Intent(device_loc.this,AuthActivity.class).putExtra("padname",padName).putExtra("padid",padIp));

                            }
                        });

                        Log.v(TAG, "You clicked Panel 3");

                    }
                });

           // }

        }

        String product = intent.getStringExtra("product");
//        String counts = intent.getStringExtra("counts");

        if(product != null) {

            startActivity(new Intent(device_loc.this,VoiceCommandOrder.class).putExtra("voiceprod",product));

            //Log.v(TAG, "Voice received, \n product:" + product + "\ncounts:" + counts);
        }

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart the app by restarting MainActivity
                Intent intent = new Intent(device_loc.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // Finish the current instance of MainActivity
                finish();

                // Show a toast message indicating app reset
                Toast.makeText(device_loc.this, "App reset", Toast.LENGTH_SHORT).show();
            }
        });

        resetbtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Handle button selection (focus) change
                if (hasFocus) {
                    // Button is selected (focused)
                    resetbtn.setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    // Button is deselected (unfocused)
                    resetbtn.setTextColor(getResources().getColor(android.R.color.black));
                }
            }
        });







       // }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Auth_ACTIVITY: //表示由authActivity返回
                if(resultCode == RESULT_OK && padIp != null){
                    String result=data.getStringExtra("result");
                    if(result.equals("true")){
                        panelList= new int[]{0, 0, 0};
                        panelList[panel_temp_num]=1;
                        panel_temp.setImageResource(R.drawable.panel_green);


                    }

                } else if(resultCode == RESULT_OK && dvID != null){
                    String result=data.getStringExtra("result");
                    if(result.equals("true")){
                        lightList= new int[]{0, 0, 0};

                        lightList[light_temp_num]=1;
                        light_temp.setImageResource(R.drawable.light_green);

                    }
//                    light_temp.setImageResource(R.drawable.light_green);
                }
                break;


//            case Auth_Light_ACTIVITY2: //表示由authActivity2返回
//                if(resultCode == RESULT_OK){
//                    String result=data.getStringExtra("result");
//                    if(result.equals("true")){
//                        light_temp.setImageResource(R.drawable.light_green);
//                    }
//
//                }
//                break;

        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the NanoHTTPD server when MainActivity is destroyed
        MyNanoHTTPD nanoHTTPD = new MyNanoHTTPD();
        nanoHTTPD.stopServer();
    }





    // Method to save the image state when needed
    // Method to save the image state







}

//    private void onDeviceAdd(Intent intent) {
//        //获取广播内容
//        String msg=intent.getStringExtra("msg");
//        String device_id=intent.getStringExtra("device_id");
//        String nickName=intent.getStringExtra("nickName");
//        String uuid=intent.getStringExtra("uuid");
//
//        Log.i(TAG, "panal message：" + msg);
//
//        Log.v(TAG, "new device add, device_id:" + device_id + ",nickName:" + nickName + ",uuid:" + uuid);
//
//    }
//}