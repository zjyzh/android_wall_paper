package com.example.little_wallpaper.grid_img;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.little_wallpaper.MainActivity;
import com.example.little_wallpaper.MainViewModel;
import com.example.little_wallpaper.R;
import com.example.little_wallpaper.ui.middle.MiddleViewModel;
import com.example.little_wallpaper.util.FileUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.little_wallpaper.util.FileUtil.isImgFile;

/*
 * @author zjy
 * @param null
 * @return
 * @date 16:33 2021-07-04
 * @description 这个是查看大图的类，点一下主页里面的图片，就能查看大图
 */
public class PagerFragment extends Fragment {

    ViewPager2 viewPager2;
    TextView txtPhotoNum;
    FragmentManager fragmentManager;
    private ImageView imageViewDownload;

    public PagerFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager; // 传来一个fragmnetMamager，为以后的fragment切换进行准备
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        imageViewDownload = view.findViewById(R.id.imageViewDownload);
        return view;
    }

    MainActivity mainActivity;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    /*
     * @author zjy
     * @param hidden
     * @return void
     * @date 16:35 2021-07-04
     * @description 当再一次跳转到这里的时候，就需要更新数据
     */
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        loadData();
    }

    public static String PhotoList = "PHOTO_LIST";
    public static String PhotoPosition =  "PHOTO_POSITION";

    private void loadData() {
        ArrayList<PhotoItem> photoList = getArguments().getParcelableArrayList(PhotoList);
        AdapterPagerPhoto adapterPagerPhoto = new AdapterPagerPhoto();
        viewPager2.setAdapter(adapterPagerPhoto);
        adapterPagerPhoto.submitList(photoList);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                txtPhotoNum.setText((position + 1) + "/" + photoList.size());
            }
        });

        viewPager2.setCurrentItem(getArguments().getInt(PhotoPosition), false);
    }

    MainViewModel mainViewModel;
    MiddleViewModel middleViewModel;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        middleViewModel =
                new ViewModelProvider(requireActivity()).get(MiddleViewModel.class);
        super.onActivityCreated(savedInstanceState);
        viewPager2 = requireView().findViewById(R.id.viewPager2);
        txtPhotoNum = requireView().findViewById(R.id.txt_photo_tag);
        Bundle arguments = getArguments();
        if (arguments != null) {
//            传值过来，使用viewPager2来进行切换
            ArrayList<PhotoItem> photoList = arguments.getParcelableArrayList(PhotoList);
            AdapterPagerPhoto adapterPagerPhoto = new AdapterPagerPhoto();
            viewPager2.setAdapter(adapterPagerPhoto);
            adapterPagerPhoto.submitList(photoList);
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    txtPhotoNum.setText((position + 1) + "/" + photoList.size());
                }
            });
            viewPager2.setCurrentItem(getArguments().getInt(PhotoPosition), false);
        }
        mainActivity = (MainActivity) getActivity();
        imageViewDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    try {
                        savePhoto();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ImageView returnBtn = mainActivity.findViewById(R.id.imageViewReturn);
//        左上角返回按钮的点击事件
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(PagerFragment.this);
                fragmentTransaction.commit();
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getActivity(), "按了返回键", Toast.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(PagerFragment.this);
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
            }


        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
        } else {
            Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void savePhoto() throws IOException {

        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        PagerPhotoViewHodel holder = (PagerPhotoViewHodel) recyclerView.findViewHolderForAdapterPosition(viewPager2.getCurrentItem());
        Bitmap bitmap = ((BitmapDrawable) holder.photoView.getDrawable()).getBitmap();
        OutputStream outputStream = null;
        try {
            Uri saveUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new ContentValues());
            outputStream = requireContext().getContentResolver().openOutputStream(saveUri);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {

                Toast.makeText(requireContext(), "存储成功", Toast.LENGTH_SHORT).show();
                String realPath =   FileUtil.getPath(getContext(), saveUri);
                if (isImgFile(realPath)) {
                    middleViewModel.mutiImgs.getValue().add(realPath);
                    middleViewModel.saveImgs();
                    middleViewModel.getRecycleAdapta().notifyDataSetChanged();
                    Log.e("saveSingle", middleViewModel.getRecycleAdapta().hashCode() + "");
                }
            } else {
                Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }finally {
            if(outputStream != null){
                outputStream.close();
            }
        }
    }

}