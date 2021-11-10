package com.zeke.ktx.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * author：ZekeWang
 * date：2021/4/16
 * description：文件工具
 * 未经过校验
 */
public final class FileUtils {

    /**
     * 根据uri获取文件真实路径
     * @param context context
     * @param uri     文件uri
     * @return  真实路径
     */
    @SuppressLint("ObsoleteSdkInt")
    public static String getFilePathByUri(Context context, Uri uri) {
        String scheme = uri.getScheme();
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = uri.getPath();
            return path;
        }

        /**
         * 以 content:// 开头的,
         * <4.4   如: content://media/extenral/images/media/17766
         * >=4.4  如: content://com.android.providers.media.documents/document/image%3A235700
         */
        if(ContentResolver.SCHEME_CONTENT.equals(scheme)){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){ //<4.4
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        if (columnIndex > -1) {
                            path = cursor.getString(columnIndex);
                        }
                    }
                    cursor.close();
                }
                return path;
            } else {
                if (DocumentsContract.isDocumentUri(context, uri)) {
                    if (isExternalStorageDocument(uri)) {
                        // ExternalStorageProvider
                        String docId = DocumentsContract.getDocumentId(uri);
                        String[] split = docId.split(":");
                        String type = split[0];
                        if ("primary".equalsIgnoreCase(type)) {
                            path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        }else {
                            path = "/storage/".concat(type).concat("/").concat(split[1]);
                        }
                        return path;
                    } else if (isDownloadsDocument(uri)) {
                        // DownloadsProvider
                        String id = DocumentsContract.getDocumentId(uri);
                        Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                Long.parseLong(id));
                        path = getDataColumn(context, contentUri, null, null);
                        return path;
                    } else if (isMediaDocument(uri)) {
                        // MediaProvider
                        String docId = DocumentsContract.getDocumentId(uri);
                        String[] split = docId.split(":");
                        String type = split[0];
                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                        final String selection = "_id=?";
                        final String[] selectionArgs = new String[]{split[1]};
                        path = getDataColumn(context, contentUri, selection, selectionArgs);
                        return path;
                    }
                }
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 外部储存文档
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

     /**
     * 是否是下载文档
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 是否是图片、影音文档
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
