package com.huizhuang.zxsq.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsNode;
import com.huizhuang.zxsq.share.OnekeyShare;
import com.huizhuang.zxsq.ui.activity.ShareShowActivity;

/**
 * @ClassName: Util
 * @Description: 工具集
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:07:52
 */
public class Util {

    /**
     * 安装apk文件
     * 
     * @param file
     * @param context
     */
    public static void installApk(Context context, String apkPath) {
        File file = new File(apkPath);
        if (!file.exists()) {
            Toast.makeText(context, "未找不到安装文件", Toast.LENGTH_LONG);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载apk文件
     * 
     * @param packageName
     * @param context
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("package:" + packageName);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 获取本地ip地址
     * 
     * @return
     */
    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前版本标示号
     * 
     * @param mContext
     * @return
     */
    public static int getCurrentVersionCode(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前版本号
     * 
     * @param mContext
     * @return
     */
    public static String getCurrentVersionName(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 是否挂载了SD卡
     * 
     * @return
     */
    public static boolean isHaveExternalStorage() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     * 
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern emailer =
                Pattern.compile("[_a-z\\d\\-\\./]+@[_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)*"
                        + "(\\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$");
        if (email == null || email.trim().length() == 0) return false;
        return emailer.matcher(email.toLowerCase()).matches();
    }

    /**
     * 判别手机是否为正确手机号码
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }

    /**
     * 拨打电话
     * 
     * @param activity
     * @param phone
     */
    public static void callPhone(Activity activity, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(intent);
    }

    /**
     * 验证是否是有效的验证码
     */
    public static boolean IsValidVerify(String verify) {
        // Pattern pattern = Pattern.compile("\\d{4}");
        // Matcher matcher = pattern.matcher(verify);
        if (verify.length() < 1) {
            return false;
        }
        return true;
    }

    /**
     * 发送短信
     * 
     * @param activity
     * @param phone
     * @param msg
     */
    public static void sendMessage(Activity activity, String phone, String msg) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", msg);
        activity.startActivity(intent);
    }

    public static String formatMoney(double amount) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("##,###.00");
        return df.format(amount);
    }

    /**
     * 将传入的金额（单位分）格式化为元，保留两位小数 格式如：5.00
     * 
     * @param s
     * @return
     */
    public static String formatMoneyNoUnit(String s) {
        if (TextUtils.isEmpty(s)) {
            return "0.00";
        }
        double num = Double.parseDouble(s);
        DecimalFormat formater = new DecimalFormat("###,##0.00");
        String result = formater.format(num / 100.0d);
        return result;
    }

    /**
     * 将传入的金额（单位分）格式化为元，保留两位小数 格式如：5.00
     * 
     * @param s
     * @return
     */
    public static String formatMoneyNoUnitTwo(String s) {
        if (TextUtils.isEmpty(s)) {
            return "0.00";
        }
        double num = Double.parseDouble(s);
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("0.00");
        String result = df.format(num);
        return result;
    }

    /**
     * 格式化金额
     * 
     * @param s
     * @param len
     * @return
     */
    public static String formatMoneyNoSingle(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        String result = formater.format(num);
        if (result.indexOf(".") == -1) {
            result = result + ".00";
        } else {
            result = result;
        }
        return result;
    }

    /**
     * 格式化金额
     * 
     * @param s
     * @param len
     * @return
     */
    public static String formatMoney(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        String result = formater.format(num);
        if (result.indexOf(".") == -1) {
            result = "￥" + result + ".00";
        } else {
            result = "￥" + result;
        }
        return result;
    }

    /**
     * 格式化金额
     * 
     * @param s
     * @param len
     * @return
     */
    public static String formatMoneyNoZero(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        String result = formater.format(num);
        if (result.indexOf(".") == -1) {
            result = "￥" + result + "";
        } else {
            result = "￥" + result;
        }
        return result;
    }

    public static User parseShareSDKUser(String platform, HashMap<String, Object> res) {
        User user = new User();
        if (res == null || res.size() <= 0) {
            return user;
        }
        if ("SinaWeibo".equals(platform)) {
            // users[id, name, description]
            user.setOpenId(String.valueOf(res.get("idstr")));
            user.setNickname(String.valueOf(res.get("name")));
            user.setAvatar(String.valueOf(res.get("profile_image_url")));
            user.setRemark(String.valueOf(res.get("description")));

        } else if ("QQ".equals(platform)) {
            // [ret=0, null, null, null,
            // figureurl_qq_1=http://q.qlogo.cn/qqapp/100371282/332DE6AA17C1F84523D87A33EB37673C/40,
            // nickname=Mr.♛.Li, null, null, yellow_vip_level=0, null, null,
            // is_lost=0, null, msg=, null, city=成都,
            // figureurl_1=http://qzapp.qlogo.cn/qzapp/100371282/332DE6AA17C1F84523D87A33EB37673C/50,
            // vip=0, null,
            // figureurl_2=http://qzapp.qlogo.cn/qzapp/100371282/332DE6AA17C1F84523D87A33EB37673C/100,
            // null, null, null, null, province=四川, gender=男, null, null,
            // figureurl=http://qzapp.qlogo.cn/qzapp/100371282/332DE6AA17C1F84523D87A33EB37673C/30,
            // null, null, null]
            user.setOpenId(String.valueOf(res.get("name")));
            user.setNickname(String.valueOf(res.get("nickname")));
            user.setAvatar(String.valueOf(res.get("figureurl_qq_1")));
            user.setProvince(String.valueOf(res.get("province")));
            user.setCity(String.valueOf(res.get("city")));
        } else if ("TencentWeibo".equals(platform)) {
            // info[nick, name, tweet[text]]
            user.setOpenId(String.valueOf(res.get("openid")));
            user.setNickname(String.valueOf(res.get("name")));
            user.setAvatar(String.valueOf(res.get("head")) + "/100");
        } else if ("Facebook".equals(platform)) {
            // data[id, name]
            @SuppressWarnings("unchecked")
            ArrayList<HashMap<String, Object>> datas =
                    (ArrayList<HashMap<String, Object>>) res.get("data");
            for (HashMap<String, Object> d : datas) {
                user.setOpenId(String.valueOf(d.get("id")));
                user.setNickname(String.valueOf(d.get("name")));
                user.setRemark(String.valueOf(d.get("description")));
                @SuppressWarnings("unchecked")
                HashMap<String, Object> picture = (HashMap<String, Object>) d.get("picture");
                if (picture != null) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> pData = (HashMap<String, Object>) picture.get("data");
                    if (d != null) {
                        user.setAvatar(String.valueOf(pData.get("url")));
                    }
                }
            }
        } else if ("Renren".equals(platform)) {
            // info[nick, name, tweet[text]]
            user.setOpenId(String.valueOf(res.get("openid")));
            user.setNickname(String.valueOf(res.get("name")));
            user.setAvatar(String.valueOf(res.get("head")) + "/100");
        } else if ("Douban".equals(platform)) {
            // info[nick, name, tweet[text]]
            user.setOpenId(String.valueOf(res.get("openid")));
            user.setNickname(String.valueOf(res.get("name")));
            user.setAvatar(String.valueOf(res.get("head")) + "/100");
        } else if ("Wechat".equals(platform)) {
            // [
            // res: {
            // sex=1,
            // nickname=null,
            // privilege=[
            //
            // ],
            // unionid=okWB5uAgAxc11M9Q2K0qxolog8f8,
            // province=Sichuan,
            // openid=oTiadjis8Dqb73bd6htH4Ob75o9s,
            // language=zh_CN,
            // headimgurl=http:
            // //wx.qlogo.cn/mmopen/CErxiaUjIdZxYn6viadSCO2EibVFY6bUkHia01Pib6dia3622LZFFD86iapouow6ZAQo92hx9VIwWAorPb9ZdibE8CRNLVNNnVGhWF4ic/0,
            // city=Chengdu,
            // country=CN
            // }
            // ]
            user.setOpenId(String.valueOf(res.get("openid")));
            user.setNickname(String.valueOf(res.get("nickname")));
            user.setAvatar(String.valueOf(res.get("headimgurl")));
            user.setProvince(String.valueOf(res.get("province")));
            user.setCity(String.valueOf(res.get("city")));
        } else if ("Twitter".equals(platform)) {
            // users[screen_name, name, description]
        }
        return user;
    }

    /**
     * 统一分享方法, 如果有界面分享需要回调结果就在调用的该方法的Activity中的onActivityResult方法中通requestCode==
     * Constants.TO_SHARE_CODE判断获取结果
     * 如果一键快捷分享需要自己的回调，就在share对象中传入platformActionListener回调对象,不传入就调用系统默认的回调
     * 
     * @param silent 是否指定平台分享 true指定平台的一键快捷分享 false有界面的多个平台分享
     * @param context
     * @param share 分享的参数(isCallBack为true时接收回调结果，否则不接收,如果本地图片和网络图片地址都没传就默认截屏)
     */
    public static void showShare(boolean silent, Activity context, Share share) {
        if (share == null) {
            share = new Share();
        }
        if (!silent) {
            // 如果没有传入网络图片并且没有本地图片地址就截屏
            if (TextUtils.isEmpty(share.getImageUrl()) && TextUtils.isEmpty(share.getImagePath())) {
                Bitmap bitmap =
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.share);
                String path = ScreenShotUtil.savePic(bitmap, context);
                share.setImagePath(path);
            }
            Intent intent = new Intent(context, ShareShowActivity.class);
            Bundle bd = new Bundle();
            bd.putSerializable(AppConstants.PARAM_SHARE, share);
            intent.putExtras(bd);
            if (share.isCallBack()) {
                context.startActivityForResult(intent, AppConstants.TO_SHARE_CODE);
            } else {
                context.startActivity(intent);
            }
        } else {
            final OnekeyShare oks = new OnekeyShare();
            oks.setNotification(R.drawable.ic_launcher, "");
            oks.setAddress(share.getAddress());
            oks.setTitle(share.getTitle());
            oks.setTitleUrl(share.getTitleUrl());
            oks.setText(share.getText());// 分享内容

            if (TextUtils.isEmpty(share.getImageUrl())) {
                // 如果没有传入网络图片并且没有本地图片地址就截屏
                if (TextUtils.isEmpty(share.getImagePath())) {
                    Bitmap bitmap =
                            BitmapFactory.decodeResource(context.getResources(), R.drawable.share);
                    String path = ScreenShotUtil.savePic(bitmap, context);
                    oks.setImagePath(path);
                } else {
                    oks.setImagePath(share.getImagePath());
                }
            } else {
                oks.setImageUrl(share.getImageUrl());
            }
            oks.setUrl(share.getUrl());
            oks.setComment(share.getComment());
            oks.setSite(share.getSite());
            oks.setSiteUrl(share.getSiteUrl());
            oks.setVenueName(share.getVenueName());
            oks.setVenueDescription(share.getVenueDescription());
            oks.setSilent(silent);
            oks.setPlatform(share.getPlatformName());
            // 令编辑页面显示为Dialog模式
            oks.setDialogMode();
            // 在自动授权时可以禁用SSO方式
            oks.disableSSOWhenAuthorize();
            // 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
            // oks.addHiddenPlatform(TencentWeibo.NAME);
            // oks.addHiddenPlatform(QQ.NAME);
            // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
            if (share.getPlatformActionListener() != null) {
                oks.setCallback(share.getPlatformActionListener());
            }
            oks.show(context);
        }
        // oks.setShareContentCustomizeCallback(new
        // ShareContentCustomizeDemo());

        // 去除注释，演示在九宫格设置自定义的图标
        // Bitmap logo = BitmapFactory.decodeResource(menu.getResources(),
        // R.drawable.ic_launcher);
        // String label = menu.getResources().getString(R.string.app_name);
        // OnClickListener listener = new OnClickListener() {
        // public void onClick(View v) {
        // String text = "Customer Logo -- ShareSDK " +
        // ShareSDK.getSDKVersionName();
        // Toast.makeText(menu.this, text, Toast.LENGTH_SHORT).show();
        // oks.finish();
        // }
        // };
        // oks.setCustomerLogo(logo, label, listener);
    }

    public static String getPngName(String prefix) {
        return prefix + ".png";
    }

    public static String getOpenType(String platformName) {
        if (platformName.equals("QQ")) {
            return "qq";
        } else if (platformName.equals("Douban")) {
            return "douban";
        } else if (platformName.equals("SinaWeibo")) {
            return "xlwb";
        } else if (platformName.equals("Wechat")) {
            return "wexin";
        } else if (platformName.equals("Renren")) {
            return "renren";
        } else if (platformName.equals("Douban")) {
            return "douban";
        } else if (platformName.equals("TencentWeibo")) {
            return "tencentweibo";
        }
        return null;
    }

    public static String createImagePath(Context context) {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            String fileName = Util.getPngName(DateUtil.getStringDate("yyyyMMddHHmmssSSS"));
            File file;
            try {
                file = FileUtil.createSDFile(AppConfig.PICTURE_PATH, fileName);
                return file.getPath();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "在存储卡中创建图片失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "存储卡已取出，拍照、相册功能暂不可用", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    /**
     * 格式化金额（精确到元，取消小数点）
     * 
     * @param s
     * @return
     */
    public static String formatMoneyUnitYuan(String s) {
        String str = formatMoney(s, 2);
        return str.substring(0, str.indexOf("."));
    }

    /**
     * 用系统浏览器打开连接地址
     * 
     * @param context
     * @param url
     */
    public static void toBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 判断字符串是否是数字
     * 
     * @param str
     * @return
     */

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 根据节点ID得到节点名称
     * 
     * @param context
     * @param id
     * @return
     */
    public static String getNodeNameById(String id) {
        List<KeyValue> zxNodes = ZxsqApplication.getInstance().getConstant().getZxNodes();
        for (KeyValue keyValue : zxNodes) {
            if (keyValue.getId().equals(id)) {
                return keyValue.getName();
            }
        }
        return null;
    }

    /**
     * 根据风格ID得到风格名称
     * 
     * @param id
     * @return
     */
    public static String getRoomStyleNameById(int id) {
        List<KeyValue> roomStyles = ZxsqApplication.getInstance().getConstant().getRoomStyles();
        for (KeyValue keyValue : roomStyles) {
            if (keyValue.getId().equals(id + "")) {
                return keyValue.getName();
            }
        }
        return null;
    }

    /**
     * 是否是有效的
     * 
     * @param str
     * @return
     */
    public static boolean isValidTagAndAlias(String str) {
        Pattern pattern = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 根据nodeId（节点id）得到装修宝典对应的node（节点）
     * 
     * @param nodeId 节点id
     * @return
     */
    public static String getZxbdNodeByNodeId(String nodeId) {
        String node = new String();
        switch (Integer.valueOf(nodeId.trim())) {
            case 1:// 开工阶段
                node = "p5";
                break;
            case 2:// 水电阶段
                node = "p1";
                break;
            case 3:// 泥木阶段
                node = "p2";
                break;
            case 4:// 油漆阶段
                node = "p3";
                break;
            case 5:// 竣工阶段
                node = "p4";
                break;
            default:
                break;
        }
        return node;
    }

    /**
     * 根据不同的订单状态得到对应的分享文案
     * 
     * @param nodeId 节点id
     */
    public static void setShareByNodeId(String nodeId) {
        switch (Integer.valueOf(nodeId.trim())) {
            case 0:// 通用
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_COMMON);
                break;
            case 1:// 开工阶段
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_START_WORKING);
                break;
            case 2:// 水电阶段
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_WATER);
                break;
            case 3:// 泥木阶段
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_WOOD);
                break;
            case 4:// 油漆阶段
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_PAINT);
                break;
            case 5:// 竣工阶段
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_COMPLETE);
                break;
            default:
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_COMMON);
                break;
        }
    }

    /*
     * 指定平台的一键快捷分享
     */
    public static void ShareToWeiXin(Context context, String url, String platformName) {
        final OnekeyShare oks = new OnekeyShare();
        oks.setNotification(R.drawable.ic_launcher, "");
        oks.setAddress("");
        oks.setTitle("跪求小伙伴帮我再砍10000元！");
        oks.setTitleUrl("");
        oks.setText("我正在惠装528周年砍价团ing，最高能砍10000元！快戳链接来帮我砍价吧！");// 分享内容
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.share);
        String path = ScreenShotUtil.savePic(bitmap, context);
        oks.setImagePath(path);
        oks.setUrl(url);
        oks.setComment("");
        oks.setSite("");
        oks.setSiteUrl("");
        oks.setVenueName("");
        oks.setVenueDescription("");
        oks.setSilent(true);// 一键快捷分享
        oks.setPlatform(platformName);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        // 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
        // oks.addHiddenPlatform(TencentWeibo.NAME);
        // oks.addHiddenPlatform(QQ.NAME);
        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        oks.show(context);
    }

    public static File getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator);
    }
    
    /**
     * 根据投诉节点名称得到投诉节点ID
     * @param name
     * @return
     */
    public static String getIdByComplaintsName(String name){
        List<ComplaintsNode> complaintsNodes = ZxsqApplication.getInstance().getmComplaintsNode();
        for (ComplaintsNode complaintsNode : complaintsNodes) {
            if(name.equals(complaintsNode.getDispute_node_name())){
                return complaintsNode.getDispute_node_id();
            }
        }
        return null;
    }
}
