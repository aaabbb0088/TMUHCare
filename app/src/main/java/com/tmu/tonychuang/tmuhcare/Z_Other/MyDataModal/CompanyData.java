package com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TonyChuang on 2016/6/29.
 */
public class CompanyData {

    private static CompanyData self = new CompanyData();
    private CompanyData(){}
    public static CompanyData getSelf() {
        return self;
    }

    private ArrayList<String> CompanyCode = new ArrayList<>();
    private ArrayList<String> CompanyName = new ArrayList<>();
    private Map<String, String> CompanyNameCode = new HashMap<>();
    private String ErrMsg = "null";

    public void clearAll() {
        CompanyCode.clear();
        CompanyName.clear();
        CompanyNameCode.clear();
        ErrMsg = "null";
    }

    public ArrayList<String> getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(ArrayList<String> companyCode) {
        CompanyCode = companyCode;
    }

    public ArrayList<String> getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(ArrayList<String> companyName) {
        CompanyName = companyName;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public Map<String, String> getCompanyNameCodeMap() {
        return CompanyNameCode;
    }

    public void setCompanyNameCodeMap(Map<String, String> companyNameCode) {
        CompanyNameCode = companyNameCode;
    }
}
