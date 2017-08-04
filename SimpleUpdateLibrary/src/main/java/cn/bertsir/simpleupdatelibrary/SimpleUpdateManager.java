package cn.bertsir.simpleupdatelibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import cn.bertsir.simpleupdatelibrary.utils.HtmlUtils;
import cn.bertsir.simpleupdatelibrary.utils.UpdateUtil;

/**
 * Created by Bert on 2017/8/4.
 */

public class SimpleUpdateManager {

    private static final String TAG = "SimpleUpdateManager";

    public static int FROM_PRE = 1;
    public static int FROM_WADOUJIA = 2;
    public static int FROM_QQ = 3;


    private Context mContext;
    private int FROM_CURRENT = FROM_PRE;
    private String check_url = "";
    private int Current_VersionCode = 0;
    private Double Current_VersionName= 0.0;
    private String Save_Apk = Environment.DIRECTORY_DOWNLOADS;

    private String urlSource;
    private int url_VersionCode = 0;
    private String url_VersionName = "0";
    private String url_VersionTime = "0";
    private String url_VersionLog;
    private String url_VersionDownURL;

    private static long downloadApkId;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(FROM_CURRENT == FROM_PRE){
                        getPreMsg();
                    }else if(FROM_CURRENT == FROM_WADOUJIA){
                        getWanDouJiaMsg();
                    }else if(FROM_CURRENT == FROM_QQ){
                        getQQMsg();
                    }
                    break;
            }
        }
    };



    public void check(){
        getHtml();
    }

    private void getHtml(){
        new Thread(new Runnable() {
            @Override
            public void run() {
        try {
                    urlSource = HtmlUtils.getInstance().getURLSource(check_url);
                    mHandler.sendEmptyMessage(1);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "获取版本信息失败！"+e.toString() );
        }
            }

        }).start();
    }

    /**
     * 获取Pre.im信息
     */
    private void getPreMsg(){
        //截取相关信息
        String versionMsg = HtmlUtils.getInstance().getHtmlMsg(urlSource, "当前版本:\n" +
                "                    <em>\n" +
                "                    ", "                    </em>\n" +
                "                </span>");
        String[] versionArray = versionMsg.split("\\(Build ");
        if(versionArray.length>0){
            url_VersionName = versionArray[0];
            url_VersionCode = Integer.valueOf(versionArray[1].replace(")","").trim());
        }
        url_VersionTime = HtmlUtils.getInstance().getHtmlMsg(urlSource,"更新于: "," </span>\n" +
                "            </p>");
        url_VersionDownURL = HtmlUtils.getInstance().getHtmlMsg(urlSource,"\"down2\" href=\"","\" onClick=\"ajaxTj");
        url_VersionLog = HtmlUtils.getInstance().getHtmlMsg(urlSource,"Update_log_content\">","</p>");

        //判断是否更新
        if(url_VersionCode > Current_VersionCode ){
            showUpdateDialog();
        }
    }

    /**
     * 获取豌豆荚信息
     */
    private void getWanDouJiaMsg() {
        url_VersionName = HtmlUtils.getInstance().getHtmlMsg(urlSource,"data-app-vname=\"","\" data-app-icon");
        url_VersionCode = Integer.parseInt(HtmlUtils.getInstance().getHtmlMsg(urlSource,"data-app-vcode=\"","\" data-app-vname"));
        url_VersionTime = HtmlUtils.getInstance().getHtmlMsg(urlSource,"datePublished\" datetime=\"","\">");
        String[] splitCheck = check_url.split("\\/");
        String packageName = splitCheck[splitCheck.length-1];
        url_VersionDownURL = "http://www.wandoujia.com/apps/"+packageName+"/download?pos=detail-ndownload-"+packageName;
        url_VersionLog = HtmlUtils.getInstance().getHtmlMsg(urlSource,"<div data-originheight=\"100\" " +
                "class=\"con\">","</div>");
        url_VersionLog = url_VersionLog.replace("<br>","\n");
//        Log.e(TAG, "getWanDouJiaMsg: "+url_VersionName+"--"+url_VersionCode+"---"+url_VersionTime
//                +"---"+url_VersionDownURL+"----"+url_VersionLog);
        //判断是否更新
        if(url_VersionCode > Current_VersionCode ){
            showUpdateDialog();
        }
    }


    /**
     * 获取应用宝信息
     * @return
     */
    private void getQQMsg(){
        url_VersionDownURL = HtmlUtils.getInstance().getHtmlMsg(urlSource,"data-apkUrl=\"","\" appname=");
        String[] versionArray = url_VersionDownURL.split("_");
        url_VersionName = versionArray[1];
        url_VersionCode = Integer.parseInt(versionArray[versionArray.length-1].split("\\.")[0]);
        url_VersionTime = UpdateUtil.getInstance().stampToDate(HtmlUtils.getInstance().getHtmlMsg(urlSource,
                "data-apkPublishTime=\"","\"></div")+"000");
        String log_temp = HtmlUtils.getInstance().getHtmlMsg(urlSource,"更新内容","det-intro-showmore");
        url_VersionLog = HtmlUtils.getInstance().getHtmlMsg(log_temp,"det-app-data-info\">","</div>").replace
                ("</br>","\n");

        //判断是否更新
        if(url_VersionCode > Current_VersionCode ){
            showUpdateDialog();
        }
//        Log.e(TAG, "getQQMsg: "+url_VersionName+"--"+url_VersionCode+"---"+url_VersionTime
//                +"---"+url_VersionDownURL+"----"+url_VersionLog);
    }




    public static long getdownloadApkId(){
        return downloadApkId;
    }

    /**
     * 提示Dialog
     */
    private void showUpdateDialog(){
        String verMsg;
        if(TextUtils.isEmpty(url_VersionLog)){
            verMsg = "versionName:"+url_VersionName+"\n"+"更新时间："+url_VersionTime;
        }else {
            verMsg = "versionName:"+url_VersionName+"\n"+"更新时间："+url_VersionTime+"\n"+"更新日志：\n"+url_VersionLog;
        }

        AlertDialog.Builder builder = new AlertDialog
                .Builder(mContext);
        builder.setTitle("发现新版本");
        builder.setMessage(verMsg);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "onClick: "+ url_VersionDownURL);
                downloadApkId = UpdateUtil.getInstance().startDownload(mContext, url_VersionDownURL, Save_Apk, "正在下载更新", "请稍后");
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    public static class Builder{
        private SimpleUpdateManager watcher;

        public Builder(Context mContext){
            watcher = new SimpleUpdateManager();
            watcher.mContext = mContext;
        }

        public Builder setFrom(int from) {
            watcher.FROM_CURRENT = from;
            return this;
        }

        public Builder setURL(String url){
            watcher.check_url = url;
            return this;
        }

        public Builder setVersionCode(int code){
            watcher.Current_VersionCode = code;
            return this;
        }

        public Builder setVersionName(double name){
            watcher.Current_VersionName = name;
            return this;
        }

        public Builder setSaveUrl(String url){
            watcher.Save_Apk = url;
            return this;
        }

        public SimpleUpdateManager create(){
            return watcher;
        }
    }

}
