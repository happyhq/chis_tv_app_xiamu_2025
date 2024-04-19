package com.example.shopdemo.mufis;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.changhong.jedge.MufisJ;
import com.changhong.jedge.MufisMessageService;
import com.changhong.jedge.ap.ActivePage;
import com.changhong.qlib.QData;
import com.changhong.qlib.intf.QIData;
import com.example.shopdemo.AppSettings;
import com.example.shopdemo.lightselectsettings;
import com.example.shopdemo.MainActivity;

public class MufisLinkin {
    public static final String ACTION_VOICE_COMMAND="com.changhong.iotbuy.buy";
    public static final String ACTION_SmartPad_ADD="com.changhong.iotdevice.padIn";
    public static final String ACTION_DEVICE_ADD="com.changhong.iotdevice.deviceIn";
    public static final String ACTION_DEVICE_UPDATE="com.changhong.iotdevice.deviceStatus";
    public static final String ACTION_ASK_RESULT="com.changhong.iotdevice.askResult";
    public static final String ACTION_DEVICE_CHISID_ACCESS = "com.changhong.iotdevice.chisIdAccess";

    static String ip="192.168.68.15";

    public static String ip_address="";
    public static String lightmac_address="";

    public static void initialize() {
        // Access the AppSettings instance to retrieve ipAddress
        String ipAddress = AppSettings.getInstance().getIpAddress();

        // Use ipAddress in MufisLinkin initialization
        // Assign ipAddress to the desired variable (e.g., ip_address)
        ip_address = ipAddress;
        Log.v("chislab::MufisLinkin", "Address, \n IP:" + ip_address);
    }

    public static void initializelight() {
        // Access the AppSettings instance to retrieve ipAddress
        String lightmac = lightselectsettings.getInstance().getlightmacAddress();

        // Use ipAddress in MufisLinkin initialization
        // Assign ipAddress to the desired variable (e.g., ip_address)
        lightmac_address = lightmac;
        Log.v("chislab::MufisLinkin", "Light Address, \n IP:" + lightmac_address);


    }



    /****************************************************************************
     * 注意在运行设备的相同局域网内运行 mgs-cygwin.exe 服务程序
     * 在虚拟机中运行本模块将无法自动发现面板,需要手动指定mgs-cygwin.exe所在PC的IP地址
     */


    private static MufisJ glMufis = MufisJ.getInstance();
    private static ActivePage glPage = ActivePage.getInstance("IotBuyDemo");

    //  private static final String test_mgbus_node_ip_ = "192.168.1.6";
//    private static MufisJ glMufis = MufisJ.getInstance(test_mgbus_node_ip_);
//    private static ActivePage glPage = ActivePage.getInstance("IotBuyDemo",test_mgbus_node_ip_);
    private static Context mContext;

    public static void initApp(Context context) {
        mContext = context;
        glPage.registerVoiceCommand("buyProduct", new ActivePage.VoiceCommandHandler() {
            @Override
            public void onVoiceCommand(String cmd, QIData param) {
                String log = String.format("购买一个设备的动作,在这里发送Broadcast调起其它的Activity : %s -> %s ", cmd, param.toJSONString(true));
//                Toast.makeText(mContext, log, Toast.LENGTH_SHORT ).show();
                glPage.printClientLog(log);

                String product = param.getString("product");
                String counts = param.getString("counts", "1");
                Intent intent = new Intent();
                intent.setAction(ACTION_VOICE_COMMAND);
                intent.putExtra("product",product);
                intent.putExtra("counts",counts);
                Log.v("chislab::Device_Loc_Activity", "onVoiceCommand2222 Voice received, \n product:" + product + "\ncounts:" + counts);
                //发送无序广播
                mContext.sendBroadcast(intent);
            }
        });

        if(!glPage.start()) {
            glPage.errLog("Fail to start ap app : " + glPage.getName());
            return;
        }

        glMufis.setOnNewMgbusNodeConnected(new MufisJ.OnNewMgbusNodeConnectedInHandler() {
            @Override
            public void onMgbusNodeConnectedIn(String module, String mac, String nickName, String sn, String ip, QIData msg) {
                String log = String.format("发现新的面板接入! [%s]: %s (from %s, ip: %s)", nickName, module, sn, ip);
//                Toast.makeText(mContext, log, Toast.LENGTH_SHORT ).show();

                glMufis.printClientLog(log);

                Intent intent = new Intent();
                intent.setAction(ACTION_SmartPad_ADD);
                intent.putExtra("msg",log);
                intent.putExtra("nickName",nickName);
                intent.putExtra("param", msg.asJsonObject());
                intent.putExtra("ip", ip);
                intent.putExtra("module", module);
                intent.putExtra("sn", sn);
                mContext.sendBroadcast(intent);
            }

            @Override
            public void onMgbusNodeOffline(String mgnet, String module, QIData msg) {
                glMufis.markLog("面板掉线!  : " + module);
            }
        });


        glMufis.setOnDeviceChangedHandler(new MufisMessageService.OnDeviceChangedEventHandler() {
            @Override
            public void onDeviceAdd(String device_id, String type, String uuid, String loc, String nickName) {
                String log = String.format("在本地发现新的设备加入 [%s(%s,%s)] @ [%s] : [uuid: %s]", nickName, device_id, type,loc,  uuid);
//                Toast.makeText(mContext, log, Toast.LENGTH_SHORT ).show();

                glMufis.printClientLog(log);
                if(type.equals("TVUIController"))
                    return;

                Intent intent = new Intent();
                intent.setAction(ACTION_DEVICE_ADD);
                intent.putExtra("msg",log);
                intent.putExtra("device_id",device_id);
                intent.putExtra("nickNamex",nickName);
                intent.putExtra("devtype", type);
                intent.putExtra("uuid", uuid);
                mContext.sendBroadcast(intent);


            }

            @Override
            public void onDeviceRemoved(String device_id) {
                glMufis.printClientLog("设备被删除了 : " + device_id);
            }
        });

        glMufis.setDeviceStatusUpdated(new MufisMessageService.OnDeviceStatusUpdatedHandler() {
            @Override
            public void onDeviceStatusUpdated(String devId, String running_state, boolean isLocal, QIData params) {
                String log = String.format("设备 [%s] 的状态发生了变化 : [%s] , data : %s", devId, running_state, params.toJSONString(true));
//                Toast.makeText(mContext, log, Toast.LENGTH_SHORT ).show();
//                params.getInteger()
                Intent intent = new Intent();
                intent.setAction(ACTION_DEVICE_UPDATE);
                intent.putExtra("access",devId);
                intent.putExtra("id",running_state);
                intent.putExtra("data", params.toJSONString(true));
                mContext.sendBroadcast(intent);


                //context.startActivity(new Intent(MufisLinkin.this, .class).putExtra("nickname",nickName).putExtra("sen",sn).putExtra("ipadd",ip));
                glMufis.printClientLog(log);
            }
        });


        glMufis.setChisIdVerifyResultHandler((method, macAddr, addr, result) -> {
            String log = String.format("本地ChisId验证事件: 对方ID%s, 方法: %s ,来自 %s ,结果:%s ",  macAddr, method, addr,  result?"true":"false");
//            Toast.makeText(mContext, log, Toast.LENGTH_SHORT ).show();
            glMufis.printClientLog(log);

            Intent intent = new Intent();
            intent.setAction(ACTION_DEVICE_CHISID_ACCESS);
            intent.putExtra("msg", log);
            intent.putExtra("access",method);
            intent.putExtra("id",macAddr);
            intent.putExtra("addr", addr);
            intent.putExtra("result", result);
            mContext.sendBroadcast(intent);
        });

        if(!glMufis.start()) {
            glMufis.errLog("Fail to init mufis module");
        }
    }

    public static void clearApp() {
        glPage.shutdown();
        glMufis.shutdown();
    }
    public static void switchOnLight() {
        Log.v("chislab::MufisLinkin", "outLight Address, \n IP:" + lightmac_address);
        glMufis.controlDevice(lightmac_address,"turnOn", new QData());
    }

    public static void switchOffLight() {
        Log.v("chislab::MufisLinkin", "outLight Address, \n IP:" + lightmac_address);
        glMufis.controlDevice(lightmac_address,"turnOff", new QData());
    }

    //longchord: D2 84 7A 69 95 00
    //light 2 not normal: AB CA 1D 97 1B 30

    public static void askForResult(String text, ActivePage.AskResultHandler handler) {
        glPage.askForResultAndroid(text, handler);
    }

    public static void speak(String text) {
        glPage.speakText(text);
    }
}
