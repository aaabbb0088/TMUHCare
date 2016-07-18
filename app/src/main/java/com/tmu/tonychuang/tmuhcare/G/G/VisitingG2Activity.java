package com.tmu.tonychuang.tmuhcare.G.G;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tmu.tonychuang.tmuhcare.R;
import com.tmu.tonychuang.tmuhcare.Z_Other.CustomTabWebPage.CustomTabActivityHelper;
import com.tmu.tonychuang.tmuhcare.Z_Other.JSON.JsonParser;
import com.tmu.tonychuang.tmuhcare.Z_Other.JSON.WebAPI;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ExamDateData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ProjectData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ProjectForCompanyData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.SelfData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.VIDRData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.WriteExamData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyModal.FilterListViewAdapter;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyModal.MyDateSFormat;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyUI.MySyncingDialog;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyUI.MyToast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import me.grantland.widget.AutofitHelper;

public class VisitingG2Activity extends AppCompatActivity implements View.OnClickListener {

    //Android2.2版本以後的URL，之前的就不寫了
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    private ActionBar actionBar;
    public static Button nextBTN;
    private Context mContext;
    private TableRow t1;
    private TableRow t2;
    private TableRow t3;
    private RadioGroup PCRG;
    private RadioButton PRB;
    private RadioButton CRB;
    private TextView PProjectTV;
    private TextView CompanyNameTV;
    private TextView CProjectTV;
    public static TextView DateTV;

    private MySyncingDialog mySyncingDialog;
    private VIDRData vidrdata = VIDRData.getSelf();
    private ProjectData projectData = ProjectData.getSelf();

    private String ProjectName = "";
    private String ProjectCode = "";
    private static Date PreExamDate = new Date();

    //FucGetExamDate
    private ExamDateData examDateData = ExamDateData.getSelf();

    //id紀錄
    private SelfData selfData = SelfData.getSelf();

    //FucWriteExamData
    private WriteExamData writeExamData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_g2);

        mContext = this.getApplicationContext();
        initBar();
        initView();
        initViewData();
        initListener();

        PRB.setChecked(true);
        t2.setVisibility(View.GONE);
        t3.setVisibility(View.GONE);

        DateTV.setEnabled(false);
        nextBTN.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PProjectTV:
                PProjectDialog();
                break;
            case R.id.CProjectTV:
                CProjectDialog();
                break;
            case R.id.nextBTN:
                //完成預約時，使用alert提示
                initWriteExamData();
                break;
            case R.id.DateTV:
                DateChoose();
                break;
            default:
                break;
        }
    }

    private void PProjectDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_choose_company, null);
        final TextView TitleTV = (TextView) dialogView.findViewById(R.id.TitleTV);
        final MaterialEditText KeyWordET = (MaterialEditText) dialogView.findViewById(R.id.KeyWordET);
        final ListView CompanyLV = (ListView) dialogView.findViewById(R.id.CompanyLV);
        final TextView NoCompanyTV = (TextView) dialogView.findViewById(R.id.NoCompanyTV);
        final InputMethodManager imm = (InputMethodManager) dialogView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        TitleTV.setText("選擇個人健檢方案");
        KeyWordET.setHint("關鍵字 (空白分隔條件)");
        NoCompanyTV.setText("取消");

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setOnKeyListener(getOnKeyListener())
                .setCancelable(false).create();

        final FilterListViewAdapter companyAdapter = new FilterListViewAdapter(VisitingG2Activity.this, projectData.getProjectName(), false);
        CompanyLV.setAdapter(companyAdapter);

        CompanyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView PProjectNameTV = (TextView) view.findViewById(R.id.tv_name);
                ProjectName = PProjectNameTV.getText().toString();
                String PName = ProjectName.replace("\n", "");
                ProjectCode = projectData.getPCodeNameMap().get(PName);
                initExamDateData();

                PProjectTV.setText(ProjectName);

                alertDialog.dismiss();
            }
        });
        CompanyLV.setTextFilterEnabled(true);

        CompanyLV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    KeyWordET.clearFocus();
                    imm.hideSoftInputFromWindow(KeyWordET.getWindowToken(), 0);
                }
                return false;
            }
        });

        KeyWordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                companyAdapter.getFilter().filter(s);

                //只能判斷字首情況
//                if (s.toString().trim().equals("")) {
//                    CompanyLV.clearTextFilter();
//                } else {
//                    CompanyLV.setFilterText(s.toString());
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NoCompanyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void CProjectDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_choose_company, null);
        final TextView TitleTV = (TextView) dialogView.findViewById(R.id.TitleTV);
        final MaterialEditText KeyWordET = (MaterialEditText) dialogView.findViewById(R.id.KeyWordET);
        final ListView CompanyLV = (ListView) dialogView.findViewById(R.id.CompanyLV);
        final TextView NoCompanyTV = (TextView) dialogView.findViewById(R.id.NoCompanyTV);
        final InputMethodManager imm = (InputMethodManager) dialogView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        TitleTV.setText("選擇特約健檢方案");
        KeyWordET.setHint("關鍵字 (空白分隔條件)");
        NoCompanyTV.setText("取消");

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setOnKeyListener(getOnKeyListener())
                .setCancelable(false).create();

        final FilterListViewAdapter companyAdapter = new FilterListViewAdapter(VisitingG2Activity.this, vidrdata.getPName(), false);
        CompanyLV.setAdapter(companyAdapter);

        CompanyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView CProjectNameTV = (TextView) view.findViewById(R.id.tv_name);
                ProjectName = CProjectNameTV.getText().toString();
                String PName = ProjectName.replace("\n", "");
                ProjectCode = vidrdata.getPCNMap().get(PName);
                initExamDateData();

                CProjectTV.setText(ProjectName);

                alertDialog.dismiss();
            }
        });
        CompanyLV.setTextFilterEnabled(true);

        CompanyLV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    KeyWordET.clearFocus();
                    imm.hideSoftInputFromWindow(KeyWordET.getWindowToken(), 0);
                }
                return false;
            }
        });

        KeyWordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                companyAdapter.getFilter().filter(s);

                //只能判斷字首情況
//                if (s.toString().trim().equals("")) {
//                    CompanyLV.clearTextFilter();
//                } else {
//                    CompanyLV.setFilterText(s.toString());
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NoCompanyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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

    private void initExamDateData() {
        new AsyncTask<String, Void, ExamDateData>() {
            @Override
            protected ExamDateData doInBackground(String... params) {
                try {
                    String s;
                    s = WebAPI.FucGetExamDate(params[0], params[1], params[2]);
                    examDateData = JsonParser.parseExamDate(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return examDateData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(ExamDateData examDateData) {
                super.onPostExecute(examDateData);
                if (examDateData.getErrMsg().equals("null")) {
                    DateTV.setEnabled(true);
                    DateTV.setHint("點擊選擇日期");
                    DateTV.setBackgroundResource(R.drawable.bg_g2_selector);
                    DateTV.setText("");
                    mySyncingDialog.dismiss();
                } else {
                    mySyncingDialog.dismiss();
                    MyToast myToast = new MyToast(VisitingG2Activity.this, getResources().getString(R.string.getDataErrorMsg));
                }
            }
        }.execute(selfData.getId(), vidrdata.getECFLAG(), ProjectCode);
    }

    private void DateChoose() {
        new SimpleCalendarDialogFragment().show(getSupportFragmentManager(), "test-simple-calendar");
    }

    private void initWriteExamData() {
        new AsyncTask<String, Void, WriteExamData>() {
            @Override
            protected WriteExamData doInBackground(String... params) {
                writeExamData = new WriteExamData();
                try {
                    String s;
                    s = WebAPI.FucWriteExamData(params[0], params[1], params[2], params[3]);
                    writeExamData = JsonParser.parseWriteExamData(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return writeExamData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(WriteExamData writeExamData) {
                super.onPostExecute(writeExamData);
                if (writeExamData.getDWrite().equals("Y")) {
                    mySyncingDialog.dismiss();
                    writeCalendarEvent();
                    WriteExamDateFinshDialog();
                } else {
                    mySyncingDialog.dismiss();
                    if (writeExamData.getErrMsg().equals("此客人已預約過同日不可在預約")) {
                        Toast.makeText(VisitingG2Activity.this, getResources().getString(R.string.getDataErrorMsg_oneday), Toast.LENGTH_LONG).show();
                    } else {
                        MyToast myToast = new MyToast(VisitingG2Activity.this, getResources().getString(R.string.getDataErrorMsg));
                    }
                }
            }
        }.execute(selfData.getIsTaiwan(), selfData.getId(), ProjectCode, MyDateSFormat.getFrmt_yyyyMMdd().format(PreExamDate));
    }

    private void WriteExamDateFinshDialog() {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_write_examdata_finsh, null);
        final TextView TitleTV = (TextView) dialogView.findViewById(R.id.TitleTV);
        final TextView examDataTV = (TextView) dialogView.findViewById(R.id.examDataTV);
        final TextView PSTV = (TextView) dialogView.findViewById(R.id.PSTV);
        final LinearLayout QisLayout = (LinearLayout) dialogView.findViewById(R.id.QisLayout);
        final LinearLayout NQisLayout = (LinearLayout) dialogView.findViewById(R.id.NQisLayout);
        final TextView WriteQisTV = (TextView) dialogView.findViewById(R.id.WriteQisTV);
        final TextView NoWriteQisTV = (TextView) dialogView.findViewById(R.id.NoWriteQisTV);
        final TextView IKnowTV = (TextView) dialogView.findViewById(R.id.IKnowTV);

        TitleTV.setText("您已完成健檢預約");
        String ProjectNameStr = ProjectName;
        if (ProjectNameStr.length() < 15) {
            ProjectNameStr = ProjectNameStr.replace("\n", "");
        }
        String examDataTVStr = "預約項目名稱 : \n" + ProjectNameStr + "\n\n" +
                "預約健檢日期 : \n" + MyDateSFormat.getFrmt_yMd().format(PreExamDate);
        examDataTV.setText(examDataTVStr);
        String PSTvStr = "P.S. 本健檢項目需填寫檢前問卷\n" +
                "     \"" + examDateData.getQuestionnaireName() + "\"\n" +
                "     稍後可至健檢預約查詢頁面填寫";
        PSTV.setText(PSTvStr);
        WriteQisTV.setText("現在填寫");
        NoWriteQisTV.setText("稍後填寫");
        IKnowTV.setText("我知道了");

        if (examDateData.getQuestionnaire().equals("Y")) {
            PSTV.setVisibility(View.VISIBLE);
            QisLayout.setVisibility(View.VISIBLE);
            NQisLayout.setVisibility(View.GONE);
        } else {
            PSTV.setVisibility(View.GONE);
            QisLayout.setVisibility(View.GONE);
            NQisLayout.setVisibility(View.VISIBLE);
        }


        final AlertDialog alertDialog = new AlertDialog.Builder(VisitingG2Activity.this)
                .setView(dialogView)
                .setOnKeyListener(getOnKeyListener())
                .setCancelable(false).create();

        WriteQisTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeQis();
                alertDialog.dismiss();
                VisitingG2Activity.this.finish();
            }
        });

        NoWriteQisTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                VisitingG2Activity.this.finish();
            }
        });

        IKnowTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                VisitingG2Activity.this.finish();
            }
        });

        alertDialog.show();
    }

    private void writeQis() {
        String url = examDateData.getQuestionnaireURL();
        if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0) {
            Uri uri = Uri.parse(url);

            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary)).setShowTitle(true)
                    .setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back))
                    .build();
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
    }


    private void initBar() {
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);
        actionBar.setTitle("健檢預約 - 選擇方案與時間");
    }

    private void initView() {
        nextBTN = (Button) findViewById(R.id.nextBTN);
        nextBTN.setOnClickListener(this);
        t1 = (TableRow) findViewById(R.id.t1);
        t2 = (TableRow) findViewById(R.id.t2);
        t3 = (TableRow) findViewById(R.id.t3);
        PCRG = (RadioGroup) findViewById(R.id.PCRG);
        PRB = (RadioButton) findViewById(R.id.PRB);
        CRB = (RadioButton) findViewById(R.id.CRB);
        PProjectTV = (TextView) findViewById(R.id.PProjectTV);
        PProjectTV.setOnClickListener(this);
        AutofitHelper.create(PProjectTV);
        CompanyNameTV = (TextView) findViewById(R.id.CompanyNameTV);
        AutofitHelper.create(CompanyNameTV);
        CProjectTV = (TextView) findViewById(R.id.CProjectTV);
        CProjectTV.setOnClickListener(this);
        AutofitHelper.create(CProjectTV);
        DateTV = (TextView) findViewById(R.id.DateTV);
        DateTV.setOnClickListener(this);
        DateTV.setHint("點擊選擇日期");
        mySyncingDialog = new MySyncingDialog(false, this, getResources().getString(R.string.waitingDataStr));
    }

    private void initViewData() {
        if (vidrdata.getECFLAG().equals("Y")) {
            PCRG.setEnabled(true);
            CompanyNameTV.setText(vidrdata.getCNAME());
        } else {
            PCRG.setEnabled(false);
            PRB.setEnabled(false);
            CRB.setEnabled(false);
        }
        PProjectTV.setHint("選擇個人健檢方案");
        CProjectTV.setHint("選擇公司特約方案");

        if (vidrdata.getFirstFLAG().equals("Y") && vidrdata.getECFLAG().equals("Y")) {
            initCompanyProject();
        }
    }

    private void initCompanyProject() {
        new AsyncTask<Void, Void, ProjectForCompanyData>() {
            @Override
            protected ProjectForCompanyData doInBackground(Void... params) {
                ProjectForCompanyData projectForCompanyData = ProjectForCompanyData.getSelf();
                try {
                    String s;
                    s = WebAPI.FucGetProjectForCompany(vidrdata.getCCode());
                    projectForCompanyData = JsonParser.parseProjectForCompany(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return projectForCompanyData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(ProjectForCompanyData projectForCompanyData) {
                super.onPostExecute(projectForCompanyData);
                if (projectForCompanyData.getErrMsg().equals("null")) {
                    vidrdata.setPCode(projectForCompanyData.getProjectCode());
                    vidrdata.setPName(projectForCompanyData.getProjectName());
                    vidrdata.updatePCMap();
                } else {
                    VisitingG2Activity.this.finish();
                    MyToast myToast = new MyToast(VisitingG2Activity.this, getResources().getString(R.string.getDataErrorMsg));
                }
                mySyncingDialog.dismiss();
            }
        }.execute();
    }

    private void initListener() {
        PCRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.PRB:
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.GONE);
                        t3.setVisibility(View.GONE);
                        //清空選擇
                        PProjectTV.setText("");
                        ProjectCode = "";
                        ProjectName = "";
                        DateTV.setText("");
                        DateTV.setEnabled(false);
                        DateTV.setHint("先選擇健檢方案");
                        DateTV.setBackgroundResource(R.drawable.bg_g2_tv_true);
                        nextBTN.setEnabled(false);
                        break;
                    case R.id.CRB:
                        t1.setVisibility(View.GONE);
                        t2.setVisibility(View.VISIBLE);
                        t3.setVisibility(View.VISIBLE);
                        //清空選擇
                        CProjectTV.setText("");
                        ProjectCode = "";
                        ProjectName = "";
                        DateTV.setText("");
                        DateTV.setEnabled(false);
                        DateTV.setHint("先選擇健檢方案");
                        DateTV.setBackgroundResource(R.drawable.bg_g2_tv_true);
                        nextBTN.setEnabled(false);
                        break;
                    default:
                        break;
                }
            }
        });

//        phexListAdapter = new ArrayAdapter<String>(VisitingG2Activity.this, android.R.layout.simple_spinner_dropdown_item, Phexs);
//        PSP.setAdapter(phexListAdapter);
//        PSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //test 前往問卷網頁Flag
//                morebuyTV.setText("選擇 \"" + parent.getItemAtPosition(position) + "\" 的人\n也選擇了以下方案");
//                if (position % 2 == 1) {
//                    webPageFlag = true;
//                } else {
//                    webPageFlag = false;
//                }
//                DateTV.setText("");
//                morebuyLayout.setVisibility(View.GONE);
//                nextBTN.setEnabled(false);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        chexListAdapter = new ArrayAdapter<String>(VisitingG2Activity.this, android.R.layout.simple_spinner_dropdown_item, Comps);
//        CSP1.setAdapter(chexListAdapter);
//        CSP1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        itemname = CompEx1;
//                        break;
//                    case 1:
//                        itemname = CompEx2;
//                        break;
//                    case 2:
//                        itemname = CompEx3;
//                        break;
//                    case 3:
//                        itemname = CompEx4;
//                        break;
//                    case 4:
//                        itemname = CompEx5;
//                        break;
//                    default:
//                        break;
//                }
//                //當itemanme不為空時，對spinner name進行初始化
//                if (itemname != null) {
//                    setnameitem();
//                }
//
//                DateTV.setText("");
//                morebuyLayout.setVisibility(View.GONE);
//                nextBTN.setEnabled(false);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//            private void setnameitem() {
//                showtoast = false;
//                c2hexListAdapter = new ArrayAdapter<String>(VisitingG2Activity.this, android.R.layout.simple_spinner_dropdown_item,
//                        itemname);
//                CSP2.setAdapter(c2hexListAdapter);
//                CSP2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        //test 前往問卷網頁Flag
//                        morebuyTV.setText("選擇 \"" + parent.getItemAtPosition(position) + "\" 的人也選擇了以下方案");
//                        if (position % 2 == 1) {
//                            webPageFlag = true;
//                        } else {
//                            webPageFlag = false;
//                        }
//
//                        if (showtoast) {
//                            //公司體檢選完要做的事
//
//
////                            Toast.makeText(VisitingG2Activity.this,
////                                    "您選擇" + parent.getSelectedItem().toString(),
////                                    Toast.LENGTH_SHORT).show();
//                        }
//                        showtoast = true;
//
//                        DateTV.setText("");
//                        morebuyLayout.setVisibility(View.GONE);
//                        nextBTN.setEnabled(false);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }
//        });
    }

    //write Calendar
    //添加賬戶
    private void initCalendars() {
        Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null, CalendarContract.Calendars.NAME + " = 'TMUHCalendar'", null, null);
        if (userCursor.getCount() > 0) {
            return;
        } else {
            TimeZone timeZone = TimeZone.getDefault();
            ContentValues value = new ContentValues();
            value.put(CalendarContract.Calendars.NAME, "TMUHCalendar");

            value.put(CalendarContract.Calendars.ACCOUNT_NAME, "臺北醫學大學附設醫院預約健檢");
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange");
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "預約健檢日曆");
            value.put(CalendarContract.Calendars.VISIBLE, 1);
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, ContextCompat.getColor(this, R.color.colorPrimary));
            value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, "臺北醫學大學附設醫院健檢中心");
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

            Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
            calendarUri = calendarUri.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "臺北醫學大學附設醫院預約健檢日曆")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange")
                    .build();

            getContentResolver().insert(calendarUri, value);
        }
    }

    private void writeCalendarEvent() {
        initCalendars(); //準備預約日曆

        // 獲取要出入的gmail賬戶的id
        String calId = "";
        Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null, CalendarContract.Calendars.NAME + " = 'TMUHCalendar'", null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToLast();  //注意：是向最後一個賬戶添加，開發者可以根據需要改變添加事件 的賬戶
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));
        } else {
            Toast.makeText(this, "沒有賬戶，請先添加賬戶", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.TITLE, ProjectName);
        event.put(CalendarContract.Events.DESCRIPTION, ProjectName + "\n8點報到");
        // 插入賬戶
        event.put(CalendarContract.Events.CALENDAR_ID, calId);
//        System.out.println("calId: " + calId);
        event.put(CalendarContract.Events.EVENT_LOCATION, "臺北市信義區吳興街252號");

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(PreExamDate);
        mCalendar.set(Calendar.HOUR_OF_DAY, 8);
        mCalendar.set(Calendar.MINUTE, 0);
        long start = mCalendar.getTime().getTime();
        mCalendar.set(Calendar.HOUR_OF_DAY, 12);
        mCalendar.set(Calendar.MINUTE, 0);
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);

//            event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //這個是時區，必須有，
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());  //這個是時區，必須有，
        //添加事件
        Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
        //事件提醒的設定
        long id = Long.parseLong(newEvent.getLastPathSegment());
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, id);
        // 提前60分鐘有提醒
        values.put(CalendarContract.Reminders.MINUTES, 60);
        getContentResolver().insert(Uri.parse(calanderRemiderURL), values);

//        Toast.makeText(VisitingG2Activity.this, "插入事件成功!!!", Toast.LENGTH_LONG).show();
    }

    //Calender設定
    public static class SimpleCalendarDialogFragment extends DialogFragment implements OnDateSelectedListener {

        private TextView enableMTV;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_basic, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            enableMTV = (TextView) view.findViewById(R.id.enableMTV);

            MaterialCalendarView widget = (MaterialCalendarView) view.findViewById(R.id.calendarView);
            widget.setHeaderTextAppearance(R.style.TextAppearance_Header);
            widget.setDateTextAppearance(R.style.TextAppearance_Date);
            widget.setOnDateChangedListener(this);
            widget.setBackgroundColor(Color.parseColor("#FFFFFF"));

            // Add a decorator to disable prime numbered days
            widget.addDecorator(new PrimeDayableDecorator());
//            widget.addDecorator(new SaturDayDecorator());
//            widget.addDecorator(new SundayDecorator());
            widget.addDecorator(new disablePastDecorator());
            widget.addDecorator(new PrimeDayDisableDecorator());

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);                      //取出年
            int month = c.get(Calendar.MONTH) + 1;           //取出月，月份的編號是由0~11 故+1
            int day = c.get(Calendar.DAY_OF_MONTH);        //取出日
            c.add(Calendar.MONTH, 1);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            int year2 = c.get(Calendar.YEAR);                      //取出年
            int month2 = c.get(Calendar.MONTH) + 1;           //取出月，月份的編號是由0~11 故+1
            int day2 = c.get(Calendar.DAY_OF_MONTH);
            widget.state().edit()
                    .setMinimumDate(CalendarDay.from(year, month - 1, 1))
                    .setMaximumDate(CalendarDay.from(year2, month2 - 1, day2))
                    .commit();

            day += 1;
            enableMTV.setText("開放預約日期\n" + year + "/" + month + "/" + day + " 至 " + year2 + "/" + month2 + "/" + day2);
        }

        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//            Toast.makeText(getActivity(), ""+date.getDate() + "\n" + date.getCalendar().get(Calendar.DAY_OF_WEEK), Toast.LENGTH_LONG).show();
            VisitingG2Activity.PreExamDate = date.getDate();
            VisitingG2Activity.DateTV.setText(MyDateSFormat.getFrmt_yMd().format(VisitingG2Activity.PreExamDate));
            VisitingG2Activity.nextBTN.setEnabled(true);
//            VisitingG2Activity.morebuyLayout.setVisibility(View.VISIBLE);
//            morePayLayout1.setVisibility(View.GONE);
//            morePayLayout2.setVisibility(View.GONE);
//            morePayLayout3.setVisibility(View.GONE);
//            morePayLayout4.setVisibility(View.GONE);
            this.dismiss();
        }
    }

    private static class SaturDayDecorator implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();
        private final Drawable highlightDrawable;
        private static final int color = Color.parseColor("#228BC34A");

        public SaturDayDecorator() {
            highlightDrawable = new ColorDrawable(color);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(highlightDrawable);
        }
    }

    private static class SundayDecorator implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();
        private final Drawable highlightDrawable;
        private static final int color = Color.parseColor("#228BC34A");

        public SundayDecorator() {
            highlightDrawable = new ColorDrawable(color);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
            view.setBackgroundDrawable(highlightDrawable);
        }
    }

    private static class disablePastDecorator implements DayViewDecorator {

        private final Drawable highlightDrawable;
        private static final int color = Color.parseColor("#DFDFDF");

        public disablePastDecorator() {
            highlightDrawable = new ColorDrawable(color);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            Calendar c = Calendar.getInstance();
            if (day.getCalendar().get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                return day.getDay() < c.get(Calendar.DAY_OF_MONTH);
            } else {
                return false;
            }
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(highlightDrawable);
            view.setDaysDisabled(true);
        }
    }

    private static class PrimeDayDisableDecorator implements DayViewDecorator {

        private final Drawable highlightDrawable;
        private static final int color = Color.parseColor("#FFFFFF");

        public PrimeDayDisableDecorator() {
            highlightDrawable = new ColorDrawable(color);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            Date date = day.getDate();
            String dateStr = MyDateSFormat.getFrmt_yyyyMMdd().format(date);
            return !ExamDateData.getSelf().getExamDateLst().contains(dateStr);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(highlightDrawable);
            view.setDaysDisabled(true);
        }
    }

    private static class PrimeDayableDecorator implements DayViewDecorator {

        private final Drawable highlightDrawable;
        private static final int color = Color.parseColor("#5577DDFF");

        public PrimeDayableDecorator() {
            highlightDrawable = new ColorDrawable(color);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            Date date = day.getDate();
            String dateStr = MyDateSFormat.getFrmt_yyyyMMdd().format(date);
            return ExamDateData.getSelf().getExamDateLst().contains(dateStr);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(highlightDrawable);
            view.setDaysDisabled(false);
        }
    }
}
