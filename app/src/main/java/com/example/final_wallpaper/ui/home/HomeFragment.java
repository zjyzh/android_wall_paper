package com.example.final_wallpaper.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.final_wallpaper.R;
import com.example.final_wallpaper.ToFragmentListener;
import com.example.final_wallpaper.databinding.FragmentHomeBinding;
import com.example.final_wallpaper.grid_img.Muti_img;
//import com.example.final_wallpaper.grid_img.Muti_img_fragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ToFragmentListener {

    private HomeViewModel homeViewModel;
    private View root;
//

    //    配置fragment的状态保持
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("imgUrl", HomeViewModel.mImageUrl);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        将子类的fragment进行替换，这样就能初始化图片画廊
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.muti_img_liner, new Muti_img());
        transaction.commit();
    }

    public FragmentHomeBinding fragmentHomeBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//       通过ViewModel管理数据
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater);

        root = fragmentHomeBinding.getRoot();
        Bundle b = getArguments();
        if (null != b) {
            HomeViewModel.mImageUrl = b.getString("imgUrl");
            Log.e("Bundle fragment : ",HomeViewModel.mImageUrl  );
        }

//        因为横竖屏幕切换的时候，mainActivity传来的数据不会保存，所以需要保存
        if(savedInstanceState != null){
            HomeViewModel.mImageUrl = (String) savedInstanceState.get("imgUrl");
        }

//        Log.e("FragmentHomeBinding    ", HomeViewModel.mImageUrl);
//        设置初始化的imageUrl
        fragmentHomeBinding.setImageUrl(HomeViewModel.mImageUrl);
        homeViewModel.setCardImg(root.findViewById(R.id.home_card_img) ,HomeViewModel.mImageUrl);

        return fragmentHomeBinding.getRoot();

    }

    @Override
    public void onTypeClick(String imgUrl) {
        Log.e("onTypeClick : ",imgUrl );
        homeViewModel.setCardImg(root.findViewById(R.id.home_card_img) ,imgUrl);
        homeViewModel.saveImgUrl(imgUrl);
        fragmentHomeBinding.setImageUrl(imgUrl);
    }
}