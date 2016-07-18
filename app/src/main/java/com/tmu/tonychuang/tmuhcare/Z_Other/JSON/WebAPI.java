package com.tmu.tonychuang.tmuhcare.Z_Other.JSON;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by TonyChuang on 2016/6/29.
 */
public class WebAPI {
    private static final String url = "http://203.71.89.114:9999/TMUH_Healthcare/Service1.asmx";
    private static final String TAG = "netUtils";

    /**
     * 根據流返回一個字符串信息         *
     * @param is
     * @return
     * @throws IOException
     */
    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 模板代碼 必須熟練
        byte[] buffer = new byte[1024];
        int len = -1;
        // 一定要寫len=is.read(buffer)
        // 如果while((is.read(buffer))!=-1)則無法將數據寫入buffer中
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的數據轉換成字符串,採用的編碼是utf-8(模擬器默認編碼)
        os.close();
        return state;
    }


    /**
     * 使用post方式登陸
     *
     * @return
     */
    private static String load(String methodName, Map<String, String> params) {
        HttpURLConnection conn = null;
        String murl = url + "/" + methodName;
        try {
            // 創建一個URL對象
            URL mURL = new URL(murl);
            // 調用URL的openConnection()方法,獲取HttpURLConnection對象
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("POST");// 設置請求方法為post
            conn.setReadTimeout(5000);// 設置讀取超時為5秒
            conn.setConnectTimeout(10000);// 設置連接網絡超時為10秒
            conn.setDoOutput(true);// 設置此方法,允許向服務器輸出內容

            // post請求的參數
            String data = "";
            Set<String> keySet = params.keySet();
            if (!keySet.isEmpty()) {
                for (String key : keySet) {
                    data += key + "=" + params.get(key) + "&";
                }
                data = data.substring(0, data.length() - 1);
            }
            // 獲得一個輸出流,向服務器寫數據,默認情況下,系統不允許向服務器輸出內容
            OutputStream out = conn.getOutputStream();// 獲得一個輸出流,向服務器寫數據
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();// 調用此方法就不必再使用conn.connect()方法
            if (responseCode == 200) {

                InputStream is = conn.getInputStream();
                String state = getStringFromInputStream(is);
                return state;
            } else {
                Log.i(TAG, "訪問失敗" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();// 關閉連接
            }
        }
        return null;
    }

    public static String FucVIDR (String isTaiwan, String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("isTaiwan", isTaiwan);
        map.put("id", id);
        return load("FucVIDR", map);
    }

    public static String FucGetProjectForCompany (String CompanyCode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("CompanyCode", CompanyCode);
        return load("FucGetProjectForCompany", map);
    }

    public static String FucGetProject () {
        Map<String, String> map = new HashMap<String, String>();
        return load("FucGetProject", map);
    }

    public static String FucGetExamDate (String id, String isCompany, String appCode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("isCompany", isCompany);
        map.put("appCode", appCode);
        return load("FucGetExamDate", map);
    }

    public static String FucGetExamData (String isTaiwan, String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("isTaiwan", isTaiwan);
        map.put("id", id);
        return load("FucGetExamData", map);
    }

    public static String FucGetCompany () {
        Map<String, String> map = new HashMap<String, String>();
        return load("FucGetCompany", map);
    }

    public static String FucWriteBasicData (String isTaiwan, String id, String patname, String phone,
                                            String email, String sex, String birthdate,
                                            String adrr, String companycode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("isTaiwan", isTaiwan);
        map.put("id", id);
        map.put("patname", patname);
        map.put("phone", phone);
        map.put("email", email);
        map.put("sex", sex);
        map.put("birthdate", birthdate);
        map.put("adrr", adrr);
        map.put("companycode", companycode);
        return load("FucWriteBasicData", map);
    }

    public static String FucWriteExamData (String isTaiwan, String id, String projectcode, String opd_date) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("isTaiwan", isTaiwan);
        map.put("id", id);
        map.put("projectcode", projectcode);
        map.put("opd_date", opd_date);
        return load("FucWriteExamData", map);
    }

    public static String FucGetQisWrite (String isTaiwan, String id, String app_no, String opd_date) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("isTaiwan", isTaiwan);
        map.put("id", id);
        map.put("app_no", app_no);
        map.put("opd_date", opd_date);
        return load("FucGetQisWrite", map);
    }

    public static String FucGetExamisCancel (String isTaiwan, String id, String app_no, String opd_date) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("isTaiwan", isTaiwan);
        map.put("id", id);
        map.put("app_no", app_no);
        map.put("opd_date", opd_date);
        return load("FucGetExamisCancel", map);
    }
}
