package com.example.final_wallpaper.util;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import com.example.final_wallpaper.GlobalSetting;
import com.example.final_wallpaper.ui.middle.BackgroundTask;
import com.example.final_wallpaper.ui.middle.MiddleViewModel;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.IOException;
import java.util.List;

//import com.example.final_wallpaper.ToFragmentListener;

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
    public static void SetLockWallPaper(Bitmap bitmap, WallpaperManager mWallManager, Context context) {

        try {
            if (mWallManager.isSetWallpaperAllowed()) {
                mWallManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                makeToast("设置锁屏壁纸成功", context);
                bitmap.recycle();
            }
        } catch (Exception e) {
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
                                MiddleViewModel middleViewModel) {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(activity, selectedImages);
        DocumentFile[] files = pickedDir.listFiles();
        new BackgroundTask(activity) {
            @Override
            public void doInBackground() {
                try {
//                    遍历文件夹，返回文件路径
                    for (DocumentFile file : files) {
                        String filePath = getPath(activity, file.getUri());
//                        Log.e("path", filePath + "   " + file.getUri());
                        if (isImgFile(filePath)) {
                            middleViewModel.mutiImgs.getValue().add(filePath);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPostExecute() {
//                线程执行完了之后，通知对应的组件更新自己的数据
                middleViewModel.saveImgs();
                middleViewModel.getRecycleAdapta().notifyDataSetChanged();
            }
        }.execute();

    }


    /*
     * @author zjy
     * @param context 上下文
     * @param picturePath 图片文件的名字
     * @return void
     * @date 16:53 2021-06-30
     * @description 设置背景图片和锁屏壁纸，使用多线程运行
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setWallPaperAndBackground(WallpaperManager mWallManager, String picturePath, Integer screenModel, Context context) {

//        System.out.println("setWallPaperAndBackground " + picturePath);
        final Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        switch (screenModel) {
            case GlobalSetting
                    .lock_mainScreen: { // 锁屏和主屏幕都有
                new Thread(() -> {
                    try {
                        Looper.prepare();
                        SetWallPaper(bitmap, mWallManager,context);
                        Looper.loop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();


                new Thread(() -> {
                    try {
                        Looper.prepare();
                        SetLockWallPaper(bitmap, mWallManager,context);
                        Looper.loop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            }
            case GlobalSetting.lockScreen: { // 只有锁屏
                new Thread(() -> {
                    try {
                        Looper.prepare();
                        SetLockWallPaper(bitmap, mWallManager,context);
                        Looper.loop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            }
            case GlobalSetting.mainScreen: // 只有主屏幕

                new Thread(() -> {
                    try {
                        Looper.prepare();
                        SetWallPaper(bitmap, mWallManager,context);
                        Looper.loop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void SetWallPaper(Bitmap bitmap, WallpaperManager mWallManager, Context context) {
        try {
            mWallManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
            makeToast("设置主屏幕壁纸成功", context);
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SetWallPaper壁纸失败");
        }

    }

    /*
     * @author zjy
     * @param context
     * @param uri
     * @return java.lang.String
     * @date 16:54 2021-06-30
     * @description 通过文件的uri，返回可以读取的内部存储路径，因为如果自己扫描
     * 文件，返回的路径不能直接读取，需要转换
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
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            /**
             * 最关键在此，把options.inJustDecodeBounds = true;
             * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
             */
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(fname, options); // 此时返回的bitmap为null
            /**
             *options.outHeight为原始图片的高
             */
            if (options.outHeight > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    //    toast
    public static void makeToast(String str, Context context) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void getFilePermission(Context context) {
        XXPermissions.with(context)
                // 不适配 Android 11 可以这样写
                //.permission(Permission.Group.STORAGE)
                // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
//                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            makeToast("获取存储权限成功", context);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            makeToast("被永久拒绝授权，请手动授予存储权限", context);
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            makeToast("获取存储权限失败", context);
                        }
                    }
                });
    }
}



