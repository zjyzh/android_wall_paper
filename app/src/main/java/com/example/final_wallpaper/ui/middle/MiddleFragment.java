package com.example.final_wallpaper.ui.middle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

//import com.example.final_wallpaper.MyViewModelFactory;
import com.example.final_wallpaper.MainViewModel;
import com.example.final_wallpaper.R;
import com.example.final_wallpaper.ToFragmentListener;
import com.example.final_wallpaper.ui.home.HomeViewModel;
//import com.example.final_wallpaper.ui.home.middleViewModel;
import com.example.final_wallpaper.util.FileUtil;
//import com.example.final_wallpaper.util/.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.example.final_wallpaper.util.FileUtil.readPath;


public class MiddleFragment extends Fragment  {


//    public static String mImageUrl = null;

    private MiddleViewModel middleViewModel;
    RecycleAdapta recycleAdapta;

//    MiddleViewModel middleViewModel;
    View root;
    
    //3、声明引用
    private MyListener ml;
    //4.为引用赋值
    public void onAttach(Context context) {
        super.onAttach(context);
        ml = (MyListener)getActivity();
        //传递消息方法，不一定在onAttach中实现
//        ml.middleFragmentMsg("消息");
    }

    MainViewModel mainViewModel;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        初始化viewModel
        middleViewModel =
                new ViewModelProvider(this,
                        new SavedStateViewModelFactory(getActivity().getApplication(), this))
                        .get(MiddleViewModel.class);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);



        if(root == null)
        root = inflater.inflate(R.layout.fragment_middle, null, false);

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
                ml.middleFragmentMsg(imgList.get(position));
                mainViewModel.getSavedImageUrl().setValue(imgList.get(position));
                middleViewModel.setCardImg(getView().findViewById(R.id.single_card_img), imgList.get(position));
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
        middleViewModel.mutiImgs.observe(getViewLifecycleOwner(),
                (Observer<Set>) set ->
                {
                    recycleAdapta.setItems(set);
                    recycleAdapta.convertList();
                    recycleAdapta.notifyDataSetChanged();
                }
        );
//        mainViewModel.getSavedImageUrl().observe(this.getActivity(), (Observer<String> img -> {
//
//        }));

        mainViewModel.getSavedImageUrl().observe(getViewLifecycleOwner(),
                (Observer<String>) imgUrl ->
                {
                    Log.e("livedataupdate", imgUrl);
                    middleViewModel.setCardImg(root.findViewById(R.id.single_card_img), mainViewModel.getSavedImageUrl().getValue());
                }
        );

        Bundle b = getArguments();
        if (null != b) {
            mainViewModel.getSavedImageUrl().setValue(b.getString("imgUrl") );
            Log.e("Bundle fragment : ",mainViewModel.getSavedImageUrl().getValue()  );
        }

//        因为横竖屏幕切换的时候，mainActivity传来的数据不会保存，所以需要保存
        if(savedInstanceState != null){
            mainViewModel.getSavedImageUrl().setValue((String) savedInstanceState.get("imgUrl") );
        }

        middleViewModel.setCardImg(root.findViewById(R.id.single_card_img),mainViewModel.getSavedImageUrl().getValue() );


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
                    readPath(selectedImages,getActivity(),recycleAdapta,middleViewModel);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("多线程问题");
                }
            }).start();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("imgUrl", MiddleViewModel.mImageUrl);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onTypeClick(String imgUrl) {
//        Log.e("onTypeClick : ",imgUrl );
//    }
}