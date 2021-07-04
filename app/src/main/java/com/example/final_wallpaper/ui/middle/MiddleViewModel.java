package com.example.final_wallpaper.ui.middle;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.bumptech.glide.Glide;
import com.example.final_wallpaper.MainActivity;
import com.example.final_wallpaper.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MiddleViewModel extends AndroidViewModel {

    public static String mImageUrl;
    //    代表图片集合，将在外边遍历到的文件夹下面的图片路径保存下来
    public MutableLiveData<ArrayList<String>> mutiImgs;

//    si
    public MutableLiveData<String> singleImgs;

    //    保存外部状态
    private SavedStateHandle handle;

//  把recycleView的适配器拿过来，为了动态通知它更新数据
    private RecycleAdapta recycleAdapta;
    public RecycleAdapta getRecycleAdapta() {
        if(recycleAdapta == null){
            recycleAdapta = new RecycleAdapta(mutiImgs.getValue());
        }
        return recycleAdapta;
    }

    /*
     * @author zjy
     * @param view
     * @param imageUrl
     * @return void
     * @date 16:22 2021-07-04
     * @description 设置cardView的背景图片，先转换为int，代表它是我的资源文件。
     * 如果没办法转换，代表是用户的文件
     */
    public void setCardImg(ImageView view, String imageUrl){

        if(imageUrl == null){
            imageUrl = String.valueOf(R.id.activity_background);
        }
        try {
            Integer img =  Integer.valueOf(imageUrl);
            // 这里之所以要这样做，就是glide不认string类型的资源文件
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

    public MiddleViewModel(@NonNull Application application, SavedStateHandle handler){
        super(application);
        this.recycleAdapta = recycleAdapta;
        mutiImgs = new MutableLiveData<>();
        mutiImgs.setValue(new ArrayList<>());
        singleImgs= new MutableLiveData<>();
        singleImgs.setValue(String.valueOf(R.id.single_card_img));
        this.handle = handler;
        if(!handle.contains(MainActivity.GlobalBackground)){
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
        Set<String> x =  shp.getStringSet(MainActivity.MutiImageList,new HashSet<>(this.mutiImgs.getValue()) );
        this.mutiImgs.setValue( new ArrayList<>(x));
        handle.set(MainActivity.MutiImageList, x);
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
//        Log.e("saveImgs","save");
        SharedPreferences shp =getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putStringSet(MainActivity.MutiImageList, new HashSet<>(mutiImgs.getValue()));
        editor.apply();
    }

}