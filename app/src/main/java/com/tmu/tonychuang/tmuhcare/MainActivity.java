package com.tmu.tonychuang.tmuhcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tmu.tonychuang.autoscrollbanner.BannerLayout;
import com.tmu.tonychuang.tmuhcare.G.GS.GSearchActivity;
import com.tmu.tonychuang.tmuhcare.G.G.VisitingGActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private BannerLayout banner;
    private ImageButton waitingBTN;
    private ImageButton visitingBTN;
    private ImageButton searchingBTN;
    private ImageButton hospitalBTN;
    private ImageButton referenceBTN;
    private ImageButton geographyBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBar();
        initView();
        initBanner();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.waitingBTN :
                break;
            case R.id.visitingBTN :
                visitingDislog();
                break;
            case R.id.searchingBTN :
                searchingDislog();
                break;
            case R.id.hospitalBTN :
                break;
            case R.id.referenceBTN :
                break;
            case R.id.geographyBTN :
                break;
            default:
                break;
        }
    }

    private void visitingDislog() {
        final Intent intent = new Intent();
        final List<String> lunch = new ArrayList<>();
        lunch.add(getString(R.string.appointment1));
        lunch.add(getString(R.string.appointment2));
        new AlertDialog.Builder(MainActivity.this)
                .setItems(lunch.toArray(new String[lunch.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
//                                intent.setClass(getActivity(), VisitingActivity.class);
//                                startActivity(intent);
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    private void searchingDislog() {
        final Intent intent = new Intent();
        final List<String> examination = new ArrayList<>();
        examination.add(getString(R.string.examination1));
        examination.add(getString(R.string.examination2));
        examination.add(getString(R.string.examination3));
        examination.add(getString(R.string.examination4));
        examination.add(getString(R.string.examination5));
        new AlertDialog.Builder(this)
                .setItems(examination.toArray(new String[examination.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                intent.setClass(MainActivity.this, VisitingGActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent.setClass(MainActivity.this, GSearchActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    //region 按下返回鍵出現確認離開視窗 onKeyDown, dialog
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {  //確定按下退出鍵and防止重複按下退出鍵
            excDialog();
        }
        return false;
    }

    private void excDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exc_body, null);
        TextView confirmTv = (TextView) dialogView.findViewById(R.id.confirmTv);
        TextView cancelTv = (TextView) dialogView.findViewById(R.id.cancelTv);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        alertDialog.show();

        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                MainActivity.this.finish();
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    //endregion

    private void initBar() {
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

    private void initView() {
        banner = (BannerLayout) findViewById(R.id.banner);
        waitingBTN = (ImageButton) findViewById(R.id.waitingBTN);
        visitingBTN = (ImageButton) findViewById(R.id.visitingBTN);
        searchingBTN = (ImageButton) findViewById(R.id.searchingBTN);
        hospitalBTN = (ImageButton) findViewById(R.id.hospitalBTN);
        referenceBTN = (ImageButton) findViewById(R.id.referenceBTN);
        geographyBTN = (ImageButton) findViewById(R.id.geographyBTN);
        waitingBTN.setOnClickListener(this);
        visitingBTN.setOnClickListener(this);
        searchingBTN.setOnClickListener(this);
        hospitalBTN.setOnClickListener(this);
        referenceBTN.setOnClickListener(this);
        geographyBTN.setOnClickListener(this);
    }

    private void initBanner() {
        List<String> urls = new ArrayList<>();
        urls.add("http://www.telecareservice.org.tw/UploadFile/TW/ADV/20140908221738779_Banner_shadow1.jpg");
        urls.add("http://www.telecareservice.org.tw/UploadFile/TW/ADV/20140908221804998_Banner_shadow2.jpg");
        //网络地址
//        banner.setViewUrls(urls);

        List<Integer> viewRes = new ArrayList<>();
        viewRes.add(R.drawable.banner1);
        viewRes.add(R.drawable.banner2);
        viewRes.add(R.drawable.banner3);
        viewRes.add(R.drawable.banner4);
        //本地资源
        banner.setViewRes(viewRes);
        //添加点击监听
//        banner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


}
