package com.ads.puzzle.flag.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.PEvent;
import com.ads.puzzle.flag.Settings;
import com.ads.puzzle.flag.screen.GameScreen;
import com.ads.puzzle.flag.screen.MainScreen;

import net.umipay.android.UmiPaySDKManager;
import net.umipay.android.UmiPaymentInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OneKeyShareCallback;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2014/10/2.
 */
public class PEventImpl extends PEvent {

    private static final String adsUrl = "http://ads360.duapp.com/House";
    private static final String gameUrl = adsUrl + "/FlagPuzzle";
    public static final String SHARE_TITLE = "有趣的迷宫";
    public static final String SHARE_TEXT = "太难了,我不行,有种你来!";
    private AndroidLauncher launcher;
    private String title = "您好,我是小智";
    private ProgressDialog progressDialog;
    private Handler handler;
    private String gameLogoImage;

    public PEventImpl(AndroidLauncher androidLauncher) {
        launcher = androidLauncher;
        handler = new Handler();
    }

    private void openNetworkFailDlg() {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(title).setMessage("连接不到网络,请检查哦!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).setIcon(R.drawable.xiaozi).create().show();
            }
        });
    }

    @Override
    public void pay() {
        if (launcher.isNetwork()) {
            openNetworkFailDlg();
        } else {
            //设置充值信息
            UmiPaymentInfo paymentInfo = new UmiPaymentInfo();
            //业务类型，SERVICE_TYPE_QUOTA(固定额度模式，充值金额在支付页面不可修改)，SERVICE_TYPE_RATE(汇率模式，充值金额在支付页面可修改）
            paymentInfo.setServiceType(UmiPaymentInfo.SERVICE_TYPE_QUOTA);
            //定额支付金额，单位RMB
            paymentInfo.setPayMoney(1);
            //订单描述
            paymentInfo.setDesc(AndroidLauncher.BUYSTARNUM + "颗智慧星 + 去广告");
            //调用支付接口
            UmiPaySDKManager.showPayView(launcher, paymentInfo);
        }
    }

    @Override
    public void exit(final MainScreen mainScreen) {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(title).setMessage("确定要退出游戏吗?")
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                save();
                                launcher.exit();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton("爱迪出品", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(adsUrl);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        launcher.startActivity(it);
                    }
                }).setIcon(R.drawable.xiaozi).create().show();
            }
        });
    }

    @Override
    public void save() {
        SharedPreferences.Editor sharedata = launcher.getSharedPreferences("data", 0).edit();
        sharedata.putBoolean("music", Settings.musicEnabled);
        sharedata.putBoolean("sound", Settings.soundEnabled);
        sharedata.putInt("passNum", Settings.unlockGateNum);
        sharedata.putInt("helpNum", Settings.helpNum);
        StringBuffer sb = new StringBuffer();
        for (Integer starNum : Answer.gateStars) {
            sb.append(starNum).append(",");
        }
        sharedata.putString("starNum", sb.substring(0, sb.length() - 1));
        sharedata.putBoolean("adManager", Settings.adManager);
        sharedata.commit();
    }

    @Override
    public void sos(final GameScreen gs) {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(title).setMessage("您还有" + Settings.helpNum + "次机会,需要帮助吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Settings.helpNum = Settings.helpNum - 1;
                                gs.useSos();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(R.drawable.xiaozi).create().show();
            }
        });
    }

    @Override
    public void invalidateSos() {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(title).setMessage("智慧星不够,点击分享可以获取智慧星哦!")
                        .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNeutralButton("分享", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showShare();
                    }
                }).setNegativeButton("智慧星", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pay();
                    }
                }).setIcon(R.drawable.xiaozi).create().show();
            }
        });
    }

    @Override
    public void resetGame() {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(title).setMessage("是否重置您的进度?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Settings.unlockGateNum = 0;
                        Settings.helpNum = 3;
                        Answer.gateStars.clear();
                        Answer.gateStars.add(0);
                        save();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(R.drawable.xiaozi).create().show();
            }
        });
    }

    @Override
    public void share() {
        showShare();
    }

    @Override
    public void install(String url) {
        openFile(downLoadFile(url));
    }

    @Override
    public boolean isNetworkEnable() {
        if (!note_Intent(launcher.getContext())) {
            openNetworkFailDlg();
            return false;
        }
        return true;
    }

    private boolean note_Intent(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            // 当前网络不可用
            return false;
        }
        return true;
    }

    private void showShare() {
        if (!note_Intent(launcher.getContext())) {
            openNetworkFailDlg();
        } else {
            initImagePath();
            handler.post(new Runnable() {
                public void run() {
                    ShareSDK.initSDK(launcher);
                    OnekeyShare oks = new OnekeyShare();
                    oks.setNotification(R.drawable.ic_launcher, launcher.getContext().getString(R.string.app_name));
                    oks.setTitle(SHARE_TITLE);
                    oks.setText(SHARE_TEXT);
                    oks.setImagePath(gameLogoImage);
                    oks.setUrl(gameUrl);
                    // 令编辑页面显示为Dialog模式
                    oks.setDialogMode();
                    // 在自动授权时可以禁用SSO方式
                    oks.disableSSOWhenAuthorize();
                    // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
                    oks.setCallback(new OneKeyShareCallback());
                    oks.show(launcher.getContext());
                }
            });
        }
    }

    private void initImagePath() {
        try {
            String cachePath = cn.sharesdk.framework.utils.R.getCachePath(launcher, null);
            gameLogoImage = cachePath + "gamelogo.png";
            File file = new File(gameLogoImage);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(launcher.getResources(), R.drawable.ic_launcher);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            gameLogoImage = null;
        }
    }

    protected File downLoadFile(final String httpUrl) {
        handler.post(new Runnable() {
            public void run() {
                openProgressBar();
            }
        });
        final String fileName = "ads.apk";
        String path1 = "/mnt/sdcard/update";
        File tmpFile = new File(path1);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        final File file = new File(path1 + "/" + fileName);
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[256];
            conn.connect();
            double count = 0;
            if (conn.getResponseCode() >= 400) {
                Toast.makeText(launcher, "连接超时", Toast.LENGTH_SHORT).show();
            } else {
                while (count <= 100) {
                    if (is != null) {
                        int numRead = is.read(buf);
                        if (numRead <= 0) {
                            break;
                        } else {
                            fos.write(buf, 0, numRead);
                        }
                    } else {
                        break;
                    }
                }
            }
            conn.disconnect();
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        return file;
    }

    private void openProgressBar() {
        progressDialog = new ProgressDialog(launcher.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在下载,请稍候.");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    //打开APK程序代码
    private void openFile(File file) {
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        launcher.startActivity(intent);
    }
}
