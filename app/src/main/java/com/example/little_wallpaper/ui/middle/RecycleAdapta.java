package com.example.little_wallpaper.ui.middle;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.little_wallpaper.R;
import java.util.ArrayList;
import io.supercharge.shimmerlayout.ShimmerLayout;

/*
 * @author zjy
 * @param null
 * @return
 * @date 16:26 2021-07-04
 * @description 为相册写的ListAdapter
 */

public class RecycleAdapta extends ListAdapter<String, MyViewHolder> {
    private ArrayList<String> items;
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
     * @description 设置监听事件，这个监听事件是从外边传过来的
     */
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }


    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public RecycleAdapta(ArrayList<String> items) {

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
            this.items = new ArrayList<>();
        }else{
            this.items = items;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public   MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new
                MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.img_cell, parent, false), mOnItemClickListener, items);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull   MyViewHolder holder, int position) {
//        配置shimmerLayout的东西
        holder.shimmerLayout.setShimmerColor(0x55ffffff);
        holder.shimmerLayout.setShimmerAngle(0);
        holder.shimmerLayout.startShimmerAnimation();
        holder.imgList = items;
//        加载图片
        Glide.with(holder.itemView)
                .load(items.get(position))
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
    ArrayList<String> imgList;
//    //声明自定义的监听接口
    public MyViewHolder(@NonNull View itemView,final RecycleAdapta.OnItemClickListener onClickListener, ArrayList<String> imList) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_cell);
        shimmerLayout = itemView.findViewById(R.id.shimmer_simgle);
        this.imgList = imList;
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

    public void setImgList(ArrayList<String> imgList) {
        this.imgList = imgList;
    }
}