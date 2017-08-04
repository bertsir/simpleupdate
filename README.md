# simpleupdate
目前支持的平台有Pre.im、豌豆荚、应用宝，超简单实现，调用DownLoadManager做下载，有什么好的建议或者问题，请在下面留言

使用方法：

Pre.im：
```
 SimpleUpdateManager simpleUpdateManager = new SimpleUpdateManager.Builder(this)
                        .setFrom(SimpleUpdateManager.FROM_PRE)
                        .setURL("http://pre.im/arseeds")
                        .setVersionCode(UpdateUtil.getInstance().getVersionCode(this))
                        .setVersionName(UpdateUtil.getInstance().getVersionName(this))
                        .create();
                simpleUpdateManager.check();
````

豌豆荚：

````
SimpleUpdateManager simpleUpdateManager1 = new SimpleUpdateManager.Builder(this)
                        .setFrom(SimpleUpdateManager.FROM_WADOUJIA)
                        .setURL("http://www.wandoujia.com/apps/com.arseeds.zhaojian")
                        .setVersionCode(UpdateUtil.getInstance().getVersionCode(this))
                        .setVersionName(UpdateUtil.getInstance().getVersionName(this))
                        .create();
                simpleUpdateManager1.check();
````

应用宝:

````
 SimpleUpdateManager simpleUpdateManager2 = new SimpleUpdateManager.Builder(this)
                        .setFrom(SimpleUpdateManager.FROM_QQ)
                        .setURL("http://sj.qq.com/myapp/detail.htm?apkName=com.arseeds.zhaojian")
                        .setVersionCode(UpdateUtil.getInstance().getVersionCode(this))
                        .setVersionName(UpdateUtil.getInstance().getVersionName(this))
                        .create();
                simpleUpdateManager2.check();
````

先看效果，没怎么自定义，用的原生Dialog


![download.gif](http://upload-images.jianshu.io/upload_images/3029020-21d8fbd0425e1a90.gif?imageMogr2/auto-orient/strip)

十分轻量，对于想直接依赖应用市场做更新的是不错的选择。

直接使用：
````
compile 'cn.bertsir.simpleupdatelibrary:SimpleUpdateLibrary:1.0.2'
````
