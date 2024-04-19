package com.example.shopdemo.mufis;

import static com.example.shopdemo.mufis.MufisLinkin.ACTION_ASK_RESULT;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_DEVICE_ADD;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_DEVICE_CHISID_ACCESS;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_SmartPad_ADD;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_VOICE_COMMAND;
import static com.example.shopdemo.mufis.MufisLinkin.ACTION_DEVICE_UPDATE;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.shopdemo.device_loc;

public class RegisterService extends Service {

    /** 广播消息处理 */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action)
            {
                case ACTION_DEVICE_CHISID_ACCESS:
                    //TODO::添加对CHIS ID 的设备身份认证事件的UI响应

                    break;
                case ACTION_DEVICE_ADD:
                    onDeviceAdd(intent);
                    //onDeviceConnectedIn(intent);
                    break;
                case ACTION_VOICE_COMMAND:
                    onVoiceCommand(intent);
                    break;
                case ACTION_DEVICE_UPDATE:
//                    onDeviceUpdatedIn(intent);
                    break;
                case ACTION_SmartPad_ADD:
                    //添加对面板响应的UI事件触发
                    onPadAdd(intent);
                    break;
                case ACTION_ASK_RESULT:
                    break;
                default:
                    break;
            }
        }
    };

    private void onDeviceUpdatedIn(Intent intent) {
        //获取广播内容
//        String msglog=intent.getStringExtra("msg");
        String deviceID=intent.getStringExtra("access");
        String running_state=intent.getStringExtra("id");
        String message=intent.getStringExtra("data");

        //String showMessage = String.format("设备ID[%s,%s]接入", deviceID, running_state);
//            Toast.makeText(RegisterService.this, showMessage, Toast.LENGTH_LONG).show();
        startActivity(new Intent(RegisterService.this, device_loc.class).putExtra("deviceIDsend",deviceID).putExtra("runningstate",running_state).putExtra("msglog",message));


        //Log.i("test","panal message：" + msg);
    }


    private void onDeviceAdd(Intent intent) {
        //获取广播内容
        String nickname=intent.getStringExtra("nickNamex");
        String deviceID=intent.getStringExtra("device_id");
        String devitype=intent.getStringExtra("devtype");

        //String uuid=intent.getStringExtra("uuid");

        String showMessage = String.format("在本地发现新的设备加入 Nickname:[%s] @ [Device ID: %s] : [Device type: %s]", nickname ,deviceID, devitype);
//            Toast.makeText(RegisterService.this, showMessage, Toast.LENGTH_LONG).show();
        startActivity(new Intent(RegisterService.this, device_loc.class).putExtra("deviceIDx",deviceID).putExtra("nickx",nickname).putExtra("devitypex",devitype));


        //Log.i("test","panal message：" + msg);
    }

    private void onPadAdd(Intent intent) {
        //获取广播内容
        String pad_nickname=intent.getStringExtra("nickName");
        String pad_ip=intent.getStringExtra("ip");
        String pad_module=intent.getStringExtra("module");
        //String uuid=intent.getStringExtra("uuid");

        //String showMessage = String.format("发现新的面板接入! Pad_nickname: [%s]: %s (from %s, ip: %s)", pad_nickname ,pad_ip, pad_module);
//            Toast.makeText(RegisterService.this, showMessage, Toast.LENGTH_LONG).show();
        startActivity(new Intent(RegisterService.this, device_loc.class).putExtra("padname",pad_nickname).putExtra("padip",pad_ip).putExtra("padmodule",pad_module));


        //Log.i("test","panal message：" + msg);
    }

    private void onVoiceCommand(Intent intent) {
        //获取广播内容
        String product = intent.getStringExtra("product");
        String counts = intent.getStringExtra("counts");
        Log.v("chislab::Device_Loc_Activity", "onVoiceCommand Voice received, \n product:" + product + "\ncounts:" + counts);
//        intent.setAction(ACTION_VOICE_COMMAND);


        //String showMessage = String.format("发现新的面板接入! Pad_nickname: [%s]: %s (from %s, ip: %s)", pad_nickname ,pad_ip, pad_module);
//            Toast.makeText(RegisterService.this, showMessage, Toast.LENGTH_LONG).show();
        startActivity(new Intent(RegisterService.this, device_loc.class).putExtra("product",product).putExtra("counts",counts));


        //Log.i("test","panal message：" + msg);
    }

//    private void onSmartpadConnectedIn(Intent intent) {
//        //获取广播内容
//        String msg=intent.getStringExtra("msg");
//        String nickName=intent.getStringExtra("nickName");
//        if(StringUtils.isNotValidStr(nickName))
//            nickName = "未命名面板";
//        Bundle extra = intent.getExtras();
//        Object ext_param = extra.get("param");
//        JSONObject param = null;
//        if(ext_param instanceof JSONObject) {
//               param = (JSONObject) ext_param;
//        }
//        String ip = param==null?"未知地址":param.getString("ip");
//        String sn=intent.getStringExtra("sn");
//        if(StringUtils.isValidStr(msg)) {
//            String showMessage = String.format("发现面板[%s(%s,%s)]接入", nickName, sn, ip);
////            Toast.makeText(RegisterService.this, showMessage, Toast.LENGTH_LONG).show();
//            startActivity(new Intent(RegisterService.this, device_loc.class).putExtra("nickname",nickName).putExtra("sen",sn).putExtra("ipadd",ip));
//        }
//        Log.i("test","panal message：" + msg);
//    }

    public RegisterService() {

    }
    @Override
    public void onCreate() {
        super.onCreate();
        //1.创建广播接收者对象
        //2.创建intent-filter对象
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ACTION_DEVICE_UPDATE);
        intentFilter.addAction(ACTION_DEVICE_ADD);
        intentFilter.addAction(ACTION_SmartPad_ADD);
        intentFilter.addAction(ACTION_VOICE_COMMAND);
        registerReceiver(receiver, intentFilter);


        //startActivity(new Intent(RegisterService.this, ClickedItemActivity.class).putExtra("nickname",nickName).putExtra("sen",sn).putExtra("ipadd",ip));

        MufisLinkin.initApp(RegisterService.this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除注册
        unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}