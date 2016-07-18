package com.tmu.tonychuang.tmuhcare.Z_Other.JSON;

import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.CompanyData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ExamData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ExamDateData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ExamisCancelData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ProjectData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ProjectForCompanyData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.QisWriteData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.VIDRData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.WriteBasicData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.WriteExamData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by TonyChuang on 2016/6/29.
 */
public class JsonParser {

    public static VIDRData parseVIDRData(String jsonStr) {
        VIDRData vidrData = VIDRData.getSelf();
        vidrData.clearAll();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                vidrData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (vidrData.getErrMsg().equals("null")) {
                    vidrData.setPCode(JSONFormatter.getStringArray(jsonObject.getJSONArray("PCode")));
                    vidrData.setPName(JSONFormatter.getStringArray(jsonObject.getJSONArray("PName")));
                    vidrData.setIDFLAG(jsonObject.getString("IDFLAG"));
                    vidrData.setFirstFLAG(jsonObject.getString("FirstFLAG"));
                    vidrData.setECFLAG(jsonObject.getString("ECFLAG"));
                    vidrData.setCNAME(jsonObject.getString("CNAME"));
                    vidrData.setCCode(jsonObject.getString("CCode"));
                    for (int i = 0 ; i < vidrData.getPCode().size() ; i++) {
                        vidrData.getPCNMap().put(vidrData.getPName().get(i), vidrData.getPCode().get(i));
                    }
                } else {
                    vidrData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return vidrData;
    }

    public static ProjectForCompanyData parseProjectForCompany(String jsonStr) {
        ProjectForCompanyData projectForCompanyData = ProjectForCompanyData.getSelf();
        projectForCompanyData.clearAll();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                projectForCompanyData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (projectForCompanyData.getErrMsg().equals("null")) {
                    projectForCompanyData.setProjectCode(JSONFormatter.getStringArray(jsonObject.getJSONArray("ProjectCode")));
                    projectForCompanyData.setProjectName(JSONFormatter.getStringArray(jsonObject.getJSONArray("ProjectName")));
                    projectForCompanyData.setCompanyCode(JSONFormatter.getStringArray(jsonObject.getJSONArray("CompanyCode")));
                } else {
                    projectForCompanyData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return projectForCompanyData;
    }

    public static ProjectData parseProject(String jsonStr) {
        ProjectData projectData = ProjectData.getSelf();
        projectData.clearAll();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                projectData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (projectData.getErrMsg().equals("null")) {
                    projectData.setProjectCode(JSONFormatter.getStringArray(jsonObject.getJSONArray("ProjectCode")));
                    projectData.setProjectName(JSONFormatter.getStringArray(jsonObject.getJSONArray("ProjectName")));
                    projectData.setCompanyCode(JSONFormatter.getStringArray(jsonObject.getJSONArray("CompanyCode")));
                    for (int i = 0 ; i < projectData.getProjectCode().size() ; i++) {
                        projectData.getPCodeNameMap().put(projectData.getProjectName().get(i), projectData.getProjectCode().get(i));
                    }
                } else {
                    projectData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return projectData;
    }

    public static ExamDateData parseExamDate(String jsonStr) {
        ExamDateData examDateData = ExamDateData.getSelf();
        examDateData.clearAll();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                examDateData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (examDateData.getErrMsg().equals("null")) {
                    examDateData.setExamDateLst(JSONFormatter.getStringArray(jsonObject.getJSONArray("ExamDateLst")));
                    examDateData.setQuestionnaire(jsonObject.getString("Questionnaire"));
                    examDateData.setQuestionnaireName(jsonObject.getString("QuestionnaireName"));
                    examDateData.setQuestionnaireURL(jsonObject.getString("QuestionnaireURL"));
                } else {
                    examDateData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return examDateData;
    }

    public static ArrayList<ExamData> parseExamData(String jsonStr) {
        ArrayList<ExamData> arrayList = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                JSONObject jsonObj = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObj = jsonArray.getJSONObject(i);
                    arrayList.add(new ExamData(
                            jsonObj.getString("Vid_Flag"),
                            jsonObj.getString("Customer_Exam_No"),
                            jsonObj.getString("APP_NO"),
                            jsonObj.getString("APP_NO_NAME"),
                            jsonObj.getString("OPD_DATE"),
                            jsonObj.getString("Questionnaire"),
                            jsonObj.getString("Questionnaire_Write"),
                            jsonObj.getString("QuestionnaireURL"),
                            jsonObj.getString("ErrMsg")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public static CompanyData parseCompany(String jsonStr) {
        CompanyData companyData = CompanyData.getSelf();
        companyData.clearAll();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                companyData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (companyData.getErrMsg().equals("null")) {
                    companyData.setCompanyCode(JSONFormatter.getStringArray(jsonObject.getJSONArray("CompanyCode")));
                    companyData.setCompanyName(JSONFormatter.getStringArray(jsonObject.getJSONArray("CompanyName")));
                    for (int i = 0 ; i < companyData.getCompanyName().size() ; i++) {
                        companyData.getCompanyNameCodeMap().put(companyData.getCompanyName().get(i), companyData.getCompanyCode().get(i));
                    }
                } else {
                    companyData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return companyData;
    }

    public static WriteBasicData parseWriteBasicData(String jsonStr) {
        WriteBasicData writeBasicData = new WriteBasicData();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                writeBasicData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (writeBasicData.getErrMsg().equals("null")) {
                    writeBasicData.setDWrite(jsonObject.getString("DWrite"));
                } else {
                    writeBasicData.setDWrite(jsonObject.getString("DWrite"));
                    writeBasicData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return writeBasicData;
    }

    public static WriteExamData parseWriteExamData(String jsonStr) {
        WriteExamData writeExamData = new WriteExamData();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                writeExamData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (writeExamData.getErrMsg().equals("null")) {
                    writeExamData.setDWrite(jsonObject.getString("DWrite"));
                } else {
                    writeExamData.setDWrite(jsonObject.getString("DWrite"));
                    writeExamData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return writeExamData;
    }

    public static QisWriteData parseQisWriteData(String jsonStr) {
        QisWriteData qisWriteData = new QisWriteData();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                qisWriteData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (qisWriteData.getErrMsg().equals("null")) {
                    qisWriteData.setDWrite(jsonObject.getString("DWrite"));
                } else {
                    qisWriteData.setDWrite(jsonObject.getString("DWrite"));
                    qisWriteData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return qisWriteData;
    }

    public static ExamisCancelData parseExamisCancelData(String jsonStr) {
        ExamisCancelData examisCancelData = new ExamisCancelData();
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                examisCancelData.setErrMsg(jsonObject.getString("ErrMsg"));
                if (examisCancelData.getErrMsg().equals("null")) {
                    examisCancelData.setDWrite(jsonObject.getString("DWrite"));
                } else {
                    examisCancelData.setDWrite(jsonObject.getString("DWrite"));
                    examisCancelData.setErrMsg(jsonObject.getString("ErrMsg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return examisCancelData;
    }
}
