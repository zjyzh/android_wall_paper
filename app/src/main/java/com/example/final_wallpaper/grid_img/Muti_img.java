package com.example.final_wallpaper.grid_img;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_wallpaper.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Muti_img#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Muti_img extends Fragment {

    MyViewModel myViewModel;
    ResAdapta resAdapta;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Muti_img() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Muti_img.
     */
    // TODO: Rename and change types and number of parameters
    public static Muti_img newInstance(String param1, String param2) {
        Muti_img fragment = new Muti_img();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

//        if (myViewModel.getPhotoListLive().getValue() == null) {

        myViewModel.fetchData();
//        }

        myViewModel.getPhotoListLive().observe(getViewLifecycleOwner(), photoItems -> {
            resAdapta.submitList(photoItems);
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> myViewModel.fetchData());
        resAdapta = new ResAdapta();
        recyclerView.setAdapter(resAdapta);
//        return inflater.inflate(R.layout.fragment_muti_img, container, false);
        return root;

    }
}