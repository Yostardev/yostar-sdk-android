package com.YoStarEN.AzurLane;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.airisdk.sdkcall.AiriSDK;
import com.airisdk.sdkcall.AiriSDKConnect;
import com.airisdk.sdkcall.AiriSDKInstance;
import com.airisdk.sdkcall.ErrorEntity;
import com.airisdk.sdkcall.tools.entity.AiriLoginEntity;
import com.airisdk.sdkcall.tools.entity.Platform;

public class DemoConnect {

    private static DemoConnect instance ;
    public static synchronized DemoConnect getInstance(){
        if (instance == null){
            instance = new DemoConnect() ;
        }
        return instance ;
    }

    public AiriSDKConnect.InitResultCallback getInitResultCallback(){
        return new AiriSDKConnect.InitResultCallback() {
            @Override
            public void onSuccess(boolean isVirtual) {
                MainActivity.showLoginLayout();
                MainActivity.setResultTv("Initialization successful. Whether the current machine is an emulator:" + isVirtual) ;
            }

            @Override
            public void onFail(ErrorEntity entity) {
                MainActivity.showInitLayout();
                MainActivity.setResultTv("Initialization failed" ) ;
            }
        } ;
    }

    public AiriSDKConnect.LoginResultCallback getLoginResultCallback(){
        return new AiriSDKConnect.LoginResultCallback() {
            @Override
            public void onSuccess(AiriLoginEntity entity) {
                MainActivity.showSettingLayout();
                MainActivity.setResultTv("Login successfully." + entity.toString());
                if (entity.isCanBindGuest()){//Whether the landing account can be bound to the local tourist account.
                    AlertDialog.Builder builder = new AlertDialog.Builder(AiriSDK.instance);
                    builder.setTitle("Prompt:");
                    builder.setMessage("Whether to bind?");
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    //点击对话框以外的区域是否让对话框消失
                    builder.setCancelable(true);
                    //设置正面按钮
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AiriSDKInstance.getInstance().SDKNewAccountLink(getReLinkResultCallback()) ;
                        }
                    });
                    //设置反面按钮
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

            @Override
            public void onFail(ErrorEntity entity) {
                MainActivity.showLoginLayout();
                MainActivity.setResultTv("Login failed：" + entity.toString());
            }
        } ;
    }

    public AiriSDKConnect.CodeReqResultCallback getCodeReqResultCallback(){
        return new AiriSDKConnect.CodeReqResultCallback() {
            @Override
            public void onSuccess() {
                MainActivity.setResultTv("Get the verification code successfully.") ;
            }

            @Override
            public void onFail(ErrorEntity entity) {
                MainActivity.setResultTv("Failed to get verification code："+entity.toString()) ;
            }
        } ;
    }

    public AiriSDKConnect.TranscodeResultCallback getTranscodeResultCallback(){
        return new AiriSDKConnect.TranscodeResultCallback() {
            @Override
            public void onSuccess(String transcode, String transUid) {
                MainActivity.setResultTv("Get the transcode successfully:"+transcode + ",Corresponding UID：" + transUid) ;
            }

            @Override
            public void onFail(ErrorEntity error) {
                MainActivity.setResultTv("Get the transcode failed："+error.toString()) ;
            }
        } ;
    }



    public AiriSDKConnect.LogoutCallback getLogoutCallback(){
        return new AiriSDKConnect.LogoutCallback() {
            @Override
            public void onSuccess() {
                MainActivity.showInitLayout();
                MainActivity.setResultTv("Logout successful, please re-initialize the program." ) ;
            }

            @Override
            public void onFail(ErrorEntity entity) {
                MainActivity.setResultTv("Logout failed："+entity.toString()) ;
                MainActivity.showSettingLayout();
            }
        } ;
    }

    public AiriSDKConnect.LinkResultCallback getLinkResultCallback(){
        return new AiriSDKConnect.LinkResultCallback() {
            @Override
            public void onSuccess(Platform platform, String socailName) {
                MainActivity.setResultTv("Binding success:" + platform.name() + ",Corresponding user name：" + socailName ) ;
            }

            @Override
            public void onFail(ErrorEntity error) {
                MainActivity.setResultTv("Binding failed："+error.toString()) ;
            }
        } ;
    }

    public AiriSDKConnect.UnLinkResultCallback getUnLinkResultCallback(){
        return new AiriSDKConnect.UnLinkResultCallback() {
            @Override
            public void onSuccess(Platform platform, String socailName) {
                MainActivity.setResultTv("Unbind successfully:" + platform.name() + ",Corresponding user name：" + socailName  ) ;
            }

            @Override
            public void onFail(ErrorEntity error) {
                MainActivity.setResultTv("解除绑定失败："+error.toString()) ;
            }
        } ;
    }

    public AiriSDKConnect.ReLinkResultCallback getReLinkResultCallback(){
        return new AiriSDKConnect.ReLinkResultCallback() {
            @Override
            public void onSuccess(Platform platform, String socailName, String accessToken) {
                MainActivity.setResultTv("覆盖绑定成功:" + platform.name() + ",对应的用户名为：" + socailName + ",更新的AccessToken:" +accessToken ) ;
            }

            @Override
            public void onFail(ErrorEntity error) {
                MainActivity.setResultTv("覆盖绑定失败："+error.toString()) ;
            }
        } ;
    }

    public AiriSDKConnect.BirthSetResultCallback getBirthSetResultCallback(){
        return new AiriSDKConnect.BirthSetResultCallback() {
            @Override
            public void onSuccess() {
                MainActivity.setResultTv("生日设置成功") ;
            }

            @Override
            public void onFail(ErrorEntity error) {
                MainActivity.setResultTv("生日设置失败："+error.toString()) ;
            }
        } ;
    }

    public AiriSDKConnect.PurchaseResultCallback getPurchaseResultCallback(){
        return new AiriSDKConnect.PurchaseResultCallback() {
            @Override
            public void onResult(String orderId, String extraData, ErrorEntity entity) {
                if (entity.CODE() == 0){
                    MainActivity.setResultTv("支付成功 - 订单ID:" + orderId + ", 附加参数：" + extraData );
                }else{
                    MainActivity.setResultTv("支付失败："+entity.toString()) ;
                }
            }
        } ;
    }

    public AiriSDKConnect.ShareResultCallback getShareResultCallback(){
        return new AiriSDKConnect.ShareResultCallback() {
            @Override
            public void onSuccess() {
                MainActivity.setResultTv("Android 端的系统级分享，没有分享结果回调，所以除特殊情况，默认为成功");
            }

            @Override
            public void onFail(ErrorEntity entity) {
                MainActivity.setResultTv("Android 分享失败：" + entity.MESSAGE());
            }
        } ;
    }
}
