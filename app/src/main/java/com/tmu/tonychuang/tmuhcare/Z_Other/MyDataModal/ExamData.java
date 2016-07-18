package com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal;

/**
 * Created by TonyChuang on 2016/6/29.
 */
public class ExamData {

//    private static ExamData self = new ExamData();
//    private ExamData(){}
//    public static ExamData getSelf() {
//        return self;
//    }

    private String Vid_Flag = "";
    private String Customer_Exam_No = "";
    private String APP_NO = "";
    private String APP_NO_NAME = "";
    private String OPD_DATE = "";
    private String Questionnaire = "";
    private String Questionnaire_Write = "";
    private String QuestionnaireURL = "";
    private String ErrMsg = "null";

    public ExamData(String vid_Flag, String customer_Exam_No, String APP_NO, String APP_NO_NAME,
                    String OPD_DATE, String questionnaire, String questionnaire_Write,
                    String questionnaireURL, String errMsg) {
        Vid_Flag = vid_Flag;
        Customer_Exam_No = customer_Exam_No;
        this.APP_NO = APP_NO;
        this.APP_NO_NAME = APP_NO_NAME;
        this.OPD_DATE = OPD_DATE;
        Questionnaire = questionnaire;
        Questionnaire_Write = questionnaire_Write;
        QuestionnaireURL = questionnaireURL;
        ErrMsg = errMsg;
    }

    //    public void clearAll() {
//        Vid_Flag = "";
//        Customer_Exam_No = "";
//        APP_NO = "";
//        APP_NO_NAME = "";
//        OPD_DATE = "";
//        Questionnaire = "";
//        Questionnaire_Write = "";
//        QuestionnaireURL = "";
//        ErrMsg = null;
//    }

    public String getVid_Flag() {
        return Vid_Flag;
    }

    public void setVid_Flag(String vid_Flag) {
        Vid_Flag = vid_Flag;
    }

    public String getCustomer_Exam_No() {
        return Customer_Exam_No;
    }

    public void setCustomer_Exam_No(String customer_Exam_No) {
        Customer_Exam_No = customer_Exam_No;
    }

    public String getAPP_NO() {
        return APP_NO;
    }

    public void setAPP_NO(String APP_NO) {
        this.APP_NO = APP_NO;
    }

    public String getAPP_NO_NAME() {
        return APP_NO_NAME;
    }

    public void setAPP_NO_NAME(String APP_NO_NAME) {
        this.APP_NO_NAME = APP_NO_NAME;
    }

    public String getOPD_DATE() {
        return OPD_DATE;
    }

    public void setOPD_DATE(String OPD_DATE) {
        this.OPD_DATE = OPD_DATE;
    }

    public String getQuestionnaire() {
        return Questionnaire;
    }

    public void setQuestionnaire(String questionnaire) {
        Questionnaire = questionnaire;
    }

    public String getQuestionnaire_Write() {
        return Questionnaire_Write;
    }

    public void setQuestionnaire_Write(String questionnaire_Write) {
        Questionnaire_Write = questionnaire_Write;
    }

    public String getQuestionnaireURL() {
        return QuestionnaireURL;
    }

    public void setQuestionnaireURL(String questionnaireURL) {
        QuestionnaireURL = questionnaireURL;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }
}
