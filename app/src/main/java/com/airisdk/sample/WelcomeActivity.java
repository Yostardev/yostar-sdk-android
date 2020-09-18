package com.airisdk.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.YostarENAmazon.AzurLane.R;
import com.airisdk.sdkcall.AiriSDK;
import com.airisdk.sdkcall.tools.entity.AiriSDKCommon;
import com.airisdk.sdkcall.tools.entity.Platform;
import com.airisdk.sdkcall.tools.plugin.LoginEvents.AmazonPlugin;
import com.airisdk.sdkcall.tools.plugin.LoginEvents.AmazonStorePlugin;
import com.airisdk.sdkcall.tools.utils.AiriErrorUtils;
import com.airisdk.sdkcall.tools.utils.AiriSDKUtils;
import com.airisdk.sdkcall.tools.utils.LogUtil;
import com.airisdk.sdkcall.tools.utils.ToastUtils;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class WelcomeActivity extends AppCompatActivity {
    private static LinearLayout initLayout, loginLayout, accountCenter, aboutLayout, otherLayout, payLayout;
    private Button initSDK;
    private Button quickLogin, deviceLogin, yostarLogin, inheritanceLogin, facebookLogin, twitterLogin, googleLogin, googleplayLogin, amzonLogin, appleLogin, resetLogin;
    private Button adjustButton,queryConfigBtn;
    private Button payBtn, aboutBtn, otherBtn, agreementBtn, errorBtn, backLoginBtn;
    private Button linkFB, unLinkFB, linkAmazon, unlinkAmazon, linkTW, unLinkTW, linkYostar, unLinkYostar, linkGoogle, unLinkGoogle, linkGooglePlay, unLinkGooglePlay, delAccount, resetAccount, transCodeReq;
    private Button customServiceBtn,birthSetBtn, statisticBtn, shareBtn, scoreBtn, backCenterBtn2;
    public InputLayout inputLayout;
    private static TextView info;
    private String requestSDK;
    public static WelcomeActivity instance;

    private static int showiD;

    private LinearLayout welcomLayout;


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("onRestart" + showiD);
        showlayout(showiD);
    }

    private static int getId(String name) {
        return ResourceUtils.getId(instance, name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtils.getLayoutId(this, "activity_welcome"));
        instance = this;
        welcomLayout = findViewById(R.id.welcomLayout);
        AiriSDK.instance = this;
        initView();
        requestSDK = SampleUtils.getMetaDataOfApplicaiton(this, "BASE_URL");
        info.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (TextUtils.isEmpty(requestSDK)) {
            SampleUtils.showTips("sdk请求地址没有配置.");
            info.setText("sdk请求没有地址\n\n");
            dissmissLayout();
        } else {
            info.setText("地址：" + requestSDK + "\n\n");
            showlayout(ResourceUtils.getId(this, "initLayout"));
        }

        onClickMethod();
        accountClick();
        othesClick();

        findViewById(ResourceUtils.getId(this, "backCenterBtn3")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlayout(getId("accountCenter"));
            }
        });

        findViewById(getId("uidWithTokenLogin")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] edits = {"登陆UID", "登陆Token"};
                String[] btns = {"登陆"};

                inputLayout = new InputLayout(instance, edits, btns);
                inputLayout.setCancelable(false);
                View.OnClickListener[] onClickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uid = inputLayout.edit_1.getText().toString();
                                String token = inputLayout.edit_2.getText().toString();
                                if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(token)) {
                                    ToastUtils.showLong("参数不能为空");
                                } else {
                                    AiriSDK.updateUnityToken(token, uid);
                                    ToastUtils.showLong("参数更新完成，点击 '快速登陆' 登陆到" + uid);
                                }
                                inputLayout.dismiss();
                            }
                        }
                };
                inputLayout.setBtnClick(onClickListeners);
                inputLayout.show();
            }
        });
    }

    public static void dissmissLayout() {
        initLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.GONE);
        accountCenter.setVisibility(View.GONE);
        aboutLayout.setVisibility(View.GONE);
        otherLayout.setVisibility(View.GONE);
        payLayout.setVisibility(View.GONE);
    }

    public static void showlayout(int layoutID) {
        dissmissLayout();

        showiD = layoutID;
        if (layoutID == getId("initLayout")) {
            initLayout.setVisibility(View.VISIBLE);
        }

        if (layoutID == getId("accountLayout")) {
            loginLayout.setVisibility(View.VISIBLE);
        }

        if (layoutID == getId("accountCenter")) {
            accountCenter.setVisibility(View.VISIBLE);
        }

        if (layoutID == getId("aboutLayout")) {
            aboutLayout.setVisibility(View.VISIBLE);
        }
        if (layoutID == getId("otherLayout")) {
            otherLayout.setVisibility(View.VISIBLE);
        }
        if (layoutID == getId("payLayout")) {
            payLayout.setVisibility(View.VISIBLE);
        }
    }

    public static void setResultInfo(String... message) {
        Message mmm = new Message();
        mmm.what = 1;
        mmm.obj = Arrays.toString(message);
        mHandler.sendMessage(mmm);
    }


    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                info.setText(info.getText() + "\n" + msg.obj.toString());
            }
        }
    };

    public void initView() {
        initLayout = findViewById(getId("initLayout"));
        loginLayout = findViewById(getId("accountLayout"));
        accountCenter = findViewById(getId("accountCenter"));
        aboutLayout = findViewById(getId("aboutLayout"));
        otherLayout = findViewById(getId("otherLayout"));
        payLayout = findViewById(getId("payLayout"));
        birthSetBtn = findViewById(getId("birthSetBtn")) ;

        initSDK = findViewById(getId("initSDK"));
        info = findViewById(getId("info"));
        quickLogin = findViewById(getId("quickLogin"));
        deviceLogin = findViewById(getId("deviceLogin"));
        yostarLogin = findViewById(getId("yostarLogin"));
        inheritanceLogin = findViewById(getId("inheritanceLogin"));
        facebookLogin = findViewById(getId("facebookLogin"));
        twitterLogin = findViewById(getId("twitterLogin"));
        amzonLogin = findViewById(getId("amzonLogin"));
        googleLogin = findViewById(getId("googleLogin"));
        googleplayLogin = findViewById(getId("googleplayLogin"));
        appleLogin = findViewById(getId("appleLogin"));
        resetLogin = findViewById(getId("resetLogin"));

        adjustButton = findViewById(getId("adjustEvent"));
        queryConfigBtn = findViewById(getId("query_remote_config_btn"));


        payBtn = findViewById(getId("payBtn"));
        aboutBtn = findViewById(getId("aboutBtn"));
        otherBtn = findViewById(getId("otherBtn"));
        agreementBtn = findViewById(getId("agreementBtn"));
        errorBtn = findViewById(getId("errorBtn"));
        backLoginBtn = findViewById(getId("backLoginBtn"));

        linkFB = findViewById(getId("linkFB"));
        unLinkFB = findViewById(getId("unlinkFB"));
        linkTW = findViewById(getId("linkTW"));
        unLinkTW = findViewById(getId("unlinkTW"));
        linkYostar = findViewById(getId("linkYostar"));
        unLinkYostar = findViewById(getId("unlinkYostar"));
        linkGoogle = findViewById(getId("linkGoogle"));
        unLinkGoogle = findViewById(getId("unlinkGoogle"));
        linkGooglePlay = findViewById(getId("linkGooglePlay"));
        unLinkGooglePlay = findViewById(getId("unlinkGooglePlay"));
        delAccount = findViewById(getId("deleAccountBtn"));
        resetAccount = findViewById(getId("resetAccount"));
        transCodeReq = findViewById(getId("transCodeReq"));
        linkAmazon = findViewById(getId("linkAmazon"));
        unlinkAmazon = findViewById(getId("unlinkAmazon"));

//        customServiceBtn, statisticBtn ,shareBtn,scoreBtn,backCenterBtn2
        customServiceBtn = findViewById(getId("customServiceBtn"));
        statisticBtn = findViewById(getId("statisticBtn"));
        shareBtn = findViewById(getId("shareBtn"));
        scoreBtn = findViewById(getId("scoreBtn"));
        backCenterBtn2 = findViewById(getId("backCenterBtn2"));

        findViewById(getId("amazonPrimeBtn")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DemoConnect.offer_ids == null) {
                    setResultInfo("没有活动商品呢.");
                    return;
                }
                String[] strings = DemoConnect.offer_ids;
                String[] edits = {};
                inputLayout = new InputLayout(WelcomeActivity.this, edits, strings);
                View.OnClickListener[] clickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AmazonStorePlugin.getInstance().init(WelcomeActivity.this);
                                AmazonStorePlugin.getInstance().prime();
                                inputLayout.dismiss();
                            }
                        }
                };

                inputLayout.setBtnClick(clickListeners);
                inputLayout.setCancelable(false);
                inputLayout.show();
            }
        });

        findViewById(R.id.GetAmazonPrime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DemoConnect.offer_ids == null) {
                    setResultInfo("没有活动商品呢.");
                    return;
                }
                final String[] strings = DemoConnect.offer_ids;
                String[] edits = {};

                inputLayout = new InputLayout(WelcomeActivity.this, edits, strings);
                View.OnClickListener[] clickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AmazonStorePlugin.getInstance().notify(strings[0]);
                                inputLayout.dismiss();
                            }
                        }
                };

                inputLayout.setBtnClick(clickListeners);
                inputLayout.setCancelable(false);
                inputLayout.show();
            }
        });

        findViewById(getId("primeGet")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date after = df.parse("2020-04-21 08:00:00");
                    AiriSDK.SDKGetPrime(after.getTime() + "");
                } catch (ParseException e) {
                    LogUtil.e("e:" + e.getMessage());
                }
            }
        });

        findViewById(R.id.goPayBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, BuyActivity.class));
            }
        });


        findViewById(getId("skuDetails")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> skuList = new ArrayList<>();
                skuList.add("com.yostar.arknights.monthlycard");
                skuList.add("com.yostar.arknights.starterfurniture");
                skuList.add("com.yostar.arknights.weeklyupgrade");
                String json = AiriSDKUtils.getInstance().getGs().toJson(skuList);
                AiriSDK.QuerySkuDetails(json);
            }
        });

        birthSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] edits = {"设置生日"} ;
                String[] btns = {"确认"} ;
                inputLayout = new InputLayout(WelcomeActivity.this,edits,btns) ;
                View.OnClickListener[] onClickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String birthDay = inputLayout.edit_1.getText().toString() ;
                                if (TextUtils.isEmpty(birthDay)){
                                    ToastUtils.showLong("参数不能为空"); ;
                                }else{
                                    AiriSDK.SDKSetBirth(birthDay);
                                }
                                inputLayout.dismiss();
                            }
                        }
                } ;

                inputLayout.setBtnClick(onClickListeners);
                inputLayout.setCancelable(false);
                inputLayout.show();
             }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AiriSDK.SDKOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AiriSDK.SDKOnPause();
    }

    public void othesClick() {

        customServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> tags = new ArrayList<>(3);
                tags.add("bug");
                String json = AiriSDKUtils.getInstance().getGs().toJson(tags);

                AiriSDK.ShowAiHelpFAQs("2.1.42","serverID 1","playerUid_271","playerName_271","2020-08-24",1000,json);
            }
        });

        findViewById(R.id.deviceID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("deviceID:" + AiriSDK.SDKGetDeviceID());
            }
        });

        statisticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] edits = {"事件名称"};
                String[] btns = {"adjust", "S2S"};
                inputLayout = new InputLayout(WelcomeActivity.this, edits, btns);
                View.OnClickListener[] clickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String event_name = inputLayout.edit_1.getText().toString();
                                if (TextUtils.isEmpty(event_name)) {
                                    setResultInfo("参数不能为空");
                                    return;
                                }
                                Map<String, String> map = new HashMap<>();
                                map.put("test1", "t_test_1");
                                map.put("test2", "t_test_2");
                                map.put("test3", "t_test_3");
                                String json = AiriSDKUtils.getInstance().getGs().toJson(map);
                                inputLayout.dismiss();
                                AiriSDK.SDKUserEventUpload(event_name, json);
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                inputLayout.dismiss();
                                AiriSDK.SDKServerToServer("devToken", "linkID", "event_name", "1", "USD");
                            }
                        }
                };

                inputLayout.setCancelable(false);
                inputLayout.setBtnClick(clickListeners);
                inputLayout.show();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = SampleUtils.getCacheBitmapFromView(welcomLayout);
                String file = SampleUtils.saveBitmap(bitmap, System.currentTimeMillis() + "");
                LogUtil.e("filefilefilefilefile:" + file);
                AiriSDK.SystemShare("123455", file);
            }
        });

        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKStore();
            }
        });

        backCenterBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlayout(getId("accountCenter"));
            }
        });
    }


    public void accountClick() {
        transCodeReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKTranscodeReq();
            }
        });
        linkFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLink(AiriSDKCommon.LOGINPLATFORM_FACEBOOK, "", "");
            }
        });

        unLinkFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKUnLink(AiriSDKCommon.LOGINPLATFORM_FACEBOOK);
            }
        });

        linkAmazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLink(AiriSDKCommon.LOGINPLATFROM_AMAZON, "", "");
            }
        });

        unlinkAmazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKUnLink(AiriSDKCommon.LOGINPLATFROM_AMAZON);
            }
        });

        linkTW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLink(AiriSDKCommon.LOGINPLATFORM_TWITTER, "", "");
            }
        });

        unLinkTW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKUnLink(AiriSDKCommon.LOGINPLATFORM_TWITTER);
            }
        });

        linkGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLink(AiriSDKCommon.LOGINPLATFORM_GOOGLE_EMAIL, "", "");
            }
        });

        unLinkGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKUnLink(AiriSDKCommon.LOGINPLATFORM_GOOGLE_EMAIL);
            }
        });

        linkGooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLink(AiriSDKCommon.LOGINPLATFORM_GOOGLE_GAME_PLAY, "", "");
            }
        });

        unLinkGooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKUnLink(AiriSDKCommon.LOGINPLATFORM_GOOGLE_GAME_PLAY);
            }
        });

        linkYostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] edits = {"邮箱", "邮箱验证码"};
                String[] btns = {"获取验证码", "绑定"};
                inputLayout = new InputLayout(WelcomeActivity.this, edits, btns);

                View.OnClickListener[] clickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = inputLayout.edit_1.getText().toString();
                                if (TextUtils.isEmpty(email)) {
                                    setResultInfo("参数不能为空");
                                    return;
                                }
                                AiriSDK.SDKVerificationCodeReq(email);
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = inputLayout.edit_1.getText().toString();
                                String code = inputLayout.edit_2.getText().toString();
                                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
                                    setResultInfo("参数不能为空");
                                    inputLayout.dismiss();
                                    return;
                                }
                                inputLayout.dismiss();
                                AiriSDK.SDKLink(AiriSDKCommon.LOGINPLATFORM_YOSTAR, email, code);
                            }
                        }
                };

                inputLayout.setBtnClick(clickListeners);
                inputLayout.setCancelable(false);
                inputLayout.show();
            }
        });

        unLinkYostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] edits = {"邮箱", "邮箱验证码"};
                String[] btns = {"获取验证码", "解除绑定"};


                inputLayout = new InputLayout(WelcomeActivity.this, edits, btns);

                View.OnClickListener[] clickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = inputLayout.edit_1.getText().toString();
                                if (TextUtils.isEmpty(email)) {
                                    setResultInfo("参数不能为空");
                                    return;
                                }
                                AiriSDK.SDKVerificationCodeReq(email);
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = inputLayout.edit_1.getText().toString();
                                String code = inputLayout.edit_2.getText().toString();
                                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
                                    setResultInfo("参数不能为空");
                                    inputLayout.dismiss();
                                    return;
                                }
                                AiriSDK.SDKUnLink(AiriSDKCommon.LOGINPLATFORM_YOSTAR, email, code);
                            }
                        }
                };

                inputLayout.setBtnClick(clickListeners);
                inputLayout.setCancelable(false);
                inputLayout.show();
            }
        });

        delAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKDeleteAccount();
            }
        });

        resetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKRebornAccount();
            }
        });

        resetLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKRebornAccount();
            }
        });

        findViewById(getId("backCenterBtn")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlayout(getId("accountCenter"));
            }
        });
    }

    public void onClickMethod() {
        //初始化
        AiriSDK.getInstance().setSdkInfoCallback(DemoConnect.getCallback());
        initSDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKInit(requestSDK, "googleplay", false, false);
            }
        });

        //登陆
        quickLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.QuickLogin();
            }
        });

        deviceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLogin(AiriSDKCommon.LOGINPLATFORM_DEVICE, "", "", false);
            }
        });

        amzonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLogin(AiriSDKCommon.LOGINPLATFROM_AMAZON, "", "", false);
            }
        });

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLogin(AiriSDKCommon.LOGINPLATFORM_FACEBOOK, "", "", false);
            }
        });

        twitterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLogin(AiriSDKCommon.LOGINPLATFORM_TWITTER, "", "", false);
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLogin(AiriSDKCommon.LOGINPLATFORM_GOOGLE_EMAIL, "", "", false);
            }
        });

        googleplayLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDK.SDKLogin(AiriSDKCommon.LOGINPLATFORM_GOOGLE_GAME_PLAY, "", "", false);
            }
        });

        inheritanceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultInfo("继承码登陆");
                String[] edit = {"UID", "继承码"};
                String[] btn = {"登录"};

                inputLayout = new InputLayout(WelcomeActivity.this, edit, btn);

                View.OnClickListener[] clickListener = {new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String arg_0 = inputLayout.edit_1.getText().toString();
                        String arg_1 = inputLayout.edit_2.getText().toString();
                        if (TextUtils.isEmpty(arg_0) || TextUtils.isEmpty(arg_1)) {
                            setResultInfo("参数不能为空.");
                            inputLayout.dismiss();
                            return;
                        }
                        setResultInfo(arg_0, arg_1);
                        AiriSDK.SDKLogin(AiriSDKCommon.LOGINPLATFORM_TRANSCODE, arg_0, arg_1, false);
                        inputLayout.dismiss();
                    }
                }};
                inputLayout.setBtnClick(clickListener);
                inputLayout.setCancelable(false);
                inputLayout.show();
            }
        });

        yostarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] edits = {"邮箱", "验证码"};
                String[] btns = {"获取验证码", "登录"};

                inputLayout = new InputLayout(WelcomeActivity.this, edits, btns);
                View.OnClickListener[] clickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = inputLayout.edit_1.getText().toString();
                                if (TextUtils.isEmpty(email)) {
                                    setResultInfo("参数不能为空.");
                                    inputLayout.dismiss();
                                    return;
                                }
                                AiriSDK.SDKVerificationCodeReq(email);
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = inputLayout.edit_1.getText().toString();
                                String code = inputLayout.edit_2.getText().toString();
                                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
                                    setResultInfo("参数不能为空.");
                                    inputLayout.dismiss();
                                    return;
                                }
                                setResultInfo(email, code);
                                AiriSDK.SDKLogin(AiriSDKCommon.LOGINPLATFORM_YOSTAR, email, code, false);
                                inputLayout.dismiss();
                            }
                        }
                };

                inputLayout.setBtnClick(clickListeners);
                inputLayout.setCancelable(false);
                inputLayout.show();
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlayout(getId("payLayout"));
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlayout(getId("aboutLayout"));
            }

        });

        otherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlayout(getId("otherLayout"));
            }
        });

        agreementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] edit = {};
                String[] btns = {"获取协议链接", "获取协议内容", "确认协议内容"};
                inputLayout = new InputLayout(WelcomeActivity.this, edit, btns);
                View.OnClickListener[] clickListeners = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = AiriSDK.SDKGetAgreement();
                                setResultInfo(url);
                                inputLayout.dismiss();
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AiriSDK.SDKGetAgreementInfo();
                                inputLayout.dismiss();
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AiriSDK.SDKConifrmAgreement();
                                inputLayout.dismiss();
                            }
                        }
                };

                inputLayout.setCancelable(false);
                inputLayout.setBtnClick(clickListeners);
                inputLayout.show();
            }
        });

        errorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] info = {"错误码"};
                String[] btns = {"获取错误信息"};
                inputLayout = new InputLayout(WelcomeActivity.this, info, btns);
                View.OnClickListener[] clicks = {
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String errorcode = inputLayout.edit_1.getText().toString();
                                if (TextUtils.isEmpty(errorcode)) {
                                    setResultInfo("输入正确的错误码");
                                    inputLayout.dismiss();
                                    return;
                                }
                                int code = -1;
                                try {
                                    code = Integer.parseInt(errorcode);
                                } catch (Exception e) {
                                    setResultInfo("输入正确的错误码");
                                    return;
                                }
                                String errormessage = AiriSDK.SDKGetErrorCode(code);
                                setResultInfo(errorcode + ":" + errormessage);
                                inputLayout.dismiss();
                            }
                        }
                };
                inputLayout.setCancelable(false);
                inputLayout.setBtnClick(clicks);
                inputLayout.show();
            }
        });

        backLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlayout(getId("accountLayout"));
            }
        });

        adjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, SampleActivity.class));
            }
        });
        queryConfigBtn.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {

                String[] info = {"配置KEY"};
                String[] btns = {"获取"};
                inputLayout = new InputLayout(WelcomeActivity.this, info, btns);
                View.OnClickListener[] clicks = {
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String key = inputLayout.edit_1.getText().toString();
                            LogUtil.d("key: "+key+"  value:"+AiriSDK.QueryRemoteConfig(key));
                        }
                    }
                };
                inputLayout.setCancelable(true);
                inputLayout.setBtnClick(clicks);
                inputLayout.show();

            }
        });
    }


}
