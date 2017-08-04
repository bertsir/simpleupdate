package cn.bertsir.simpleupdatelibrary.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Bert on 2017/8/4.
 */

public class UpdateUtil {

    private static UpdateUtil instance;


    public synchronized static UpdateUtil getInstance() {
        if(instance == null)
            instance = new UpdateUtil();
        return instance;
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public int getVersionCode(Context mContext) {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    /**
     * 获取版本名
     * @return 当前应用的版本号
     */
    public Double getVersionName(Context mContext) {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return Double.valueOf(version);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("UpdateUtil", "getVersionName: "+e.toString() );
            return 0.0;
        }
    }


    /**
     * 下载安装包
     * @param mContext
     * @param uri
     * @param save
     * @param title
     * @param description
     * @return
     */
    public long startDownload(Context mContext,String uri,String save, String title, String description) {
        DownloadManager dm = (DownloadManager)mContext.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));

        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //req.setAllowedOverRoaming(false);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.setDestinationInExternalFilesDir(mContext, save, "update.apk");
        // 设置一些基本显示信息
        req.setTitle(title);
        req.setDescription(description);
        req.setMimeType("application/vnd.android.package-archive");
        //加入下载队列
        return dm.enqueue(req);
    }

    /**
     * 时间戳转日期
     * @param s
     * @return
     */
    public String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

}
