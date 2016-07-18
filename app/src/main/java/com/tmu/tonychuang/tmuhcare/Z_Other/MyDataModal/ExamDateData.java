package com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal;

import java.util.ArrayList;

/**
 * Created by TonyChuang on 2016/6/29.
 */
public class ExamDateData {
    private static ExamDateData self = new ExamDateData();
    private ExamDateData(){}
    public static ExamDateData getSelf() {
        return self;
    }

    private ArrayList<String> ExamDateLst = new ArrayList<>();
    private String Questionnaire = "";
    private String QuestionnaireName = "";
    private String QuestionnaireURL = "";
    private String ErrMsg = "null";

    public void clearAll() {
        ExamDateLst.clear();
        Questionnaire = "";
        QuestionnaireName = "";
        QuestionnaireURL = "";
        ErrMsg = "null";
    }

    public ArrayList<String> getExamDateLst() {
        return ExamDateLst;
    }

    public void setExamDateLst(ArrayList<String> examDateLst) {
        ExamDateLst = examDateLst;
    }

    public String getQuestionnaire() {
        return Questionnaire;
    }

    public void setQuestionnaire(String questionnaire) {
        Questionnaire = questionnaire;
    }

    public String getQuestionnaireName() {
        return QuestionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        QuestionnaireName = questionnaireName;
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
