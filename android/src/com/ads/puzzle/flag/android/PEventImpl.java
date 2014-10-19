package com.ads.puzzle.flag.android;

import android.app.ActivityManager;
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
import com.ads.puzzle.flag.Series;
import com.ads.puzzle.flag.Settings;
import com.ads.puzzle.flag.screen.GameScreen;
import com.ads.puzzle.flag.screen.MainScreen;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;

import net.umipay.android.UmiPaySDKManager;
import net.umipay.android.UmiPaymentInfo;
import net.umipay.android.poll.SmsReceiverService;

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
    private final static String host = "bcs.duapp.com";
    private final static String accessKey = "NESAXkQp7S1SIeqUncUnTCIl";
    private final static String secretKey = "ZIQ2NE02RNWimzjyGI0Yh8NF4cAjouLf";
    private final static String bucket = "ads-series";
    private final static String path = "/mnt/sdcard/ads/";
    private final String FLAGTITLE = "国旗迷宫";

    private String adAtlasStr = "/ad.atlas";
    private File adAtlas = new File(path + "ad.atlas");
    private String urlStr = "/url.txt";
    private File url = new File(path + "url.txt");
    private static final String adsUrl = "http://ads360.duapp.com/House";
    private static final String gameUrl = adsUrl + "/DisneyPuzzle";
    public static final String SHARE_TITLE = "有趣的迷宫";
    public static final String SHARE_TEXT = "太难了,我不行,有种你来!\n" +
            "点击后,请使用浏览器下载安装.";
    private AndroidLauncher launcher;
    private String title = "您好,我是小智";
    private ProgressDialog progressDialog;
    private Handler handler;
    private String gameLogoImage;

    public PEventImpl(AndroidLauncher androidLauncher) {
        launcher = androidLauncher;
        handler =  new Handler();
        fillAd();
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
            handler.post(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(launcher).setTitle(title + ",请选择商品").setIcon(
                            R.drawable.xiaozi).setSingleChoiceItems(
                            new String[]{"1元 5颗智慧星+去广告(热销)", "2元 20颗智慧星+去广告(超值)", "5元 100颗智慧星+去广告(土豪)"}, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int starNum = 0;
                                    int yuan = 1;
                                    switch (which) {
                                        case 0:
                                            starNum = 5;
                                            yuan = 1;
                                            break;
                                        case 1:
                                            starNum = 20;
                                            yuan = 2;
                                            break;
                                        case 2:
                                            starNum = 100;
                                            yuan = 5;
                                            break;
                                    }
                                    buy(starNum, yuan);
                                }
                            }
                    ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
        }
    }

    private void buy(int starNum, int  yuan) {
        //设置充值信息
        UmiPaymentInfo paymentInfo = new UmiPaymentInfo();
        //业务类型，SERVICE_TYPE_QUOTA(固定额度模式，充值金额在支付页面不可修改)，SERVICE_TYPE_RATE(汇率模式，充值金额在支付页面可修改）
        paymentInfo.setServiceType(UmiPaymentInfo.SERVICE_TYPE_QUOTA);
        //定额支付金额，单位RMB
        paymentInfo.setPayMoney(yuan);
        //订单描述
        AndroidLauncher.BUYSTARNUM = starNum;
        paymentInfo.setDesc(AndroidLauncher.BUYSTARNUM + "颗智慧星 + 去广告");
        //调用支付接口
        UmiPaySDKManager.showPayView(launcher, paymentInfo);
    }

    @Override
    public void exit(final MainScreen ms) {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(title).setMessage("确定要退出游戏吗?")
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                save();
                                Intent smsIntent = new Intent(launcher.getContext(), SmsReceiverService.class);
                                launcher.stopService(smsIntent);
                                int currentVersion = android.os.Build.VERSION.SDK_INT;
                                if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                                    startMain.addCategory(Intent.CATEGORY_HOME);
                                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    launcher.startActivity(startMain);
                                    System.exit(0);
                                } else {// android2.1
                                    ActivityManager am = (ActivityManager) launcher.getSystemService(launcher.ACTIVITY_SERVICE);
                                    am.restartPackage(launcher.getPackageName());
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ms.setTouchBack(true);
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
    public void about() {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(FLAGTITLE).setMessage(
                        "版本: 1.0.0\n" +
                                "爱迪工作室 \n" +
                                "版权所有c 2014\n" +
                                "客户邮箱\n" +
                                "domainxu@foxmail.com\n" +
                                "工作室网址\n" +
                                "http://ads360.duapp.com/House")
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(R.drawable.xiaozi).create().show();
            }
        });
    }

    @Override
    public void netSlowInfo() {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(FLAGTITLE).setMessage(
                        "网速太慢,请稍候再试.")
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(R.drawable.xiaozi).create().show();
            }
        });
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
    public void install(final Series series) {
        handler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(launcher).setTitle(series.getName()).setMessage(series.getDetail())
                        .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openProgressBar();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        openFile(downLoadFile(series.getUrl()));
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(R.drawable.xiaozi).create().show();
            }
        });
    }

    @Override
    public boolean isNetworkEnable() {
        if (!isConnect2Net(launcher.getContext())) {
            openNetworkFailDlg();
            return false;
        }
        return true;
    }

    private void fillAd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initBucket();
            }
        }).start();
    }

    private boolean isConnect2Net(Context context) {
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
        if (!isConnect2Net(launcher.getContext())) {
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
        } catch(Throwable t) {
            t.printStackTrace();
            gameLogoImage = null;
        }
    }

    private File downLoadFile(final String httpUrl) {
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

    private void initBucket() {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
            BaiduBCS baiduBCS = new BaiduBCS(credentials, host);
            baiduBCS.setDefaultEncoding("UTF-8"); // Default UTF-8
            getObjectWithDestFile(baiduBCS, adAtlasStr, adAtlas);
            getObjectWithDestFile(baiduBCS, urlStr, url);
            getObjectWithDestFile(baiduBCS, "/ad.png" , new File(path + "ad.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getObjectWithDestFile(BaiduBCS baiduBCS, String name, File file) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, name);
        baiduBCS.getObject(getObjectRequest, file);
    }
}
