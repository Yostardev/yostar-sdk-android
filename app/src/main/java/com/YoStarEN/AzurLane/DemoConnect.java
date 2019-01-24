package com.YoStarEN.AzurLane;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.airisdk.sdkcall.AiriSDK;
import com.airisdk.sdkcall.AiriSDKConnect;
import com.airisdk.sdkcall.AiriSDKInstance;
import com.airisdk.sdkcall.ErrorEntity;
import com.airisdk.sdkcall.tools.entity.AiriLoginEntity;
import com.airisdk.sdkcall.tools.entity.Platform;

/**
 * @author binbin.ma@yo-star.com
 */
public class DemoConnect {

    private static DemoConnect instance ;
    public static synchronized DemoConnect getInstance(){
        if (instance == null){
            instance = new DemoConnect() ;
        }
        return instance ;
    }

    /**
     * Initialization result callback
     * @return
     */
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

    /**
     * Login result callback
     * @return
     */
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
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AiriSDKInstance.getInstance().SDKNewAccountLink(getReLinkResultCallback()) ;
                        }
                    });
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

    /**
     * Yostar gets the verification code result callback
     * @return
     */
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

    /**
     * Get trans code result callback
     * @return
     */
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


    /**
     * Logout result callback
     * @return
     */
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

    /**
     * Channel binding result callback
     * @return
     */
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

    /**
     * Release channel binding result callback
     * @return
     */
    public AiriSDKConnect.UnLinkResultCallback getUnLinkResultCallback(){
        return new AiriSDKConnect.UnLinkResultCallback() {
            @Override
            public void onSuccess(Platform platform, String socailName) {
                MainActivity.setResultTv("Unbind successfully:" + platform.name() + ",Corresponding user name：" + socailName  ) ;
            }

            @Override
            public void onFail(ErrorEntity error) {
                MainActivity.setResultTv("Unbind failed："+error.toString()) ;
            }
        } ;
    }

    /**
     * Override binding result callback
     * @return
     */
    public AiriSDKConnect.ReLinkResultCallback getReLinkResultCallback(){
        return new AiriSDKConnect.ReLinkResultCallback() {
            @Override
            public void onSuccess(Platform platform, String socailName, String accessToken) {
                MainActivity.setResultTv("Overwrite binding success:" + platform.name() + ", corresponding user name: " + socailName + ", updated AccessToken: " +accessToken) ;
            }

            @Override
            public void onFail(ErrorEntity error) {
                MainActivity.setResultTv("Overwrite binding failed:" +error.toString()) ;
            }
        } ;
    }

    /**
     * Birthday setting result callback
     * @return
     */
    public AiriSDKConnect.BirthSetResultCallback getBirthSetResultCallback(){
        return new AiriSDKConnect.BirthSetResultCallback() {
            @Override
            public void onSuccess() {
                MainActivity.setResultTv("Birthday setting is successful") ;
            }

            @Override
            public void onFail(ErrorEntity error) {
                MainActivity.setResultTv("Birthday setting failed:" +error.toString()) ;
            }
        } ;
    }

    /**
     * Payment result callback
     * @return
     */
    public AiriSDKConnect.PurchaseResultCallback getPurchaseResultCallback(){
        return new AiriSDKConnect.PurchaseResultCallback() {
            @Override
            public void onResult(String orderId, String extraData, ErrorEntity entity) {
                if (entity.CODE() == 0){
                    MainActivity.setResultTv("Payment success - Order ID: " + orderId + ", with additional parameters: " + extraData );
                }else{
                    MainActivity.setResultTv("Payment failed:" +entity.toString()) ;
                }
            }
        } ;
    }

    /**
     * System level sharing result callback
     * @return
     */
    public AiriSDKConnect.ShareResultCallback getShareResultCallback(){
        return new AiriSDKConnect.ShareResultCallback() {
            @Override
            public void onSuccess() {
                MainActivity.setResultTv("System-level sharing on the Android side, there is no share result callback, so except for special cases, the default is success.");
            }

            @Override
            public void onFail(ErrorEntity entity) {
                MainActivity.setResultTv("Android share failed:" + entity.MESSAGE());
            }
        } ;
    }
}
