// VoicePayAidlInterface.aidl
package com.changhong.voicepay;

// Declare any non-default types here with import statements
import com.changhong.voicepay.NotifyCallBack;

interface VoicePayAidlInterface {
    //声纹支付接口
    void voicePay(String packageName,String appId,String orderInfo);
    //语音输入接口
    void voiceInput(String packageName,String scene,in byte[] voiceData,int voiceDataLen,String voiceInfo);
    //声纹查询接口
    void voiceQuery(String packageName,String appId);
    //声纹注册接口
    void voiceRegister(String packageName,String appId,String registerContent,int registerNum);
    //声纹注销接口
    void voiceCancel(String packageName,String appId);
    //绑卡接口
    void getCardUrl(String packageName,String appId);
    //查询卡接口
    void queryCard(String packageName,String appId);
    //解绑卡接口
    void unbindCard(String packageName,String appId);
    //声纹支付结束接口
    void voicePayDestroy(String packageName,String appId);

    //注册回调接口
    int registerCallBack(String packageName,NotifyCallBack cb);
    int unregisterCallBack(String packageName,NotifyCallBack cb);
}
