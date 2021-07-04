package com.example.final_wallpaper.util;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.final_wallpaper.GlobalSetting;
import com.example.final_wallpaper.MainActivity;

import java.util.ArrayList;

import static com.example.final_wallpaper.util.FileUtil.makeToast;
/*
 * @author zjy
 * @param null
 * @return
 * @date 16:05 2021-07-04
 * @description 自动改变壁纸的服务
 */
public class WallPaperService extends Service {
    WallpaperManager wallpaperManager;
    int current = 0;
    private ArrayList<String> picPaths;

    @Override
    public void onCreate() {
        super.onCreate();
        wallpaperManager = WallpaperManager.getInstance(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    /*
     * @author zjy
     * @param intent
     * @param flags
     * @param startId
     * @return int
     * @date 15:56 2021-07-04
     * @description 开始服务，接受一堆参数，然后设置背景
     */
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(intent.getStringArrayListExtra(MainActivity.workerKey) != null){
            picPaths = intent.getStringArrayListExtra(MainActivity.workerKey);
        }
        else{
            picPaths = new ArrayList<>();
        }
        if(picPaths.size() ==0){
            return START_STICKY;
        }
//        Log.e("onStartCommand",picPaths.size() + "");
        Toast.makeText(getApplicationContext(),"开始服务",Toast.LENGTH_SHORT).show();
        current = (int) (  (Math.random() * picPaths.size() ) % picPaths.size());
        Integer screenSetting = intent.getIntExtra(MainActivity.screenSetting, GlobalSetting.mainScreen);
//        设置壁纸
        FileUtil.setWallPaperAndBackground( wallpaperManager, picPaths.get(current),screenSetting,this);

//        保存到SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.GlobalBackground, Context.MODE_MULTI_PROCESS);
        sharedPreferences.edit().putString(MainActivity.GlobalBackground, picPaths.get(current)).apply();

//       广播，通知activity更新背景
        Intent local = new Intent();
//        Action是为了更新
        local.setAction(MainActivity.UpdateAction);
        local.putExtra(MainActivity.GlobalBackground,picPaths.get(current));
        this.sendBroadcast(local);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        makeToast("服务停止", this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
