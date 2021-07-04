package com.example.little_wallpaper.grid_img;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.little_wallpaper.R;


/*
 * @author zjy
 * @param null
 * @return
 * @date 16:30 2021-07-04
 * @description 这个类是主页的那个网络布局的类，也就是一个fragment
 */
public class MutiImageFragment extends Fragment {

    MyViewModel myViewModel;
    WebRecycleViewAdapter webRecycleViewAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public MutiImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(root ==null){
            root = inflater.inflate(R.layout.fragment_muti_img, null, false);
        }

        recyclerView = root.findViewById(R.id.recycle_view);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
//        网格布局管理器
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

//        更新网络数据
        myViewModel.fetchData();

//        观察liveData
        myViewModel.getPhotoListLive().observe(getViewLifecycleOwner(), photoItems -> {
            webRecycleViewAdapter.submitList(photoItems);
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> myViewModel.fetchData());
        webRecycleViewAdapter = new WebRecycleViewAdapter(this);
        recyclerView.setAdapter(webRecycleViewAdapter);
        return root;

    }
}