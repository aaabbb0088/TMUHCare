package com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TonyChuang on 2016/6/27.
 */
public class VIDRData {

    private static VIDRData self = new VIDRData();
    private VIDRData(){}
    public static VIDRData getSelf() {
        return self;
    }

    private ArrayList<String> PCode = new ArrayList<>();
    private ArrayList<String> PName = new ArrayList<>();
    private String IDFLAG = "";
    private String ErrMsg = "null";
    private String FirstFLAG = "";
    private String ECFLAG = "";
    private String CNAME = "";
    private String CCode = "";
    private Map<String, String> PCNMap = new HashMap<>();

    public void clearAll() {
        PCode.clear();
        PName.clear();
        IDFLAG = "";
        ErrMsg = "null";
        FirstFLAG = "";
        ECFLAG = "";
        CNAME = "";
        CCode = "";
        PCNMap.clear();
    }

    public ArrayList<String> getPCode() {
        return PCode;
    }

    public void setPCode(ArrayList<String> PCode) {
        this.PCode.clear();
        this.PCode = PCode;
    }

    public ArrayList<String> getPName() {
        return PName;
    }

    public void setPName(ArrayList<String> PName) {
        this.PName.clear();
        this.PName = PName;
    }

    public String getIDFLAG() {
        return IDFLAG;
    }

    public void setIDFLAG(String IDFLAG) {
        this.IDFLAG = IDFLAG;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public String getFirstFLAG() {
        return FirstFLAG;
    }

    public void setFirstFLAG(String firstFLAG) {
        FirstFLAG = firstFLAG;
    }

    public String getECFLAG() {
        return ECFLAG;
    }

    public void setECFLAG(String ECFLAG) {
        this.ECFLAG = ECFLAG;
    }

    public String getCNAME() {
        return CNAME;
    }

    public void setCNAME(String CNAME) {
        this.CNAME = CNAME;
    }

    public String getCCode() {
        return CCode;
    }

    public void setCCode(String CCode) {
        this.CCode = CCode;
    }

    public Map<String, String> getPCNMap() {
        return PCNMap;
    }

    public void setPCNMap(Map<String, String> PCNMap) {
        this.PCNMap = PCNMap;
    }

    public void updatePCMap() {
        PCNMap.clear();
        if (!PCode.isEmpty() && !PName.isEmpty() && PName.size() == PCode.size()) {
            for (int i = 0 ; i < PCode.size() ; i++) {
                PCNMap.put(PName.get(i), PCode.get(i));
            }
        }
    }
}