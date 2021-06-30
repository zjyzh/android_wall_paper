package com.example.final_wallpaper.grid_img;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.final_wallpaper.R;
import com.example.final_wallpaper.grid_img.AdapterPagerPhoto;
import com.example.final_wallpaper.grid_img.PhotoItem;


import java.util.ArrayList;


public class PagerFragment extends Fragment {

    ViewPager2 viewPager2;
    TextView txtPhotoNum;





    public PagerFragment(){
        Log.e("Pager","create");
//        viewPager2 = requireView().findViewById(R.id.viewPager_photo);
//        txtPhotoNum = requireView().findViewById(R.id.txt_photo_tag);
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            ArrayList<PhotoItem> photoList = arguments.getParcelableArrayList("PHOTO_LIST");

            AdapterPagerPhoto adapterPagerPhoto = new AdapterPagerPhoto();
            viewPager2.setAdapter(adapterPagerPhoto);
            adapterPagerPhoto.submitList(photoList);

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    txtPhotoNum.setText((position+1)+"/"+photoList.size());
                }
            });

            viewPager2.setCurrentItem(getArguments().getInt("PHOTO_POSITION"), false);
            // then you have arguments
        } else {
//            AdapterPagerPhoto adapterPagerPhoto = new AdapterPagerPhoto();
//            viewPager2.setAdapter(adapterPagerPhoto);
//            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//                @Override
//                public void onPageSelected(int position) {
//                    super.onPageSelected(position);
//                    txtPhotoNum.setText((position+1)+"/"+photoList.size());
//                }
//            });
            // no arguments supplied...
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewPager2 = requireView().findViewById(R.id.viewPager_photo);
        txtPhotoNum = requireView().findViewById(R.id.txt_photo_tag);
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            ArrayList<PhotoItem> photoList = arguments.getParcelableArrayList("PHOTO_LIST");

            AdapterPagerPhoto adapterPagerPhoto = new AdapterPagerPhoto();
            viewPager2.setAdapter(adapterPagerPhoto);
            adapterPagerPhoto.submitList(photoList);

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    txtPhotoNum.setText((position+1)+"/"+photoList.size());
                }
            });

            viewPager2.setCurrentItem(getArguments().getInt("PHOTO_POSITION"), false);
            // then you have arguments
        } else {
//            AdapterPagerPhoto adapterPagerPhoto = new AdapterPagerPhoto();
//            viewPager2.setAdapter(adapterPagerPhoto);
//            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//                @Override
//                public void onPageSelected(int position) {
//                    super.onPageSelected(position);
//                    txtPhotoNum.setText((position+1)+"/"+photoList.size());
//                }
//            });
            // no arguments supplied...
        }
//        AdapterPagerPhoto adapterPagerPhoto = new AdapterPagerPhoto();
//        viewPager2.setAdapter(adapterPagerPhoto);
//        adapterPagerPhoto.submitList(photoList);
//
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                txtPhotoNum.setText((position+1)+"/"+photoList.size());
//            }
//        });
//
//        viewPager2.setCurrentItem(getArguments().getInt("PHOTO_POSITION"), false);
    }
}