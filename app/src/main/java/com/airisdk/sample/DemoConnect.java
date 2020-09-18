package com.airisdk.sample;

import android.view.View;

import com.airisdk.sdkcall.AiriSDK;
import com.airisdk.sdkcall.AiriSDKConnect;
import com.airisdk.sdkcall.tools.entity.AiriSDKCommon;
import com.airisdk.sdkcall.tools.entity.SDKSkudetails;
import com.airisdk.sdkcall.tools.utils.AiriSDKUtils;
import com.airisdk.sdkcall.tools.utils.LogUtil;
import com.airisdk.sdkcall.tools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Date:2020/3/17
 * Time:16:05
 * author:mabinbin
 */
public class DemoConnect {

    private static AiriSDKConnect.SDKInfoCallback callback;

    public static String[] offer_ids = null ;

    public static AiriSDKConnect.SDKInfoCallback getCallback() {
        if (callback == null) {
            callback = new AiriSDKConnect.SDKInfoCallback() {
                @Override
                public void onSuccess(String message) {
                    ResultBean resultBean = AiriSDKUtils.getInstance().getGs().fromJson(message, ResultBean.class);
                    if (resultBean.getR_CODE() == AiriSDKCommon.SUCCESS) {
                        ToastUtils.showLong("操作成功");
                        if (resultBean.getMETHOD().equals(AiriSDKCommon.INIT_CALLBACK)) {
                            WelcomeActivity.showlayout(ResourceUtils.getId(WelcomeActivity.instance,"accountLayout"));
                        }
                        if (resultBean.getMETHOD().equals(AiriSDKCommon.LOGIN_CALLBACK)) {
                            WelcomeActivity.showlayout(ResourceUtils.getId(WelcomeActivity.instance,"accountCenter"));
                        }
                    }else{
                        ToastUtils.showLong("操作失败:" + resultBean.getR_CODE() + "," + AiriSDK.SDKGetErrorCode(resultBean.getR_CODE()));
                    }
                    if (resultBean.getMETHOD().equals(AiriSDKCommon.GOOGLE_PAY_CALLBACK)){
                        if (resultBean.getR_CODE() == AiriSDKCommon.ERROR_NEED_CONFIRM_AGREMENT){
                            String[] edits = {} ;
                            String[] btns = {"获取协议","确认协议"} ;
                            WelcomeActivity.instance.inputLayout = new InputLayout(WelcomeActivity.instance,edits,btns) ;
                            View.OnClickListener[] onClickListeners = {
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AiriSDK.SDKGetUnderAgeAgrement();
                                        }
                                    },
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AiriSDK.SDKConfrimUnder();
                                            WelcomeActivity.instance.inputLayout.dismiss();
                                        }
                                    }
                            } ;
                            WelcomeActivity.instance.inputLayout.setCancelable(false);
                            WelcomeActivity.instance.inputLayout.setBtnClick(onClickListeners);
                            WelcomeActivity.instance.inputLayout.show();
                        }
                    }

                    if (resultBean.getMETHOD().equals(AiriSDKCommon.CALLBACK_AMAZON)){
                        offer_ids = resultBean.getOffer_ids() ;
                        LogUtil.e("Prime:" + offer_ids[0]);
                    }

                    WelcomeActivity.setResultInfo(resultBean.getMETHOD() + ":" + resultBean.getR_CODE() + ":" + resultBean.getR_MSG());
                    WelcomeActivity.setResultInfo(message);
                }
            };
        }
        return callback;
    }
}
