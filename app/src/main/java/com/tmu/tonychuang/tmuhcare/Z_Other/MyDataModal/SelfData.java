package com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal;

/**
 * Created by TonyChuang on 2016/7/1.
 */
public class SelfData {
    private static SelfData singleton = new SelfData();
    private SelfData(){}
    public static SelfData getSelf() {
        return singleton;
    }

    private String isTaiwan = "";
    private String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsTaiwan() {
        return isTaiwan;
    }

    public void setIsTaiwan(String isTaiwan) {
        this.isTaiwan = isTaiwan;
    }
}
