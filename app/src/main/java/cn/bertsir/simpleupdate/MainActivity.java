package cn.bertsir.simpleupdate;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import cn.bertsir.simpleupdatelibrary.SimpleUpdateManager;
import cn.bertsir.simpleupdatelibrary.utils.UpdateUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView wv;
    private Button bt_pre;
    private Button bt_wandoujia;
    private Button bt_qq;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        bt_pre = (Button) findViewById(R.id.bt_pre);
        bt_wandoujia = (Button) findViewById(R.id.bt_wandoujia);
        bt_qq = (Button) findViewById(R.id.bt_qq);

        bt_pre.setOnClickListener(this);
        bt_wandoujia.setOnClickListener(this);
        bt_qq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pre:
                SimpleUpdateManager simpleUpdateManager = new SimpleUpdateManager.Builder(this)
                        .setFrom(SimpleUpdateManager.FROM_PRE)
                        .setURL("http://pre.im/arseeds")
                        .setVersionCode(UpdateUtil.getInstance().getVersionCode(this))
                        .setVersionName(UpdateUtil.getInstance().getVersionName(this))
                        .create();
                simpleUpdateManager.check();
                break;
            case R.id.bt_wandoujia:
                SimpleUpdateManager simpleUpdateManager1 = new SimpleUpdateManager.Builder(this)
                        .setFrom(SimpleUpdateManager.FROM_WADOUJIA)
                        .setURL("http://www.wandoujia.com/apps/com.arseeds.zhaojian")
                        .setVersionCode(UpdateUtil.getInstance().getVersionCode(this))
                        .setVersionName(UpdateUtil.getInstance().getVersionName(this))
                        .create();
                simpleUpdateManager1.check();
                break;
            case R.id.bt_qq:
                SimpleUpdateManager simpleUpdateManager2 = new SimpleUpdateManager.Builder(this)
                        .setFrom(SimpleUpdateManager.FROM_QQ)
                        .setURL("http://sj.qq.com/myapp/detail.htm?apkName=com.arseeds.zhaojian")
                        .setVersionCode(UpdateUtil.getInstance().getVersionCode(this))
                        .setVersionName(UpdateUtil.getInstance().getVersionName(this))
                        .create();
                simpleUpdateManager2.check();
                break;
        }
    }
}
