package com.provider;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/15 22:47
 * description:
 */
public class GlobalContent {

    public static final String TAG = "GlobalContent";

    public static String AUTHORITY = "com.example.peopleprovider";
    public static String PATH_SINGLE = "people/#";
    public static String PATH_MULTIPLE = "people";
    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;

    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    public static final int MULTIPLE_PEOPLE = 1;
    public static final int SINGLE_PEOPLE = 2;
    public static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_SINGLE, SINGLE_PEOPLE);
        uriMatcher.addURI(AUTHORITY, PATH_MULTIPLE, MULTIPLE_PEOPLE);
    }

    public static void setAuthority(String authority){
        AUTHORITY = authority;
    }

    public static void setPathMultiple(String pathMultiple){
        PATH_MULTIPLE = pathMultiple;
    }

    public static void setPathSingle(String pathSingle){
        PATH_SINGLE = pathSingle;
    }



}
