package com.parse.xml;

import com.App;
import com.zeke.kangaroo.utils.ZLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/10/9 10:57 <br>
 * description: Pull解析XMl文件 <br>
 */
public class ParserXmlByPull {
    private static final String TAG = "ParserXmlByPull";

    public static void parserFromAssetsFile(String path) {
        try {
            InputStream dataIns = App.getAppInstance().getAppContext().getResources().getAssets().open(path);
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(dataIns, "UTF-8");
            //Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.)
            int evtType = xmlPullParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = xmlPullParser.getName();
                        String addr = xmlPullParser.getAttributeName(1);
                        String nameSpace = xmlPullParser.getNamespace();
                        ZLog.d(TAG,String.format( "Find START_TAG. tag's Name:%s" +
                                ";tag's AttributeName_1:%s" +
                                ";tag's nameSpace:%s",name,addr,nameSpace));
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }

            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

    }
}
