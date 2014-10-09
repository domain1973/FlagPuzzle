package com.ads.puzzle.flag.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.Puzzle;
import com.ads.puzzle.flag.Settings;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;

import net.umipay.android.GameParamInfo;
import net.umipay.android.UmiPaySDKManager;
import net.umipay.android.UmipayOrderInfo;
import net.umipay.android.UmipaySDKStatusCode;
import net.umipay.android.interfaces.InitCallbackListener;
import net.umipay.android.interfaces.OrderReceiverListener;
import net.umipay.android.interfaces.PayProcessListener;
import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements InitCallbackListener,
        OrderReceiverListener {
    public final static int BUYSTARNUM = 10;
    private final static String host = "bcs.duapp.com";
    private final static String accessKey = "NESAXkQp7S1SIeqUncUnTCIl";
    private final static String secretKey = "ZIQ2NE02RNWimzjyGI0Yh8NF4cAjouLf";
    private final static String bucket = "ads-series";
    private final static String path = "/mnt/sdcard/";

    private String adAtlasStr = "/ad.atlas";
    private File adAtlas = new File(path + "ad.atlas");
    private String urlStr = "/url.txt";
    private File url = new File(path + "url.txt");

    private boolean initFail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // 创建libgdx视图
        View gameView = initializeForView(new Puzzle(new PEventImpl(AndroidLauncher.this)), config);
        // 创建布局
        RelativeLayout layout = new RelativeLayout(this);
        // 添加libgdx视图
        layout.addView(gameView);
        setContentView(layout);

        loadGameConfig();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initBucket();
            }
        }).start();
        AdManager.getInstance(getContext()).setUserDataCollect(true);
        if (Settings.adManager) {
            addAdManager();
            spot();
        }
        initSDK();
        initPayProcessListener();
    }

    private void addAdManager() {
        //banner广告
        AdManager.getInstance(this).init("b55c14b4e357c19b", "2aeac354a405859c", false);
        // 实例化LayoutParams(重要)
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置广告条的悬浮位置
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
        // 调用Activity的addContentView函数
        addContentView(new AdView(this, AdSize.FIT_SCREEN), layoutParams);
    }

    private void spot() {
        // 展示插播广告，可以不调用loadSpot独立使用
        SpotManager.getInstance(
                this).showSpotAds(
                this,
                new SpotDialogListener() {
                    @Override
                    public void onShowSuccess() {
                        Log.i("YoumiAdDemo", "展示成功");
                    }

                    @Override
                    public void onShowFailed() {
                        Log.i("YoumiAdDemo", "展示失败");
                    }

                }
        );
    }

    /**
     * 初始化安全支付sdk
     */
    private void initSDK() {
        //初始化参数
        GameParamInfo gameParamInfo = new GameParamInfo();
        //您的应用的AppId
        gameParamInfo.setAppId("b55c14b4e357c19b");
        //您的应用的AppSecret123fc859610f597d
        gameParamInfo.setAppSecret("2aeac354a405859c");
        //false 订单充值成功后是使用服务器通知 true 订单充值成功后使用客户端回调
        gameParamInfo.setSDKCallBack(true);
        //调用sdk初始化接口
        UmiPaySDKManager.initSDK(this, gameParamInfo, this, this);
    }

    /**
     * 初始化支付动作回调接口
     */
    private void initPayProcessListener() {
        UmiPaySDKManager.setPayProcessListener(new PayProcessListener() {

            @Override
            public void OnPayProcess(int code) {
                switch (code) {
                    case UmipaySDKStatusCode.PAY_PROCESS_SUCCESS:
                        //充值完成，不等于充值成功，实际充值结果以订单回调接口为准
                        Toast.makeText(getApplicationContext(), "购买完成！请耐心等候充值结果",
                                Toast.LENGTH_SHORT).show();
                        Settings.adManager = false;
                        Settings.helpNum = Settings.helpNum + BUYSTARNUM;
                        break;
                    case UmipaySDKStatusCode.PAY_PROCESS_FAIL:
                        Toast.makeText(getApplicationContext(), "取消购买！",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    public boolean isNetwork() {
        return initFail;
    }

    /**
     * 初始化回调接口
     */
    @Override
    public void onInitCallback(int code, String msg) {
        initFail = false;
        if (code == UmipaySDKStatusCode.SUCCESS) {
            // 初始化成功后，即可正常调用充值
            Toast.makeText(this, "初始化成功",
                    Toast.LENGTH_SHORT).show();
        } else if (code == UmipaySDKStatusCode.INIT_FAIL) {
            // 初始化失败，一般在这里提醒用户网络有问题，反馈，等等问题
            Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
        } else if (code == 15) {
            initFail = true;
            // 网络错误
            Toast.makeText(this, "网络连接错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 接收到服务器返回的订单信息
     * ！！！注意，该返回是在非ui线程中回调，如果需要更新界面，需要手动使用主线刷新
     */
    @Override
    public List onReceiveOrders(List list) {
        //未处理的订单
        List<UmipayOrderInfo> newOrderList = list;
        //已处理的订单
        List<UmipayOrderInfo> doneOrderList = new ArrayList<UmipayOrderInfo>();
        //TODO 出来服务器返回的订单信息newOrderList，并将已经处理好充值的订单返回给sdk
        //TODO sdk将已经处理完的订单通知给服务器。服务器下次将不再返回游戏客户端已经处理过的订单
        for (UmipayOrderInfo newOrder : newOrderList) {
            try {
                //TODO 对订单order进行结算
                if (newOrder.getStatus() == 1) {
                    //注意，转成毫秒需要 乘以长整型1000L
                    String payTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", newOrder.getPayTime_s() * 1000L).toString();
                    final String tips = String.format(
                            "支付订单号:%s \n" +
                                    "外部订单号:%s \n" +
                                    "用户标记:%s \n" +
                                    "订单状态:%s \n" +
                                    "支付通道:%s \n" +
                                    "获得金币:%s \n" +
                                    "金额:%s￥ \n" +
                                    "支付时间:%s \n" +
                                    "自定义数据:%s \n",
                            newOrder.getOid(),
                            newOrder.getTradeNo(),
                            newOrder.getRid(),
                            newOrder.getStatus() + "",
                            newOrder.getPayType(),
                            newOrder.getAmount() + "",
                            newOrder.getRealMoney() + "",
                            payTime,
                            newOrder.getCData()
                    );
                    //在主线程更新界面
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
//                            String newtext = tips + mOrderResult_tv.getText().toString();
//                            mOrderResult_tv.setText(newtext);
                        }
                    });
                    //添加到已处理订单列表
                    doneOrderList.add(newOrder);
                }
            } catch (Exception e) {

            }
        }
        return doneOrderList;   //将已经处理过的订单返回给sdk，下次服务器不再返回这些订单
    }

    private void loadGameConfig() {
        SharedPreferences sharedata = getSharedPreferences("data", Context.MODE_PRIVATE);
        Settings.musicEnabled = sharedata.getBoolean("music", true);
        Settings.soundEnabled = sharedata.getBoolean("sound", true);
        Settings.unlockGateNum = sharedata.getInt("passNum", 0);
        Settings.helpNum = sharedata.getInt("helpNum", 3);
        Answer.gateStars.clear();
        String[] split = sharedata.getString("starNum", "0").split("[,]");
        for (String starNum : split) {
            if (!"".equals(starNum)) {
                Answer.gateStars.add(Integer.parseInt(starNum));
            }
        }
        Settings.adManager = sharedata.getBoolean("adManager", true);
    }

    private void initBucket() {
        try {
            BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
            BaiduBCS baiduBCS = new BaiduBCS(credentials, host);
            baiduBCS.setDefaultEncoding("UTF-8"); // Default UTF-8
            getObjectWithDestFile(baiduBCS, adAtlasStr, adAtlas);
            getObjectWithDestFile(baiduBCS, urlStr, url);
            for (int i=1; i<Integer.MAX_VALUE; i++) {
                try {
                    getObjectWithDestFile(baiduBCS, "/ad" + i + ".png" , new File(path + "ad" + i + ".png"));
                } catch (Exception x) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getObjectWithDestFile(BaiduBCS baiduBCS, String name, File file) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, name);
        baiduBCS.getObject(getObjectRequest, file);
    }
}
