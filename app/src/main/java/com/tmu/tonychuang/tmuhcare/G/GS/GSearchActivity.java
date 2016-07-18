package com.tmu.tonychuang.tmuhcare.G.GS;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tmu.tonychuang.tmuhcare.R;
import com.tmu.tonychuang.tmuhcare.Z_Other.CustomTabWebPage.CustomTabActivityHelper;
import com.tmu.tonychuang.tmuhcare.Z_Other.JSON.JsonParser;
import com.tmu.tonychuang.tmuhcare.Z_Other.JSON.WebAPI;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ExamData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ExamisCancelData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.QisWriteData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.SelfData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyModal.MyDateSFormat;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyModal.MyValidator;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyUI.MySyncingDialog;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyUI.MyToast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.grantland.widget.AutofitHelper;

public class GSearchActivity extends AppCompatActivity implements View.OnClickListener {

    //Android2.2版本以後的URL，之前的就不寫了
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    private ActionBar actionBar;

    private RadioGroup PidRG;
    private RadioButton TWRB;
    private RadioButton OSRB;
    private MaterialEditText PidET;
    private Button nextBTN;
    private LinearLayout ExamDataLayout;
    private ListView ExamDataLV;
    private TextView noExamDataTV;
    private GsearchLVAdapter gsearchLVAdapter;
    private InputMethodManager imm;
    private MySyncingDialog mySyncingDialog;

    //FucGetExamData
    private String isTaiwan = "";
    private String id = "";
    private SelfData selfData = SelfData.getSelf();
    private ArrayList<ExamData> examDatas = new ArrayList<>();

    //CALL
    private final static String CALL = "android.intent.action.CALL";

    //FucGetExamisCancel
    private String app_no = "";
    private String opd_date = "";
    private int selectPostion = 0; //要操作的listview index ( = examdata index)

    //FucGetQisWrite
    private boolean changeQisFlag = false;

    //Delete CalendarEvent
    private String ProjectName = "";
    private String eventDateStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsearch);

        initBar();
        initView();
        initListener();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        nextBTN.setEnabled(false);
        ExamDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextBTN:
                imm.hideSoftInputFromWindow(PidET.getWindowToken(), 0);
                String pidStr = PidET.getText().toString();
                PidRG.clearCheck();
                PidET.setText(pidStr);
                PidET.setEnabled(false);
                id = PidET.getText().toString().trim().toUpperCase();
                selfData.setId(id);
                selfData.setIsTaiwan(isTaiwan);
                initExamDatas();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        imm.hideSoftInputFromWindow(PidET.getWindowToken(), 0);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //填完問卷後更新問卷填寫狀態
        if (changeQisFlag) {
            initQisWriteData(); //取得問卷填寫狀態，更新問卷填寫狀態，更新到examdata
            changeQisFlag = false;
        }
    }

    private void initQisWriteData() {
        new AsyncTask<String, Void, QisWriteData>() {
            @Override
            protected QisWriteData doInBackground(String... params) {
                QisWriteData qisWriteData = new QisWriteData();
                try {
                    String s;
                    s = WebAPI.FucGetQisWrite(params[0], params[1], params[2], params[3]);
                    qisWriteData = JsonParser.parseQisWriteData(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return qisWriteData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(QisWriteData qisWriteData) {
                super.onPostExecute(qisWriteData);
                mySyncingDialog.dismiss();
                if (qisWriteData.getErrMsg().equals("null")) {
                    if (qisWriteData.getDWrite().equals("Y")) {
                        examDatas.get(selectPostion).setQuestionnaire_Write("Y");
                        gsearchLVAdapter.notifyDataSetChanged(); //填完問卷後更新畫面
                        MyToast myToast = new MyToast(GSearchActivity.this, getResources().getString(R.string.finishQisStr));
                    } else {
                        MyToast myToast = new MyToast(GSearchActivity.this, getResources().getString(R.string.unfinishQisStr));
                    }
                } else {
                    MyToast myToast = new MyToast(GSearchActivity.this, getResources().getString(R.string.getDataErrorMsg));
                }
            }
        }.execute(isTaiwan, id, app_no, opd_date);
    }

    private void initExamDatas() {
        new AsyncTask<String, Void, ArrayList<ExamData>>() {
            @Override
            protected ArrayList<ExamData> doInBackground(String... params) {
                try {
                    String s;
                    s = WebAPI.FucGetExamData(params[0], params[1]);
                    examDatas = JsonParser.parseExamData(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return examDatas;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(ArrayList<ExamData> examDatas) {
                super.onPostExecute(examDatas);
                if (examDatas != null) {
                    //將預約資料顯示出來
                    ExamDataLayout.setVisibility(View.VISIBLE);
                    initListview();
                    nextBTN.setVisibility(View.GONE);
                    mySyncingDialog.dismiss();
                } else {
                    mySyncingDialog.dismiss();
                    MyToast myToast = new MyToast(GSearchActivity.this, getResources().getString(R.string.getDataErrorMsg));
                }
            }
        }.execute(isTaiwan, id);
    }

    private void initListview() {
        gsearchLVAdapter = new GsearchLVAdapter(this, examDatas);
        ExamDataLV.setEmptyView(noExamDataTV);
        ExamDataLV.setAdapter(gsearchLVAdapter);
        ExamDataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView examNameTV = (TextView) view.findViewById(R.id.examNameTV);
                ExamData examData = examDatas.get(position);
                editExamDataDialog(position);
                app_no = examData.getAPP_NO();
                opd_date = examData.getOPD_DATE();
                selectPostion = position;
            }
        });
    }

    private void editExamDataDialog(int position) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_examdata, null);
        final TextView TitleTV = (TextView) dialogView.findViewById(R.id.TitleTV);
        final Button qisBTN = (Button) dialogView.findViewById(R.id.qisBTN);
        final Button cancelBTN = (Button) dialogView.findViewById(R.id.cancelBTN);
        final TextView noCanaelTV = (TextView) dialogView.findViewById(R.id.noCanaelTV);
        final TextView closeTV = (TextView) dialogView.findViewById(R.id.closeTV);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setOnKeyListener(getOnKeyListener())
                .setCancelable(false).create();

        final ExamData examData = examDatas.get(position);
        String titleStr = examData.getAPP_NO_NAME();
        int wrapIndex = titleStr.indexOf("(");
        if (wrapIndex != -1 && wrapIndex != 0) {
            titleStr = titleStr.substring(0, wrapIndex) + "\n" + titleStr.substring(wrapIndex);
        }

        ProjectName = titleStr; //調整成與event相同的title格式
        eventDateStr = examData.getOPD_DATE();

        AutofitHelper.create(TitleTV);
        TitleTV.setText(titleStr);

        if (examData.getQuestionnaire().equals("Y")) {
            qisBTN.setVisibility(View.VISIBLE);
            if (examData.getQuestionnaire_Write().equals("Y")) {
                qisBTN.setEnabled(false);
                qisBTN.setText("已填寫完畢");
            } else {
                qisBTN.setEnabled(true);
                qisBTN.setText("填寫問卷");
            }
        } else {
            qisBTN.setVisibility(View.GONE);
        }
        qisBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeQis();
            }
        });

        noCanaelTV.setText("請來電取消此次預約");
        AutofitHelper.create(noCanaelTV);
        if (examData.getCustomer_Exam_No().equals("")) {
            noCanaelTV.setVisibility(View.GONE);
            cancelBTN.setText("取消預約");
        } else {
            noCanaelTV.setVisibility(View.VISIBLE);
            cancelBTN.setText("致電健檢中心");
        }
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (examData.getCustomer_Exam_No().equals("")) {
                    ensureCancel(alertDialog);
                } else {
                    MyToast myToast = new MyToast(GSearchActivity.this, getResources().getString(R.string.callingStr));
                    Intent call = new Intent(CALL, Uri.parse("tel:" + "0227372181,,8325"));
                    startActivity(call);
                    alertDialog.dismiss();
                }
            }

            private void ensureCancel(final AlertDialog alertDialog) {
                View dialogView = LayoutInflater.from(GSearchActivity.this).inflate(R.layout.dialog_ensure_cancel, null);
                final TextView TitleTV = (TextView) dialogView.findViewById(R.id.TitleTV);
                final TextView bodyTV = (TextView) dialogView.findViewById(R.id.bodyTV);
                final TextView cancelTv = (TextView) dialogView.findViewById(R.id.cancelTv);
                final TextView confirmTv = (TextView) dialogView.findViewById(R.id.confirmTv);

                final AlertDialog CancelAlertDialog = new AlertDialog.Builder(GSearchActivity.this)
                        .setView(dialogView)
                        .setOnKeyListener(getOnKeyListener())
                        .setCancelable(false).create();

                TitleTV.setText("取消預約");
                Date examdate = new Date();
                try {
                    examdate = MyDateSFormat.getFrmt_yyyyMMdd().parse(examData.getOPD_DATE());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String examdateStr = MyDateSFormat.getFrmt_yMd().format(examdate);
                String bodyStr = "確定要取消 \n\n\"" + examdateStr + "\" 進行的 \n\"" + examData.getAPP_NO_NAME() + "\"\n\n專案嗎?";
                bodyTV.setText(bodyStr);
                cancelTv.setText("不取消");
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CancelAlertDialog.dismiss();
                        if (examData.getQuestionnaire().equals("N")) {
                            alertDialog.dismiss();
                        }
                    }
                });
                confirmTv.setText("取消預約");
                confirmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelExam(alertDialog);
                        CancelAlertDialog.dismiss();
                    }
                });

                CancelAlertDialog.show();
            }
        });

        closeTV.setText("關閉");
        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void writeQis() {
        String url = examDatas.get(selectPostion).getQuestionnaireURL();
//        String url = "http://goo.gl/forms/uyylAHEJFN54E0742";
        if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0) {
            changeQisFlag = true;
            Uri uri = Uri.parse(url);
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(this,  R.color.colorPrimary)).setShowTitle(true);
            builder.setCloseButtonIcon(
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
            CustomTabsIntent customTabsIntent = builder.build();
            CustomTabActivityHelper.openCustomTab(this, customTabsIntent, uri,
                    new CustomTabActivityHelper.CustomTabFallback() {
                        @Override
                        public void openUri(Activity activity, Uri uri) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
        } else {
            MyToast myToast = new MyToast(this, "問卷網址錯誤，請聯繫健檢中心");
        }

//        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
//                .setShowTitle(true)
//                .setToolbarColor(ContextCompat.getColor(this,  R.color.colorPrimary))
//                .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
//                .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//                .setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back))
//                .build();
//        // 啟動 chrome
//        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void cancelExam(final AlertDialog alertDialog) {
        new AsyncTask<String, Void, ExamisCancelData>() {
            @Override
            protected ExamisCancelData doInBackground(String... params) {
                ExamisCancelData examisCancelData = new ExamisCancelData();
                try {
                    String s;
                    s = WebAPI.FucGetExamisCancel(params[0], params[1], params[2], params[3]);
                    examisCancelData = JsonParser.parseExamisCancelData(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return examisCancelData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(ExamisCancelData examisCancelData) {
                super.onPostExecute(examisCancelData);
                if (examisCancelData.getErrMsg().equals("null")) {
                    examDatas.remove(selectPostion);
                    gsearchLVAdapter.notifyDataSetChanged();
                    deleteCalendarEvent();
                    mySyncingDialog.dismiss();
                    alertDialog.dismiss();
                    MyToast myToast = new MyToast(GSearchActivity.this, getResources().getString(R.string.cancelExamMsg));
                } else {
                    mySyncingDialog.dismiss();
                    MyToast myToast = new MyToast(GSearchActivity.this, getResources().getString(R.string.getDataErrorMsg));
                }
            }
        }.execute(isTaiwan, id, app_no, opd_date);
    }

    private DialogInterface.OnKeyListener getOnKeyListener() {
        return new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return false;
            }
        };
    }

    private void deleteCalendarEvent() {
        Date eventDate = new Date();
        try {
            eventDate = MyDateSFormat.getFrmt_yyyyMMdd().parse(eventDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar eventCal = Calendar.getInstance();
        eventCal.setTime(eventDate);
        eventCal.set(Calendar.HOUR_OF_DAY, 8);
        eventCal.set(Calendar.MINUTE, 0);
        long start = eventCal.getTime().getTime();
        eventCal.set(Calendar.HOUR_OF_DAY, 12);
        eventCal.set(Calendar.MINUTE, 0);
        long end = eventCal.getTime().getTime();

        int rownum = getContentResolver().delete(Uri.parse(calanderEventURL), CalendarContract.Events.TITLE + " =? AND " +
                CalendarContract.Events.DTSTART + " =? AND " +
                CalendarContract.Events.DTEND + " =?", new String[]{ProjectName, String.valueOf(start), String.valueOf(end)});
    }

    private void initListener() {
        nextBTN.setOnClickListener(this);

        //PidET
        PidRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PidET.setEnabled(true);
                PidET.setText("");
                nextBTN.setEnabled(false);
                ExamDataLayout.setVisibility(View.GONE);
                imm.showSoftInput(PidET,InputMethodManager.SHOW_FORCED); //顯示鍵盤
                nextBTN.setVisibility(View.VISIBLE);
                switch (checkedId) {
                    case R.id.TWRB:
                        isTaiwan = "Y";
                        PidET.setHint("身分證字號");
                        PidET.setMaxCharacters(10);
                        break;
                    case R.id.OSRB:
                        isTaiwan = "N";
                        PidET.setHint("居留證號碼");
                        PidET.setMaxCharacters(10);
                        break;
                    default:
                        break;
                }
            }
        });
        PidET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (PidRG.getCheckedRadioButtonId()) {
                    case R.id.TWRB:
                        if (!TextUtils.isEmpty(PidET.getText())
                                && MyValidator.isValidTWPID(PidET.getText().toString())) {
                            nextBTN.setEnabled(true);
                        } else {
                            nextBTN.setEnabled(false);
                        }
                        break;
                    case R.id.OSRB:
                        if (!TextUtils.isEmpty(PidET.getText()) && PidET.getText().length() == 10) {
                            nextBTN.setEnabled(true);
                        } else {
                            nextBTN.setEnabled(false);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        PidRG = (RadioGroup) findViewById(R.id.PidRG);
        TWRB = (RadioButton) findViewById(R.id.TWRB);
        OSRB = (RadioButton) findViewById(R.id.OSRB);
        PidET = (MaterialEditText) findViewById(R.id.PidET);
        PidET.setEnabled(false);
        nextBTN = (Button) findViewById(R.id.nextBTN);
        ExamDataLayout = (LinearLayout) findViewById(R.id.ExamDataLayout);
        ExamDataLV = (ListView) findViewById(R.id.ExamDataLV);
        noExamDataTV = (TextView) findViewById(R.id.noExamDataTV);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mySyncingDialog = new MySyncingDialog(false, this, getResources().getString(R.string.waitingDataStr));
    }

    private void initBar() {
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);
        actionBar.setTitle("健檢預約查詢");
    }
}
