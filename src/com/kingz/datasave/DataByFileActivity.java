package com.kingz.datasave;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.kingz.uiusingListViews.R;
import com.utils.ToastTools;

import java.io.*;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/5/22 12:58
 * description：数据存储方式1：文件形式
 */
public class DataByFileActivity extends Activity{
    public static final String FILE_NAME = "kingzFile";
    FileOutputStream fOut = null;
    FileInputStream fIn = null;
    BufferedReader reader = null;
    BufferedWriter writer = null;
    StringBuilder content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setGravity(Gravity.FILL_VERTICAL);
        root.setBackgroundColor(Color.BLACK);
        Button btn = new Button(this);
        root.addView(btn);
        setContentView(root);
        btn.setBackgroundColor(Color.GREEN);
        btn.setText("储存数据到文件");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastTools.showMgtvWaringToast(DataByFileActivity.this,"存储数据至文件");
                Log.i("KingZ","存储数据至文件");
                saveData("储存数据而已啦");
            }
        });
    }

    public void saveData(String data){
        try {
            fOut = openFileOutput("kingzFile", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fOut));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(writer != null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void readData(String fileName){
        String line;
        try {
            fIn = openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(fIn));
            while((line = reader.readLine()) != null){
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
