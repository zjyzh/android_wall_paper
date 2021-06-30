package com.example.final_wallpaper.ui.middle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.final_wallpaper.MainActivity;
import com.example.final_wallpaper.R;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MiddleViewModel extends AndroidViewModel {

//    代表图片集合，将在外边遍历到的文件夹下面的图片路径保存下来
    public MutableLiveData<Set<String>> mutiImgs;

    //    保存外部状态
    private SavedStateHandle handle;

    public MiddleViewModel(@NonNull Application application, SavedStateHandle handler){
        super(application);
        mutiImgs = new MutableLiveData<>();
        mutiImgs.setValue(new HashSet<>());
        this.handle = handler;
        if(!handle.contains(MainActivity.BACKGROUND_KEY_NUM)){
            load();
        }
    }

    /*
     * @author zjy
     * @param
     * @return void
     * @date 17:06 2021-06-30
     * @description 将保存的状态恢复
     */
    private void load() {

        SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        Set<String> x = shp.getStringSet(MainActivity.MUTI_FILE_NUM,this.mutiImgs.getValue());
        this.mutiImgs.setValue(x);
        handle.set(MainActivity.MUTI_FILE_NUM, x);
    }

    /*
     * @author zjy
     * @param
     * @return void
     * @date 17:06 2021-06-30
     * @description 状态保存，将图片集合保存起来
     */
    String shpName = "imgList";
    public void saveImgs(){
        Log.e("saveImgs","save");
        SharedPreferences shp =getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putStringSet(MainActivity.MUTI_FILE_NUM,mutiImgs.getValue());
        editor.apply();
    }

}