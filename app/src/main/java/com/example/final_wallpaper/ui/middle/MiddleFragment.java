package com.example.final_wallpaper.ui.middle;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.final_wallpaper.MainViewModel;
import com.example.final_wallpaper.R;
import com.example.final_wallpaper.util.FileUtil;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.final_wallpaper.util.FileUtil.getFilePermission;
import static com.example.final_wallpaper.util.FileUtil.readPath;


public class MiddleFragment extends Fragment {
    private MiddleViewModel middleViewModel;
    RecycleAdapta recycleAdapta;
    View root;

    //3、声明引用
    private MyListener ml;

    //4.为引用赋值
    public void onAttach(Context context) {
        super.onAttach(context);
        ml = (MyListener) getActivity();
    }

    MainViewModel mainViewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        初始化viewModel
        middleViewModel =
                new ViewModelProvider(requireActivity()).get(MiddleViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        if (root == null)
            root = inflater.inflate(R.layout.fragment_middle, null, false);

//        初始化recycleView
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view);
//         设置布局管理器
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        设置适配器
        recycleAdapta = middleViewModel.getRecycleAdapta();
        recyclerView.setAdapter(recycleAdapta);

//        设置监听请求
        recycleAdapta.setOnItemClickListener(new RecycleAdapta.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClicked(View view, int position, ArrayList<String> imgList) {
//                这里设置的是recycleview的监听请求，代表当我点击了壁纸之后，就需要更新它
                FileUtil.setWallPaperAndBackground(WallpaperManager.getInstance(getContext()),
                        imgList.get(position),mainViewModel.ScreenSetting.getValue(),getContext());
//                下面的是传递消息，然后保存数据
                ml.middleFragmentMsg(imgList.get(position));
                mainViewModel.getSavedImageUrl().setValue(imgList.get(position));
                middleViewModel.setCardImg(getView().findViewById(R.id.single_card_img), imgList.get(position));
            }
        });

//        找到那个的按钮，设置监听请求
        Button imageView = root.findViewById(R.id.middle_muti_card);
        imageView.setOnClickListener(v -> {
            getFilePermission(getActivity());
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, MUTI_PICTURE_RESULT_CODE);
        });

//        观察数据变化
        middleViewModel.mutiImgs.observe(getViewLifecycleOwner(),
                (Observer<ArrayList>) set ->
                {
                    recycleAdapta.setItems(set);
                    recycleAdapta.notifyDataSetChanged();
                }
        );

//        更新数据变化
        mainViewModel.getSavedImageUrl().observe(getViewLifecycleOwner(),
                (Observer<String>) imgUrl -> middleViewModel.setCardImg(root.findViewById(R.id.single_card_img), mainViewModel.getSavedImageUrl().getValue())
        );

//        Bundle b = getArguments();
//        if (null != b) {
//            mainViewModel.getSavedImageUrl().setValue(b.getString("imgUrl"));
//        }

//        因为横竖屏幕切换的时候，mainActivity传来的数据不会保存，所以需要保存
//        if (savedInstanceState != null) {
//            mainViewModel.getSavedImageUrl().setValue((String) savedInstanceState.get("imgUrl"));
//        }
        middleViewModel.setCardImg(root.findViewById(R.id.single_card_img), mainViewModel.getSavedImageUrl().getValue());

        ImageButton imageButton = root.findViewById(R.id.deleteImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("删除")//设置对话框标题
                        .setMessage("确认清除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件，点击事件没写，自己添加
                                middleViewModel.mutiImgs.setValue(new ArrayList<>());
                                middleViewModel.saveImgs();
                                middleViewModel.getRecycleAdapta().notifyDataSetChanged();
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {//添加返回按钮

                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件，点击事件没写，自己添加
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });

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
            new Thread(() -> {
                try {
                    readPath(selectedImages, getActivity(), middleViewModel);
                } catch (Exception e) {
                    e.printStackTrace();
//                    "多线程问题"
                }
            }).start();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("imgUrl", MiddleViewModel.mImageUrl);
        super.onSaveInstanceState(outState);
    }

}