package com.YoStarEN.AzurLane;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.YoStarEN.AzurLane.DemoConnect;
import com.YoStarEN.AzurLane.R;
import com.YoStarEN.AzurLane.ToastUtils;
import com.YoStarEN.AzurLane.Utils;
import com.airisdk.sdkcall.AiriSDKInstance;
import com.airisdk.sdkcall.tools.entity.Platform;
import com.airisdk.sdkcall.tools.entity.ProductBeans;
import com.airisdk.sdkcall.tools.utils.AiriSDKUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private static TextView resultTv ;
    private Button initBtn,birthDaySetBtn,loginBtn,
            loginFastBtn ,getTransCodeBtn,logoutBtn,
            getDeviceIdBtn,showHelpshiftBTn,linkBtn,
            unlinkBtn,uploadEventBtn,goPayBtn,backBtn,
            backPayBtn,payBtn,systemShareBtn;
    private Spinner loginSpinner,linkSpinner ,payMethodSpinner,productIdSpinner;
    private Platform platform = Platform.DEVICE ;
    private EditText productIdEdit,extraDataEdit,serverEdit ;

    private static LinearLayout initLayout,loginLayout,settingLayout,payLayout;

    private String params1,params2 = "" ;

    private boolean isLogin = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(this);

        initView() ;


        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDKInstance.getInstance().initSDK(MainActivity.this,DemoConnect.getInstance().getInitResultCallback());
            }
        });

        loginFastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDKInstance.getInstance().SDKQuickLogin(DemoConnect.getInstance().getLoginResultCallback());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = true ;
                platform = getPlatform(loginSpinner.getSelectedItemPosition()) ;
                if (platform == Platform.YOSTAR || platform == Platform.TRANSCODE){
                    showYostarDialog(platform) ;
                }else{
                    login() ;
                }
            }
        });

        uploadEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>() ;
                map.put("params1","test1") ;
                map.put("params2","test2") ;
                JSONObject json = new JSONObject(map);
                AiriSDKInstance.getInstance().SDKUserEventUpload("role_levelup",json.toString());
            }
        });

        getTransCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDKInstance.getInstance().SDKTranscodeReq(DemoConnect.getInstance().getTranscodeResultCallback());
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDKInstance.getInstance().SDKLogout(DemoConnect.getInstance().getLogoutCallback());
                resultTv.setText("");
            }
        });

        getDeviceIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultTv(AiriSDKInstance.getInstance().SDKGetDeviceID());
            }
        });

        showHelpshiftBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultTv("打开...");
                AiriSDKInstance.getInstance().SDKOpenHelpShift();
            }
        });

        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = false ;
                int postion = linkSpinner.getSelectedItemPosition() ;
                platform = getPlatform(postion + 2) ;
                if (platform == Platform.YOSTAR){
                    showYostarDialog(platform);
                }else{
                    link() ;
                }
            }
        });

        unlinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDKInstance.getInstance().SDKUnlink(platform,DemoConnect.getInstance().getUnLinkResultCallback());
            }
        });

        birthDaySetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("设置生日");
                final EditText edit = new EditText(MainActivity.this);
                edit.setBackgroundResource(R.drawable.editshape);

                builder.setView(edit);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String birthDay = edit.getText().toString() ;
                        if (TextUtils.isEmpty(birthDay)){
                            ToastUtils.showShortToast("生日不可为空");
                        }else{
                            AiriSDKInstance.getInstance().SDKSetBirth(birthDay,DemoConnect.getInstance().getBirthSetResultCallback());
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();


            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginLayout();
            }
        });

        goPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> productIdList = new ArrayList<>() ;
                for (ProductBeans.ProductsBean bean :AiriSDKUtils.getInstance().getGooglePrducts()){
                    productIdList.add(bean.getStoreProductId()) ;
                }
                String[] mItems = productIdList.toArray(new String[1]) ;
                // 建立Adapter并且绑定数据源
                ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, mItems);
                //绑定 Adapter到控件
                productIdSpinner.setAdapter(_Adapter);

                showPayBtn() ;
            }
        });

        backPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingLayout();
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = payMethodSpinner.getSelectedItemPosition() ;
                platform = getPlatform(postion + 5) ;
                String productId = productIdSpinner.getSelectedItem().toString() ;
                String extraData = extraDataEdit.getText().toString() ;
                String serverTag = serverEdit.getText().toString() ;
                AiriSDKInstance.getInstance().SDKPurchase(platform,productId,serverTag,extraData,DemoConnect.getInstance().getPurchaseResultCallback());
            }
        });

        systemShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AiriSDKInstance.getInstance().SDKSystemShare("测试图片-截图",activityShot(MainActivity.this),DemoConnect.getInstance().getShareResultCallback());
            }
        });
    }


    public static Bitmap activityShot(Activity activity) {
        /*获取windows中最顶层的view*/
        View view = activity.getWindow().getDecorView();

        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        //获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        WindowManager windowManager = activity.getWindowManager();

        //获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        //去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width,
                height-statusBarHeight);

        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView(){
        resultTv = findViewById(R.id.resultTv) ;
        initBtn = findViewById(R.id.initBtn) ;
        loginBtn = findViewById(R.id.loginBtn) ;
        loginSpinner = findViewById(R.id.loginSpinner) ;
        linkSpinner = findViewById(R.id.linkSpinner) ;
        loginFastBtn = findViewById(R.id.loginFastBtn) ;
        getTransCodeBtn = findViewById(R.id.GetTransCodeBtn) ;
        logoutBtn = findViewById(R.id.logoutBtn) ;
        linkBtn = findViewById(R.id.linkBtn) ;
        unlinkBtn = findViewById(R.id.unlinkBtn) ;
        getDeviceIdBtn = findViewById(R.id.getDeviceIdBtn) ;
        showHelpshiftBTn = findViewById(R.id.showHelpshiftBTn) ;
        birthDaySetBtn = findViewById(R.id.birthDaySetBtn) ;
        resultTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        uploadEventBtn = findViewById(R.id.uploadEventBtn) ;
        backBtn = findViewById(R.id.backBtn) ;
        goPayBtn = findViewById(R.id.goPayBtn) ;
        backPayBtn = findViewById(R.id.backPayBtn) ;
        extraDataEdit = findViewById(R.id.extraDataEdit) ;
        payMethodSpinner = findViewById(R.id.payMethodSpinner) ;
        productIdSpinner = findViewById(R.id.productIdSpinner) ;
        serverEdit = findViewById(R.id.serverEdit) ;
        payBtn = findViewById(R.id.payBtn) ;
        systemShareBtn = findViewById(R.id.systemShareBtn) ;

        initLayout = findViewById(R.id.initLayout) ;
        loginLayout = findViewById(R.id.loginLayout) ;
        settingLayout = findViewById(R.id.settingLayout) ;
        payLayout = findViewById(R.id.payLayout);
        initUI() ;
        showInitLayout() ;
    }

    public static void initUI(){
        initLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.GONE);
        settingLayout.setVisibility(View.GONE);
        payLayout.setVisibility(View.GONE);
    }

    public static void showInitLayout(){
        initUI();
        initLayout.setVisibility(View.VISIBLE);
    }

    public static void showLoginLayout(){
        initUI();
        loginLayout.setVisibility(View.VISIBLE);
    }

    public static void showSettingLayout(){
        initUI();
        settingLayout.setVisibility(View.VISIBLE);
    }

    public static void showPayBtn(){
        initUI();
        payLayout.setVisibility(View.VISIBLE);
    }

    public void login(){
        AiriSDKInstance.getInstance().SDKLogin(platform, params1, params2, true,DemoConnect.getInstance().getLoginResultCallback());
    }

    public void link(){
        AiriSDKInstance.getInstance().SDKLink(platform,params1,params2,DemoConnect.getInstance().getLinkResultCallback());
    }

    public Platform getPlatform(int position){
        Platform mPlat = Platform.DEVICE ;
        switch (position){
            case 0:
                mPlat = Platform.DEVICE ;
                break;
            case 1:
                mPlat = Platform.TRANSCODE ;
                break;
            case 2:
                mPlat = Platform.TWITTER ;
                break;
            case 3:
                mPlat = Platform.FACEBOOK ;
                break;
            case 4:
                mPlat = Platform.YOSTAR ;
                break;
            case 5:
                mPlat = Platform.GOOGLE ;
                break;
            case 6:
                mPlat = Platform.AU ;
                break;
        }
        return mPlat ;
    }

    public static void setResultTv(String msg){
        resultTv.setText(resultTv.getText().toString() + " \n" + msg);
    }

    private void showYostarDialog(final Platform sPlatform){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = View.inflate(MainActivity.this, R.layout.dialog_yostar_edit, null);
        Button getEmailCode = view.findViewById(R.id.btnGetEmailCode) ;
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        final EditText accountEmailEdit = view.findViewById(R.id.accountEmailEdit) ;
        final EditText emailCodeEdit = view.findViewById(R.id.emailCodeEdit) ;

        if (sPlatform == Platform.YOSTAR){
            accountEmailEdit.setHint("请输入用户邮箱");
            emailCodeEdit.setHint("请输入获取的验证码");
            getEmailCode.setVisibility(View.VISIBLE);
        }else if (sPlatform == Platform.TRANSCODE){
            accountEmailEdit.setHint("请输入继承码");
            emailCodeEdit.setHint("请输入用户ID");
            getEmailCode.setVisibility(View.GONE);
        }

        getEmailCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountEmail = accountEmailEdit.getText().toString() ;
                if (sPlatform == Platform.YOSTAR){
                    if (TextUtils.isEmpty(accountEmail)){
                        ToastUtils.showShortToast("没有输入用户邮箱");
                    }else {
                        AiriSDKInstance.getInstance().SDKVerificationCodeReq(accountEmail, DemoConnect.getInstance().getCodeReqResultCallback());
                    }
                }
            }
        });

        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                params1 = accountEmailEdit.getText().toString() ;
                params2 = emailCodeEdit.getText().toString()  ;
                if (TextUtils.isEmpty(params1) || TextUtils.isEmpty(params2)){
                    ToastUtils.showShortToast("参数不全");
                }else{
                    if (isLogin){
                        login();
                    }else{
                        link() ;
                    }
                }
            }
        });
        dialog.show();


    }
}
