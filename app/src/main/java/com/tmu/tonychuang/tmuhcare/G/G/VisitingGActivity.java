package com.tmu.tonychuang.tmuhcare.G.G;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tmu.tonychuang.tmuhcare.R;
import com.tmu.tonychuang.tmuhcare.Z_Other.JSON.JsonParser;
import com.tmu.tonychuang.tmuhcare.Z_Other.JSON.WebAPI;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.CompanyData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ProjectData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.SelfData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.VIDRData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.WriteBasicData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyModal.FilterListViewAdapter;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyModal.MyDateSFormat;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyModal.MyValidator;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyUI.MySyncingDialog;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyUI.MyToast;

import java.util.Date;

import me.grantland.widget.AutofitHelper;

public class VisitingGActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private Button nextBTN;
    private TableRow t3;
    private TableRow t4;
    private TableRow t5;
    private TableRow t6;
    private TableRow t7;
    private TableRow t8;
    private TableRow t9;
    private TableRow t10;
    private boolean FirstVFlag = true;
    private RadioGroup PidRG;
    private RadioButton TWRB;
    private RadioButton OSRB;
    private MaterialEditText PidET;
    private InputMethodManager imm;
    private MaterialEditText NamET;
    private MaterialEditText PhoneET;
    private MaterialEditText EmailET;
    private RadioGroup SexRG;
    private RadioButton mlRB;
    private RadioButton fmlRB;
    private TextView BirthDayTV;
    private MaterialEditText AddressET;
    private TextView CompanyTV;

    private MySyncingDialog mySyncingDialog;

    //FucGetProject
    private ProjectData projectData = ProjectData.getSelf();

    //FucVIDR
    private String isTaiwan = "";
    private String id = "";
    private VIDRData vidrData = VIDRData.getSelf();

    //FucGetCompany
    private CompanyData companyData = CompanyData.getSelf();

    //id紀錄
    private SelfData selfData = SelfData.getSelf();

    //暫定 上傳初診基本資料Fuc
    private String patname = "";
    private String phone = "";
    private String email = "";
    private String sex = "";
    private String birthdate = "";
    private String adrr = "";
    private WriteBasicData writeBasicData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_g);

        initBar();
        initView();
        initListener();
        showTableRpw(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initProject();
    }

    @Override
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(PidET.getWindowToken(), 0);
        NamET.clearFocus();
        PhoneET.clearFocus();
        EmailET.clearFocus();
        AddressET.clearFocus();
        switch (v.getId()){
            case R.id.nextBTN:
                if (FirstVFlag) {
                    id = PidET.getText().toString().trim().toUpperCase();
                    selfData.setId(id);
                    selfData.setIsTaiwan(isTaiwan);
                    inputPid();
                } else {
                    //如果資料填寫正確,執行以下
                    if (!TextUtils.isEmpty(NamET.getText()) &&
                            !TextUtils.isEmpty(PhoneET.getText()) &&
                            (MyValidator.isValidPhone(PhoneET.getText().toString()) ||
                                    MyValidator.isValidCellPhone(PhoneET.getText().toString())) &&
                            !TextUtils.isEmpty(EmailET.getText()) &&
                            MyValidator.isValidEmail(EmailET.getText().toString()) &&
                            SexRG.getCheckedRadioButtonId() != -1 &&
                            !TextUtils.isEmpty(BirthDayTV.getText()) &&
                            !TextUtils.isEmpty(AddressET.getText())) {
                        patname = NamET.getText().toString();
                        phone = PhoneET.getText().toString();
                        email = EmailET.getText().toString();
                        adrr = AddressET.getText().toString();
                        updateFirstData();
                    } else {
                        //如果資料填寫錯誤，顯示錯誤欄位
                        String str = "";
                        if (TextUtils.isEmpty(NamET.getText())) {
                            str += "姓名未填\n";
                        }
                        if (TextUtils.isEmpty(PhoneET.getText())) {
                            str += "電話未填\n";
                        } else {
                            if (!MyValidator.isValidPhone(PhoneET.getText().toString()) &&
                                    !MyValidator.isValidCellPhone(PhoneET.getText().toString())) {
                                str += "電話格式錯誤\n";
                            }
                        }
                        if (TextUtils.isEmpty(EmailET.getText())) {
                            str += "電子郵件信箱未填\n";
                        } else if (MyValidator.isValidEmail(EmailET.getText().toString())) {
                            str += "電子郵件信箱格式錯誤\n";
                        }
                        if (SexRG.getCheckedRadioButtonId() == -1) {
                            str += "性別未選\n";
                        }
                        if (TextUtils.isEmpty(BirthDayTV.getText())) {
                            str += "生日未選\n";
                        }
                        if (TextUtils.isEmpty(AddressET.getText())) {
                            str += "通訊地址未填\n";
                        }
                        str = str.substring(0, str.length() - 1);
                        Toast.makeText(VisitingGActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.BirthDayTV:
                Date date = new Date();
                String str = MyDateSFormat.getFrmt_yMd().format(date);
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(VisitingGActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        BirthDayTV.setText(dateDesc);
                        birthdate = dateDesc.substring(0, 4) + dateDesc.substring(5, 7) + dateDesc.substring(8);
                    }
                }).textConfirm("確定") //text of confirm button
                        .textCancel("取消") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
                        .colorConfirm(Color.parseColor("#000000"))//color of confirm button
                        .minYear(1900) //min year in loop
                        .maxYear(2550) // max year in loop
                        .dateChose(str) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(VisitingGActivity.this);
                break;
            case R.id.mlRB:
                mlRB.requestFocus();
                sex = "1";
                break;
            case R.id.fmlRB:
                fmlRB.requestFocus();
                sex = "0";
                break;
            case R.id.CompanyTV:
                CompanyTV.requestFocus();
                chooseCompanyDialog();
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

    private void updateFirstData() {
        new AsyncTask<String, Void, WriteBasicData>() {
            @Override
            protected WriteBasicData doInBackground(String... params) {
                writeBasicData = new WriteBasicData();
                try {
                    String s;
                    s = WebAPI.FucWriteBasicData(params[0], params[1], params[2], params[3],
                            params[4], params[5], params[6], params[7], params[8]);
                    writeBasicData = JsonParser.parseWriteBasicData(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return writeBasicData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(WriteBasicData writeBasicData) {
                super.onPostExecute(writeBasicData);
                if (writeBasicData.getErrMsg().equals("null")) {
                    mySyncingDialog.dismiss();
                    Intent intent = new Intent(VisitingGActivity.this, VisitingG2Activity.class);
                    startActivity(intent);
                    VisitingGActivity.this.finish();
                } else {
                    mySyncingDialog.dismiss();
                    MyToast myToast = new MyToast(VisitingGActivity.this, getResources().getString(R.string.getDataErrorMsg));
                }
            }
        }.execute(isTaiwan, id, patname, phone, email, sex, birthdate, adrr, vidrData.getCCode());
    }

    private void chooseCompanyDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_choose_company, null);
        final TextView TitleTV = (TextView) dialogView.findViewById(R.id.TitleTV);
        final MaterialEditText KeyWordET = (MaterialEditText) dialogView.findViewById(R.id.KeyWordET);
        final ListView CompanyLV = (ListView) dialogView.findViewById(R.id.CompanyLV);
        final TextView NoCompanyTV = (TextView) dialogView.findViewById(R.id.NoCompanyTV);
        final InputMethodManager imm = (InputMethodManager) dialogView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        TitleTV.setText("選擇您的公司");
        KeyWordET.setHint("關鍵字 (空白分隔條件)");
        NoCompanyTV.setText("我沒有特約公司身分");

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setOnKeyListener(getOnKeyListener())
                .setCancelable(false).create();

//        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,companyData.getCompanyName());
//        CompanyLV.setAdapter(listAdapter);

        final FilterListViewAdapter companyAdapter = new FilterListViewAdapter(VisitingGActivity.this, companyData.getCompanyName(), true);
        CompanyLV.setAdapter(companyAdapter);

        CompanyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView CompanyNameTV = (TextView) view.findViewById(R.id.tv_name);
                String CName = CompanyNameTV.getText().toString();
                vidrData.setCNAME(CName);
                CName = CName.replace("\n", "");
                vidrData.setCCode(companyData.getCompanyNameCodeMap().get(CName));
                vidrData.setECFLAG("Y");
                CompanyTV.setText(vidrData.getCNAME());
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
                imm.hideSoftInputFromWindow(KeyWordET.getWindowToken(), 0);
                vidrData.setCNAME("");
                vidrData.setCCode("");
                vidrData.setECFLAG("N");
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

    private void initProject() {
        new AsyncTask<Void, Void, ProjectData>(){
            @Override
            protected ProjectData doInBackground(Void... params) {
                try {
                    String s;
                    s = WebAPI.FucGetProject();
                    projectData = JsonParser.parseProject(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return projectData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(ProjectData projectData) {
                super.onPostExecute(projectData);
                mySyncingDialog.dismiss();
                if (!projectData.getErrMsg().equals("null")) {
                    VisitingGActivity.this.finish();
                    MyToast myToast = new MyToast(VisitingGActivity.this, getResources().getString(R.string.getDataErrorMsg));
                }
            }
        }.execute();
    }

    private void inputPid() {
        new AsyncTask<String, Void, VIDRData>(){
            @Override
            protected VIDRData doInBackground(String... params) {
                try {
                    String s;
                    s = WebAPI.FucVIDR(params[0], params[1]);
                    vidrData = JsonParser.parseVIDRData(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return vidrData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySyncingDialog.show();
            }

            @Override
            protected void onPostExecute(VIDRData vidrData) {
                super.onPostExecute(vidrData);
                if (vidrData.getErrMsg().equals("null")) {
                    if (vidrData.getFirstFLAG().equals("N")) {
                        mySyncingDialog.dismiss();
                        Intent intent = new Intent(VisitingGActivity.this, VisitingG2Activity.class);
                        startActivity(intent);
                        VisitingGActivity.this.finish();
                    } else {
                        initCompany();
                    }
                } else {
                    mySyncingDialog.dismiss();
                    MyToast myToast = new MyToast(VisitingGActivity.this, getResources().getString(R.string.getDataErrorMsg));
                }
            }
        }.execute(isTaiwan, id);
    }

    private void initCompany() {
        new AsyncTask<Void, Void, CompanyData>(){
            @Override
            protected CompanyData doInBackground(Void... params) {
                try {
                    String s;
                    s = WebAPI.FucGetCompany();
                    companyData = JsonParser.parseCompany(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return companyData;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(CompanyData companyData) {
                super.onPostExecute(companyData);
                mySyncingDialog.dismiss();
                if (companyData.getErrMsg().equals("null")) {
                    showTableRpw(true);
                    FirstVFlag = false;
                    TWRB.setEnabled(false);
                    OSRB.setEnabled(false);
                    PidET.setEnabled(false);
                    PidET.setShowClearButton(false);
                } else {
                    MyToast myToast = new MyToast(VisitingGActivity.this, getResources().getString(R.string.getDataErrorMsg));
                }
            }
        }.execute();
    }

    private void initView() {
        nextBTN = (Button) findViewById(R.id.nextBTN);
        nextBTN.setEnabled(false);
        t3 = (TableRow) findViewById(R.id.t3);
        t4 = (TableRow) findViewById(R.id.t4);
        t5 = (TableRow) findViewById(R.id.t5);
        t6 = (TableRow) findViewById(R.id.t6);
        t7 = (TableRow) findViewById(R.id.t7);
        t8 = (TableRow) findViewById(R.id.t8);
        t9 = (TableRow) findViewById(R.id.t9);
        t10 = (TableRow) findViewById(R.id.t10);
        PidRG = (RadioGroup) findViewById(R.id.PidRG);
        PidET = (MaterialEditText) findViewById(R.id.PidET);
        PidET.setEnabled(false);
        TWRB = (RadioButton) findViewById(R.id.TWRB);
        OSRB = (RadioButton) findViewById(R.id.OSRB);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        NamET = (MaterialEditText) findViewById(R.id.NamET);
        PhoneET = (MaterialEditText) findViewById(R.id.PhoneET);
        EmailET = (MaterialEditText) findViewById(R.id.EmailET);
        SexRG = (RadioGroup) findViewById(R.id.SexRG);
        mlRB = (RadioButton) findViewById(R.id.mlRB);
        fmlRB = (RadioButton) findViewById(R.id.fmlRB);
        BirthDayTV = (TextView) findViewById(R.id.BirthDayTV);
        AddressET = (MaterialEditText) findViewById(R.id.AddressET);
        CompanyTV = (TextView) findViewById(R.id.CompanyTV);
        AutofitHelper.create(CompanyTV);

        mySyncingDialog = new MySyncingDialog(false,this,getResources().getString(R.string.waitingDataStr));
    }

    private void initListener() {
        nextBTN.setOnClickListener(this);

        //PidET
        PidRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PidET.setEnabled(true);
                PidET.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                switch (checkedId) {
                    case R.id.TWRB:
                        isTaiwan = "Y";
                        PidET.setHint("身分證字號");
                        PidET.setText("");
                        PidET.setMaxCharacters(10);
                        imm.showSoftInput(PidET,InputMethodManager.SHOW_FORCED); //顯示鍵盤
                        break;
                    case R.id.OSRB:
                        isTaiwan = "N";
                        PidET.setHint("居留證號碼");
                        PidET.setText("");
                        PidET.setMaxCharacters(10);
                        imm.showSoftInput(PidET,InputMethodManager.SHOW_FORCED);
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
                switch (PidRG.getCheckedRadioButtonId()){
                    case R.id.TWRB:
                        if (!TextUtils.isEmpty(PidET.getText())
                                && MyValidator.isValidTWPID(PidET.getText().toString())) {
                            nextBTN.setEnabled(true);
                        } else {
                            nextBTN.setEnabled(false);
                        }
                        break;
                    case R.id.OSRB:
                        if (!TextUtils.isEmpty(PidET.getText())
                                && PidET.getText().length() == 10) {
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

        //基本資料填寫
        BirthDayTV.setOnClickListener(this);

        NamET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    NamET.setError(null);
                } else {
                    if (TextUtils.isEmpty(NamET.getText())) {
                        NamET.setError("必填");
                    }
                }
            }
        });

        PhoneET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    PhoneET.setError(null);
                } else {
                    if (TextUtils.isEmpty(PhoneET.getText())) {
                        PhoneET.setError("必填");
                    } else {
                        boolean CellPhoneFlag = !MyValidator.isValidCellPhone(PhoneET.getText().toString());
                        boolean PhoneFlag = !MyValidator.isValidPhone(PhoneET.getText().toString());
                        if (CellPhoneFlag && PhoneFlag) {
                            PhoneET.setError("格式錯誤");
                        }
                    }
                }
            }
        });

        EmailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EmailET.setError(null);
                } else {
                    if (TextUtils.isEmpty(EmailET.getText())) {
                        EmailET.setError("必填");
                    } else if (!MyValidator.isValidEmail(EmailET.getText().toString())) {
                        EmailET.setError("格式錯誤");
                    }
                }
            }
        });

        mlRB.setOnClickListener(this);
        fmlRB.setOnClickListener(this);

        AddressET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AddressET.setError(null);
                } else {
                    if (TextUtils.isEmpty(AddressET.getText())) {
                        AddressET.setError("必填");
                    }
                }
            }
        });

        CompanyTV.setOnClickListener(this);
    }

    private void initBar() {
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);
        actionBar.setTitle("健檢預約");
    }

    private void showTableRpw(boolean Flag) {
        if (Flag) {
            t3.setVisibility(View.VISIBLE);
            t4.setVisibility(View.VISIBLE);
            t5.setVisibility(View.VISIBLE);
            t6.setVisibility(View.VISIBLE);
            t7.setVisibility(View.VISIBLE);
            t8.setVisibility(View.VISIBLE);
            t9.setVisibility(View.VISIBLE);
            t10.setVisibility(View.VISIBLE);
        } else {
            t3.setVisibility(View.GONE);
            t4.setVisibility(View.GONE);
            t5.setVisibility(View.GONE);
            t6.setVisibility(View.GONE);
            t7.setVisibility(View.GONE);
            t8.setVisibility(View.GONE);
            t9.setVisibility(View.GONE);
            t10.setVisibility(View.GONE);
        }
    }
}
