package com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal;

/**
 * Created by TonyChuang on 2016/6/16.
 */
public class Singleton {
    private static Singleton singleton = new Singleton();
    private Singleton(){}
    public static Singleton getSingleton() {
        return singleton;
    }
}
