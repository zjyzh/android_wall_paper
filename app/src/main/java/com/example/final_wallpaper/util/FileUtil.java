package com.example.final_wallpaper.util;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.ViewModel;

import com.example.final_wallpaper.MainActivity;
import com.example.final_wallpaper.MainViewModel;
import com.example.final_wallpaper.ToFragmentListener;
import com.example.final_wallpaper.databinding.ActivityMainBinding;
import com.example.final_wallpaper.ui.middle.BackgroundTask;
import com.example.final_wallpaper.ui.middle.MiddleFragment;
import com.example.final_wallpaper.ui.middle.MiddleViewModel;
import com.example.final_wallpaper.ui.middle.RecycleAdapta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

/*
 * @author zjy
 * @date 16:59 2021-06-30
 * @description 文件工具类
 */
public class FileUtil {


    /*
     * @author zjy
     * @param context
     * @param bitmap
     * @return void
     * @date 16:59 2021-06-30
     * @description 设置锁屏壁纸
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void SetLockWallPaper(Context context, Bitmap bitmap) {

        try {
            WallpaperManager mWallManager = WallpaperManager.getInstance(context);
            if (mWallManager.isSetWallpaperAllowed()) {
                mWallManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                System.out.println("锁屏成功");
            } else {
                System.out.println("锁屏失败");
            }
        } catch (Throwable e) {
            Looper.prepare();
            Looper.loop();
            e.printStackTrace();
        }
    }

    /*
     * @author zjy
     * @param selectedImages 输入的文件夹uri
     * @param activity mainActivity
     * @param recycleAdapta 数据适配器，这里绑定有点过耦合了，但是目前只有它会用数据
     * @param middleViewModel viewmodel
     * @return void
     * @date 16:57 2021-06-30
     * @description 遍历文件夹下面的路径
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void readPath(final Uri selectedImages, final Activity activity,
                                RecycleAdapta recycleAdapta, MiddleViewModel middleViewModel) {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(activity, selectedImages);
        new BackgroundTask(activity) {
            @Override
            public void doInBackground() {
                int i = 0;
                try {
//                    遍历文件夹，返回文件路径
                    for (DocumentFile file : pickedDir.listFiles()) {
                        if (isImgFile(file.getUri().toString())) {
                            if (i < 10) {
                                System.out.println("readpath" + getPath(activity, file.getUri()));
                                i++;
                            }
                            middleViewModel.mutiImgs.getValue().add(getPath(activity, file.getUri()));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("threadException", "" + i);
                }
            }

            @Override
            public void onPostExecute() {
//                线程执行完了之后，通知对应的组件更新自己的数据
                recycleAdapta.convertList();
                middleViewModel.saveImgs();
                recycleAdapta.notifyDataSetChanged();
                //hear is result part same
                //same like post execute
                //UI Thread(update your UI widget)
            }
        }.execute();

    }


    /*
     * @author zjy
     * @param context 上下文
     * @param picturePath 图片文件的名字
     * @return void
     * @date 16:53 2021-06-30
     * @description 设置背景图片和锁屏壁纸
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setWallPaperAndBackground(Context context, String picturePath) {


        System.out.println("setWallPaperAndBackground");
        Bitmap bitmap = null;
        File file = new File(picturePath);

        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(picturePath);
        }
//                返回了图片路径之后，在这里设置
        Bitmap finalBitmap = bitmap;
        new Thread(() -> {
            try {
                SetWallPaper(finalBitmap, context);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("草草草草草草草草草草擦擦擦擦擦擦擦擦擦");
            }
        }).start();

        Bitmap finalBitmap1 = bitmap;
        new Thread(() -> {
            try {
                SetLockWallPaper(context, finalBitmap1);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("草草草草草草草草草草擦擦擦擦擦擦擦擦擦");
            }
        }).start();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void SetWallPaper(Bitmap bitmap, Context context) {

        try {
            WallpaperManager mWallManager = WallpaperManager.getInstance(context);
            mWallManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
            System.out.println("壁纸成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("壁纸失败");
        }

    }

    /*
     * @author zjy
     * @param context
     * @param uri
     * @return java.lang.String
     * @date 16:54 2021-06-30
     * @description 通过文件的uri，返回可以读取的内部存储路径，因为如果自己扫描
     * 文件的话，返回的路径不能直接读取，需要转换
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {


        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * *
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /*
     * @author zjy
     * @param fname
     * @return boolean
     * @date 16:55 2021-06-30
     * @description 判断一个文件是不是图片，简单的判断
     * 下文件名字的后续就行 ：ICO (Windows ICON image format), BMP, JPEG, WBMP, GIF, and PNG.
     */
    public static boolean isImgFile(String fname) {

        if (fname.toLowerCase().endsWith(".png")) {
            return true;
        }

        if (fname.toLowerCase().endsWith(".ico")) {
            return true;
        }

        if (fname.toLowerCase().endsWith(".wbmp")) {
            return true;
        }

        if (fname.toLowerCase().endsWith(".gif")) {
            return true;
        }

        if (fname.toLowerCase().endsWith(".jpg")) {
            return true;
        }

        if (fname.toLowerCase().endsWith(".jpeg")) {
            return true;
        }

        if (fname.toLowerCase().endsWith(".bmp")) {
            return true;
        }

        return false;
    }


}



