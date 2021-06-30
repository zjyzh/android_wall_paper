package com.example.final_wallpaper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.Set;

import static android.content.ContentValues.TAG;


//继承自androidViewModel，能拿到上下文对象
public class MainViewModel extends AndroidViewModel {

    private SavedStateHandle handle;

//    初始化
    public MainViewModel(@NonNull Application application, SavedStateHandle handler){
        super(application);
        this.handle = handler;
        if(!handle.contains(MainActivity.BACKGROUND_KEY_NUM)){
            load();
        }
    }

//    定义名字
    String shpName = "SharePreference";

//    从外部文件加载
    public void load(){
        SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        String x = shp.getString(MainActivity.BACKGROUND_KEY_NUM,String.valueOf(R.mipmap.background1));
        handle.set(MainActivity.BACKGROUND_KEY_NUM,x);
    }

//    保存文件的路径，放在SharedPreferences里面
    public void saveImgUrl(String imgUrl){
        this.getSavedImageUrl().setValue(imgUrl);
        Log.e(" saveImgUrl ", "saveImgUrl    "+this.getSavedImageUrl().getValue());
        SharedPreferences shp = getApplication().getSharedPreferences(shpName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putString(MainActivity.BACKGROUND_KEY_NUM,getSavedImageUrl().getValue());
        editor.apply();
    }

// 找到需要的数据
    public MutableLiveData<String> getSavedImageUrl() {
        if(!handle.contains(MainActivity.BACKGROUND_KEY_NUM)){
            handle.set(MainActivity.BACKGROUND_KEY_NUM,String.valueOf(R.mipmap.background1));
        }
        return handle.getLiveData(MainActivity.BACKGROUND_KEY_NUM);
    }



//    加载图片到imageView里面，利用databinding
    @BindingAdapter("profileMainImage")
    public static void profileMainImage(ImageView view, String imageUrl) {
//        设置mainActivity的主要图片
        Log.e("profile_mainactivity", imageUrl);
        try {
            Integer img =  Integer.valueOf( imageUrl );
            Glide.with(view.getContext())
                    .load(img)
                    .fitCenter()
                    .into(view);
        }catch (Exception e){
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .fitCenter()
                    .into(view);
        }

    }

}
