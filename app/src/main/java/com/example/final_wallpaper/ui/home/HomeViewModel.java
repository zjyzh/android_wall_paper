package com.example.final_wallpaper.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.example.final_wallpaper.MainActivity;
import com.example.final_wallpaper.MainViewModel;
import com.example.final_wallpaper.R;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    private String HomeImgUrl;

    public void saveImgUrl(String imgUrl){
        HomeImgUrl = imgUrl;
        mImageUrl = imgUrl;
    }

    public LiveData<String> getText() {
        return mText;
    }
    public static String mImageUrl = null;

    public void setCardImg(ImageView view, String imageUrl){
        Log.e("setCardImg", imageUrl);
        try {
            Integer img =  Integer.valueOf(imageUrl);
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


    @BindingAdapter("profileImage")
    public static void loadImage(ImageView view, String imageUrl) {
//        Glide.with(getActivity().getApplicationContext()).load(R.mipmap.background1).fitCenter().into(imageView);
        mImageUrl = imageUrl;
        Log.e("   " , "homeFragment loadImage   "+ HomeViewModel.mImageUrl);

//        ImageView imageView =
//        如果传进来的imageurl为null，那就加载默认的背景图片
        if(mImageUrl == null){
            mImageUrl = String.valueOf(R.mipmap.background1);
        }
        try {
            Integer img =  Integer.valueOf(mImageUrl);
            Glide.with(view.getContext())
                    .load(img)
                    .fitCenter()
                    .into(view);
        }catch (Exception e){
            Glide.with(view.getContext())
                    .load(mImageUrl)
                    .fitCenter()
                    .into(view);
        }

    }

}