package com.example.final_wallpaper.ui.middle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.final_wallpaper.MainActivity;
import com.example.final_wallpaper.R;
//import com.example.final_wallpaper.util.OnRecyclerItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class RecycleAdapta extends ListAdapter<String, MyViewHolder> {
    private Set<String> items;

//    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    /*
     * description: 为每个view的item添加监听事件
     */
    public interface OnItemClickListener {
        void onItemClicked(View view, int position, ArrayList<String> imgList);
    }

    /*
     * @author zjy
     * @param clickListener
     * @return void
     * @date 17:19 2021-06-30
     * @description 设置监听事件
     */
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        
        this.mOnItemClickListener = clickListener;
    }


    public void convertList(){
        /*
         * @author zjy
         * @param
         * @return void
         * @date 17:19 2021-06-30
         * @description 
         */
        synchronized (list){
            list = new ArrayList(items);
        }
    }




    public void setItems(Set<String> items) {
        this.items = items;
    }

    public RecycleAdapta(Set<String> items) {

        super(new DiffUtil.ItemCallback<String>() {
            @Override
            public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                return oldItem.equals(newItem);
            }
            @Override
            public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                return oldItem.equals(newItem);
            }
        });

//        初始化两个数据
        if(items == null){
            this.items = new HashSet<>();
        }else{
            this.items = items;
        }
        this.list = new ArrayList<>(items);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public   MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new
                MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.img_cell, parent, false), mOnItemClickListener, list);
        return holder;
    }


    ArrayList<String> list ;



    @Override
    public void onBindViewHolder(@NonNull   MyViewHolder holder, int position) {
//        配置shimmerLayout的东西
        holder.shimmerLayout.setShimmerColor(0x55ffffff);
        holder.shimmerLayout.setShimmerAngle(0);
        holder.shimmerLayout.startShimmerAnimation();

//        加载图片
        Glide.with(holder.itemView)
                .load(list.get(position))
                .placeholder(R.drawable.source_focus)
                .centerCrop()

                .override(480,640)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if(holder.shimmerLayout != null){
                            holder.shimmerLayout.stopShimmerAnimation();
                        }
                        return false;
                    }



                })
                .into(holder.imageView);
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    ShimmerLayout shimmerLayout;

//    //声明自定义的监听接口
//    private RecycleAdapta.OnItemClickListener monItemClickListener;

    public MyViewHolder(@NonNull View itemView,final RecycleAdapta.OnItemClickListener onClickListener, ArrayList<String> imgList) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_cell);
        shimmerLayout = itemView.findViewById(R.id.shimmer_simgle);

//        每个子类有自己的点击事件
        itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                int position = getAdapterPosition();
                //确保position值有效
                if (position != RecyclerView.NO_POSITION) {
                    onClickListener.onItemClicked(view, position,imgList);
                }
            }
        });

    }
}