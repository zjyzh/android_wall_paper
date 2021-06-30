package com.example.final_wallpaper.ui.middle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.final_wallpaper.MainActivity;
import com.example.final_wallpaper.MainViewModel;
//import com.example.final_wallpaper.MyViewModelFactory;
import com.example.final_wallpaper.R;
import com.example.final_wallpaper.util.FileUtil;
//import com.example.final_wallpaper.util/.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;
import static com.example.final_wallpaper.util.FileUtil.getPath;
import static com.example.final_wallpaper.util.FileUtil.isImgFile;
import static com.example.final_wallpaper.util.FileUtil.readPath;

public class MiddleFragment extends Fragment {

    private MiddleViewModel middleViewModel;
    RecycleAdapta recycleAdapta;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        初始化viewModel
        middleViewModel =
                new ViewModelProvider(this,
                        new SavedStateViewModelFactory(getActivity().getApplication(), this))
                        .get(MiddleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_middle, null, false);

//        初始化recycleView
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view);
//         设置布局管理器
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        设置适配器
        recycleAdapta = new RecycleAdapta(middleViewModel.mutiImgs.getValue());
        recyclerView.setAdapter(recycleAdapta);

//        设置监听请求
        recycleAdapta.setOnItemClickListener(new RecycleAdapta.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClicked(View view, int position, ArrayList<String> imgList) {
                Toast.makeText(getContext(), "You clicked item " + position +imgList.get(position), Toast.LENGTH_SHORT).show();
                FileUtil.setWallPaperAndBackground(getContext(),imgList.get(position));
            }
        });

//        找到图片，设置监听请求
        ImageView imageView = root.findViewById(R.id.muti_img_choose);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, MUTI_PICTURE_RESULT_CODE);
        });

//        观察数据变化
        middleViewModel.mutiImgs.observe(this.getActivity(),
                (Observer<Set>) set ->
                {
                    recycleAdapta.setItems(set);
                    recycleAdapta.convertList();
                    recycleAdapta.notifyDataSetChanged();
                }
        );

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private int MUTI_PICTURE_RESULT_CODE = 0x112;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MUTI_PICTURE_RESULT_CODE && resultCode == RESULT_OK && null != data) {
            final Uri selectedImages = data.getData();
//            拿到返回的文件夹
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
// Check for the freshest data.
            getActivity().getContentResolver().takePersistableUriPermission(selectedImages, takeFlags);
//            遍历文件夹的内容
            readPath(selectedImages,getActivity(),recycleAdapta,middleViewModel);
        }
    }
}