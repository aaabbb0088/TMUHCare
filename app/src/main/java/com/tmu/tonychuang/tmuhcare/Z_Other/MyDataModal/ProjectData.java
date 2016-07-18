package com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TonyChuang on 2016/6/29.
 */
public class ProjectData {

    private static ProjectData self = new ProjectData();
    private ProjectData(){}
    public static ProjectData getSelf() {
        return self;
    }

    private ArrayList<String> ProjectCode = new ArrayList<>();
    private ArrayList<String> ProjectName = new ArrayList<>();
    private ArrayList<String> CompanyCode = new ArrayList<>();
    private Map<String, String> PCodeNameMap = new HashMap<>();
    private String ErrMsg = "null";

    public void clearAll() {
        ProjectCode.clear();
        ProjectName.clear();
        CompanyCode.clear();
        ErrMsg = "null";
    }

    public ArrayList<String> getProjectCode() {
        return ProjectCode;
    }

    public void setProjectCode(ArrayList<String> projectCode) {
        ProjectCode = projectCode;
    }

    public ArrayList<String> getProjectName() {
        return ProjectName;
    }

    public void setProjectName(ArrayList<String> projectName) {
        ProjectName = projectName;
    }

    public ArrayList<String> getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(ArrayList<String> companyCode) {
        CompanyCode = companyCode;
    }

    public Map<String, String> getPCodeNameMap() {
        return PCodeNameMap;
    }

    public void setPCodeNameMap(Map<String, String> PCodeNameMap) {
        this.PCodeNameMap = PCodeNameMap;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }
}
