package com.example.little_wallpaper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.bumptech.glide.Glide;


//继承自androidViewModel，能拿到上下文对象
public class MainViewModel extends AndroidViewModel {

//    临时保存数据
    private final SavedStateHandle handle;

//    全局的背景图片，用于动态变化
    public MutableLiveData<String> globalBackgroundImg;

    // 全局设置，设置是锁定屏幕还是主屏幕
    public MutableLiveData<Integer> ScreenSetting;


    // 全局设置，设置间隔多久换壁纸
    public MutableLiveData<Integer> timeIntervalSetting;

    //    初始化
    public MainViewModel(@NonNull Application application, SavedStateHandle handler){
        super(application);
        this.handle = handler;
        globalBackgroundImg = new MutableLiveData<String> (String.valueOf(R.mipmap.background1)  );
        ScreenSetting = new MutableLiveData<>(GlobalSetting.mainScreen);
        timeIntervalSetting = new MutableLiveData<>(GlobalSetting.oneMinute);
        if(!handle.contains(MainActivity.GlobalBackground)){
            load();
        }
    }

//    从外部文件加载
    public void load(){
        SharedPreferences shp = getApplication().getSharedPreferences(MainActivity.GlobalBackground, Context.MODE_MULTI_PROCESS);
        String x = shp.getString(MainActivity.GlobalBackground,String.valueOf(R.mipmap.background1));
        handle.set(MainActivity.GlobalBackground,x);
    }

//    保存文件的路径，放在SharedPreferences里面
    public void saveImgUrl(String imgUrl){
        this.getSavedImageUrl().setValue(imgUrl);
//        Log.e(" saveImgUrl ", "saveImgUrl    "+this.getSavedImageUrl().getValue());
        SharedPreferences shp = getApplication().getSharedPreferences(MainActivity.GlobalBackground,Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = shp.edit();
        editor.putString(MainActivity.GlobalBackground,getSavedImageUrl().getValue());
        handle.set(MainActivity.GlobalBackground, getSavedImageUrl().getValue());
        globalBackgroundImg.setValue(imgUrl);
        editor.apply();
    }

// 找到需要的数据
    public MutableLiveData<String> getSavedImageUrl() {
        if(!handle.contains(MainActivity.GlobalBackground)){
            handle.set(MainActivity.GlobalBackground,String.valueOf(R.mipmap.background1));
        }
        return handle.getLiveData(MainActivity.GlobalBackground);
    }

//    加载图片到imageView里面，利用databinding
    @BindingAdapter("profileMainImage")
    public static void profileMainImage(ImageView view, String imageUrl) {
//        设置mainActivity的主要图片
        try {
            Integer img =  Integer.valueOf( imageUrl );
            Glide.with(view.getContext())
                    .load(img)
                    .centerCrop()
//                    .fitCenter()
                    .into(view);
        }catch (Exception e){
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .centerCrop()

//                    .fitCenter()
                    .into(view);
        }

    }

}
