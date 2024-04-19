package com.changhong.voiceprintregistration.pay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.changhong.voicepay.NotifyCallBack;
import com.changhong.voicepay.VoicePayAidlInterface;


import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by mac on 2019/5/13.
 */

public class PayTask {
    private static final String TAG = "PayTask";
    private Context mContext;
    private static String mAppId;
    private static String mPackageName;
    private VoicePayAidlInterface mService;
    private PayCallBack mPayCallBack;

    public PayTask(Context cxt, String appId, String packageName, PayCallBack payCallBack) {
        this.mAppId = appId;
        this.mPackageName = packageName;
        this.mContext = cxt;
        this.mPayCallBack = payCallBack;
        bindService();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mService = VoicePayAidlInterface.Stub.asInterface(service);
            Log.e(TAG, "onServiceConnected---mService1=" + mService);
            try {
                mService.registerCallBack(mPackageName, mNotifyCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                Log.e(TAG, "onServiceDisconnected---mService1 = " + mService);
                mService.unregisterCallBack(mPackageName, mNotifyCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mService = null;
            bindService();
        }
    };

    public boolean bindService() {
        Intent intent = new Intent();
        intent.setAction("com.changhong.voiceprintregistration.service.VoicePayAidlInterface");
        intent.setPackage("com.changhong.voiceprintregistration");
        mContext.startService(intent);
        boolean flag = mContext.bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
//        payCallBack.notifyPayCall("bindService", flag+"");
        Log.e(TAG, "bindService=" + flag);
        return flag;
    }

    private NotifyCallBack mNotifyCallBack = new NotifyCallBack.Stub() {

        @Override
        public void notifyPayCall(String code, String msg) throws RemoteException {
            mPayCallBack.notifyPayCall(code, msg);
            Log.e(TAG, "notifyPayCall---code:" + code + "，msg：" + msg);
        }
    };

    public void startPay() {
        Log.e(TAG, "startPay");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName componentName = new ComponentName("com.changhong.voiceprintregistration", "com.changhong.voiceprintregistration.VoicePayActivity");
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("", "");//这里Intent传值
        mContext.startActivity(intent);
    }

    public void unbindService() {
        if (mService == null) {
            return;
        }
        mContext.unbindService(mServiceConnection);
        mService = null;
    }

    public void VoiceInput(String scene, byte[] voiceData, int voiceDataLen, String voiceinfo) {
        try {
            Log.e(TAG, "VoiceInput--mService" + mService);
            if (mService == null) {
                return;
            }
            mService.voiceInput(mPackageName, scene, voiceData, voiceData.length, voiceinfo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void VoicePay(String orderInfo) {
        Log.e(TAG, "startVoicePay");
        try {
            Log.e(TAG, "VoicePay--mService2=" + mService);
            if (mService == null) {
                return;
            }
            mService.voicePay(mPackageName, mAppId, orderInfo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "start voicePayActivity");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName componentName = new ComponentName("com.changhong.voiceprintregistration", "com.changhong.voiceprintregistration.VoicePayActivity");
        intent.setComponent(componentName);
        intent.putExtra("", "");//这里Intent传值
        mContext.startActivity(intent);
    }

//    public void VoicePayDestroy() {
//        try {
//            Log.e(TAG, "voicePayDestroy--mService2=" + mService);
//            if (mService == null) {
//                return;
//            }
//            mService.voicePayDestroy(mPackageName, mAppId);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }

    public void VoiceCancel() {
        try {
            Log.e(TAG, "VoiceCancel--mService2=" + mService);
            if (mService == null) {
                return;
            }
            mService.voiceCancel(mPackageName, mAppId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void voiceQuery() {
        try {
            if (mService == null) {
                return;
            }
            mService.voiceQuery(mPackageName, mAppId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void voiceRegister(String registerConten, int registerNum) {
        try {
            Log.e(TAG, "voiceRegister--mService2=" + mService);
            if (mService == null) {
                return;
            }
            mService.voiceRegister(mPackageName, mAppId, registerConten, registerNum);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getCardUrl() {
        try {
            Log.e(TAG, "getCardUrl--mService2=" + mService);
            if (mService == null) {
                return;
            }
            mService.getCardUrl(mPackageName, mAppId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void queryCard() {
        try {
            Log.e(TAG, "queryCard--mService2=" + mService);
            if (mService == null) {
                return;
            }
            mService.queryCard(mPackageName, mAppId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unbindCard() {
        try {
            Log.e(TAG, "unbindCard--mService2=" + mService);
            if (mService == null) {
                return;
            }
            mService.unbindCard(mPackageName, mAppId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
