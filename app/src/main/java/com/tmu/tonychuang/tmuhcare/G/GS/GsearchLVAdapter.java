package com.tmu.tonychuang.tmuhcare.G.GS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmu.tonychuang.tmuhcare.R;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyDataModal.ExamData;
import com.tmu.tonychuang.tmuhcare.Z_Other.MyModal.MyDateSFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import me.grantland.widget.AutofitHelper;

/**
 * Created by TonyChuang on 2016/7/18.
 */
public class GsearchLVAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater myInflater;
    private ArrayList<ExamData> examDatas = new ArrayList<>();
    private ViewTag viewTag;

    public GsearchLVAdapter(Context context, ArrayList<ExamData> examDatas) {
        this.context = context;
        this.myInflater = LayoutInflater.from(context);
        this.examDatas = examDatas;
    }

    @Override
    public int getCount() {
        return examDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return examDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // 取得listItem容器 view
            convertView = myInflater.inflate(R.layout.item_examdata, null);

            // 建構listItem內容view
            viewTag = new ViewTag(
                    (TextView) convertView.findViewById(R.id.examNameTV),
                    (TextView) convertView.findViewById(R.id.examDateTV),
                    (ImageView) convertView.findViewById(R.id.qisIV));
            // 設置容器內容
            convertView.setTag(viewTag);
        } else {
            viewTag = (ViewTag) convertView.getTag();
        }

        ExamData examData = examDatas.get(position);
        String titleStr = examData.getAPP_NO_NAME();
        int wrapIndex = titleStr.indexOf("(");
        if (wrapIndex != -1 && wrapIndex != 0) {
            titleStr = titleStr.substring(0, wrapIndex) + "\n" + titleStr.substring(wrapIndex);
        }
        viewTag.examNameTV.setText(titleStr);
        Date examdate = new Date();
        try {
            examdate = MyDateSFormat.getFrmt_yyyyMMdd().parse(examData.getOPD_DATE());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String examdateStr = MyDateSFormat.getFrmt_yMd().format(examdate);
        viewTag.examDateTV.setText(examdateStr);
        if (examData.getQuestionnaire().equals("Y")) {
            if (examData.getQuestionnaire_Write().equals("Y")) {
                viewTag.qisIV.setImageResource(R.drawable.happy_blue);
            } else {
                viewTag.qisIV.setImageResource(R.drawable.question_yellow);
            }
        } else {
            viewTag.qisIV.setImageResource(android.R.color.transparent);
        }

        return convertView;
    }

    public class ViewTag {
        TextView examNameTV;
        TextView examDateTV;
        ImageView qisIV;

        public ViewTag(TextView examNameTV, TextView examDateTV, ImageView qisIV) {
            this.examNameTV = examNameTV;
            this.examDateTV = examDateTV;
            this.qisIV = qisIV;
            if (examNameTV.getText().length() < 25) {
                AutofitHelper.create(this.examNameTV).setMinTextSize(15).setMaxLines(2);
            } else {
                AutofitHelper.create(this.examNameTV).setMinTextSize(15).setMaxLines(3);
            }
            AutofitHelper.create(this.examDateTV);
        }
    }
}