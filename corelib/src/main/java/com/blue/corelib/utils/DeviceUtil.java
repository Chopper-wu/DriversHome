package com.blue.corelib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.blue.corelib.http.HttpInfoHelp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by chopper
 * Created Date 17/4/19 10:20
 * mail:cm1@erongdu.com
 * Describe:  获取Android设备信息的工具类
 */
public class DeviceUtil {
    public static String publicNetIp = "";
    public static String battery = 0+"%";
    public static String alipayPackage = "com.eg.android.AlipayGphone";

    public static void operator(Context context) {
        HttpInfoHelp.INSTANCE.getRequestBody(new HashMap<>());
        System.out.println(
                        "SD卡判断" + isSDCardAvailable() + "\n" +
                        "是否有网" + isNetworkConnected(context) + "\n" +
                        "是否使用wifi网" + isWifi(context) + "\n" +
                        "手机型号" + getPhoneBrand() + "  " + getPhoneModel() + "\n" +
                        "手机版本" + getBuildLevel() + "\n" +
                        "获取设备的唯一标识，deviceId" + getDeviceId(context) + "\n" +
                        "获取wifi name" + getWifiName(context) + "\n" +
                        "mac 地址" + macAddress() + "\n" +
                        "获取IP地址" + getIP(context) + "\n" +
                        "获得可用的内存" + getAvailMemory(context) + "\n" +
                        "获得内存" + getTotalMemory(context) + "\n" +
                        "获得sd存容量" + getSDTotalSize(context) + "\n" +
                        "获得sd可用容量" + getSDAvailableSize(context) + "\n" +
                        "获取IMSI" + getImsi(context) + "\n" +
                        "获取本地dns" + getLocalDNS() + "\n" +
                        "获取androidId" + getAndroidId(context) + "\n" +
                        "获取UUID" + getUUid(context) + "\n" +
                        "当前电量" + battery + "\n" +
                        "获取运营商编号" + getCarrier(context) + "\n" +
                        "获取运营商" + getCarrierName(context) + "\n"

        );
    }

    /**
     * 判断是否使用wifi网络
     *
     * @param mContext
     *
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * SD卡判断
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 是否有网
     *
     * @param context
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 返回版本名字
     * 对应build.gradle中的versionName
     *
     * @param context
     *
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "1.0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo       = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     *
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = "1";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo       = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     *
     * @return
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String deviceId = tm.getDeviceId();
            if (deviceId == null) {
                return "-1";
            } else {
                return deviceId;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "-1";
    }

    /**
     * 获取wifi name
     *
     * @return
     */
    public static String getWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo    = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取当前App进程的id
     *
     * @return
     */
    public static int getAppProcessId() {
        return android.os.Process.myPid();
    }

    /**
     * 获取当前App进程的Name
     *
     * @param context
     * @param processId
     *
     * @return
     */
    public static String getAppProcessName(Context context, int processId) {
        String processName = null;
        ActivityManager am          = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有运行App的进程集合
        List l  = am.getRunningAppProcesses();
        Iterator i  = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == processId) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));

                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                Log.e(DeviceUtil.class.getName(), e.getMessage(), e);
            }
        }
        return processName;
    }

    /**
     * 创建App文件夹
     *
     * @param appName
     * @param application
     *
     * @return
     */
    public static String createAPPFolder(String appName, Application application) {
        return createAPPFolder(appName, application, null);
    }

    /**
     * 创建App文件夹
     *
     * @param appName
     * @param application
     * @param folderName
     *
     * @return
     */
    public static String createAPPFolder(String appName, Application application, String folderName) {
        File root = Environment.getExternalStorageDirectory();
        File folder;
        /**
         * 如果存在SD卡
         */
        if (DeviceUtil.isSDCardAvailable() && root != null) {
            folder = new File(root, appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } else {
            /**
             * 不存在SD卡，就放到缓存文件夹内
             */
            root = application.getCacheDir();
            folder = new File(root, appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        if (folderName != null) {
            folder = new File(folder, folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        return folder.getAbsolutePath();
    }

    /**
     * 通过Uri找到File
     *
     * @param context
     * @param uri
     *
     * @return
     */
    public static File uri2File(Activity context, Uri uri) {
        File file;
        String[] project           = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = context.getContentResolver().query(uri, project, null, null, null);
        if (actualImageCursor != null) {
            int actual_image_column_index = actualImageCursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualImageCursor.moveToFirst();
            String img_path = actualImageCursor
                    .getString(actual_image_column_index);
            file = new File(img_path);
        } else {
            file = new File(uri.getPath());
        }
        if (actualImageCursor != null)
            actualImageCursor.close();
        return file;
    }

    /**
     * 获取AndroidManifest.xml里 的值
     *
     * @param context
     * @param name
     *
     * @return
     */
    public static String getMetaData(Context context, String name) {
        if (context == null || TextUtils.isEmpty(name)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(name);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * 判断手机是否安装某个应用
     * @param context
     * @param appPackageName  应用包名
     * @return   true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 有兴趣的朋友可以看下NetworkInterface在Android FrameWork中怎么实现的
    public static String macAddress() {
        String address = "";
        // 把当前机器上的访问网络接口的存入 Enumeration集合中
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface netWork = interfaces.nextElement();
                // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
                byte[] by = netWork.getHardwareAddress();
                if (by == null || by.length == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (byte b : by) {
                    builder.append(String.format("%02X:", b));
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                String mac = builder.toString();
                Log.d("mac", "interfaceName=" + netWork.getName() + ", mac=" + mac);
                // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
                if (netWork.getName().equals("wlan0")) {
                    Log.d("mac", " interfaceName =" + netWork.getName() + ", mac=" + mac);
                    address = mac;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context context) {
        File path        = Environment.getExternalStorageDirectory();
        StatFs stat        = new StatFs(path.getPath());
        long   blockSize   = stat.getBlockSize();
        long   totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context context) {
        File path            = Environment.getExternalStorageDirectory();
        StatFs stat            = new StatFs(path.getPath());
        long   blockSize       = stat.getBlockSize();
        long   availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context context) {
        File path        = Environment.getDataDirectory();
        StatFs stat        = new StatFs(path.getPath());
        long   blockSize   = stat.getBlockSize();
        long   totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    //获取可用运存大小
    public static String getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        System.out.println("可用内存---->>>" + mi.availMem / (1024 * 1024));
        return Formatter.formatFileSize(context, mi.availMem);
    }

    //获取总运存大小
    public static String getTotalMemory(Context context) {
        String str1           = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long     initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Long.valueOf(arrayOfString[1]).longValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
        System.out.println("总运存--->>>" + initial_memory / (1024 * 1024));
        return Formatter.formatFileSize(context, initial_memory);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static String getRomAvailableSize(Context context) {
        File path            = Environment.getDataDirectory();
        StatFs stat            = new StatFs(path.getPath());
        long   blockSize       = stat.getBlockSize();
        long   availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /** 获取IMSI */
    public static String getImsi(Context context) {
        TelephonyManager tm   = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"HardwareIds", "MissingPermission"}) String imsi = tm.getSubscriberId();
        return imsi;
    }

    /** 获取本地dns */
    public static String getLocalDNS() {
        Process cmdProcess = null;
        BufferedReader reader     = null;
        String dnsIP      = "";
        try {
            cmdProcess = Runtime.getRuntime().exec("getprop net.dns1");
            reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            dnsIP = reader.readLine();
            return dnsIP;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
            cmdProcess.destroy();
        }
    }

    /** 获取androidId */
    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /** 获取UUID */
    @SuppressLint("MissingPermission")
    public static String getUUid(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId   = deviceUuid.toString();
        return uniqueId;
    }

    /** 获取运营商编号 */
    public static String getCarrier(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperator();
    }

    /** 获取运营商名称 */
    public static String getCarrierName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperatorName();
    }
    /** 获取本机手机号码 */
    public static String getPhoneNum(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String num = tm.getLine1Number();
            if (!TextUtils.isEmpty(num) && num.indexOf("+86") != -1) {
                num = num.substring(3, num.length());
            }
            return num;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取IP地址
     */
    public static String getIP(Context context) {
        if(!"".equals(publicNetIp) && null != publicNetIp){
            return publicNetIp;
        }
        int    WIFI_IP = getWIFIIP(context);
        String GPRS_IP = getGPRSIP();
        String ip      = "0.0.0.0";
        if (WIFI_IP != 0) {
            ip = intToIP(WIFI_IP);
        } else if (!TextUtils.isEmpty(GPRS_IP)) {
            ip = GPRS_IP;
        }
        return ip;
    }


    /**
     * 获取外网的IP(必须放到子线程里处理)
     */
    public static String getNetIp() {
        InputStream inStream = null;
        try {
            URL infoUrl        = new URL("http://1212.ip138.com/ic.asp");
            URLConnection connection     = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int               responseCode   = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader  = new BufferedReader(new InputStreamReader(inStream, "gb2312"));
                StringBuilder builder = new StringBuilder();
                String line    = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    //builder.append(line).append("\n");
                }
                inStream.close();
                int start = builder.indexOf("[");
                int end   = builder.indexOf("]");
                publicNetIp = builder.substring(start + 1, end);
                return publicNetIp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得wifi的IP地址
     */
    private static int getWIFIIP(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
            return 0;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getIpAddress();
    }

    /**
     * 整型IP地址转成String的
     */
    private static String intToIP(int IPAddress) {
        return (IPAddress & 0xFF) + "." + ((IPAddress >> 8) & 0xFF) + "." + ((IPAddress >> 16) & 0xFF) + "." + (IPAddress >> 24 & 0xFF);
    }

    /**
     * 获取数据网络的IP地址
     */
    private static String getGPRSIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
