package com.ads.puzzle.flag.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements InitCallbackListener,
        OrderReceiverListener {
    public static int BUYSTARNUM = 10;
    private PEventImpl pEvent;
    private boolean initFail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // 创建libgdx视图
        pEvent = new PEventImpl(AndroidLauncher.this);
        View gameView = initializeForView(new Puzzle(pEvent), config);
        // 创建布局
        RelativeLayout layout = new RelativeLayout(this);
        // 添加libgdx视图
        layout.addView(gameView);
        setContentView(layout);

        loadGameConfig();
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
                        break;
                    case UmipaySDKStatusCode.PAY_PROCESS_FAIL:
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
                    Settings.adManager = false;
                    Settings.helpNum = Settings.helpNum + BUYSTARNUM;
                    pEvent.save();
                    handler.post(new Runnable() {
                        public void run() {
                            new AlertDialog.Builder(AndroidLauncher.this).setTitle("迪士尼迷宫").setMessage("购买完成！请重新启动游戏,去广告才有效.")
                                    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).setIcon(R.drawable.xiaozi).create().show();
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
}
