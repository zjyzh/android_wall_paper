package com.example.final_wallpaper.grid_img;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_wallpaper.R;

import uk.co.senab.photoview.PhotoView;

public class AdapterPagerPhoto extends ListAdapter<PhotoItem, PagerPhotoViewHodel> {
    public AdapterPagerPhoto() {
        super(new DiffUtil.ItemCallback<PhotoItem>() {
            @Override
//            比较器
            public boolean areItemsTheSame(@NonNull PhotoItem oldItem, @NonNull PhotoItem newItem) {
                return oldItem.photoId == newItem.photoId;
            }

            @Override
            public boolean areContentsTheSame(@NonNull PhotoItem oldItem, @NonNull PhotoItem newItem) {
                return oldItem.fullUrl.equals(newItem.fullUrl);
            }
        });
    }

    @NonNull
    @Override
    public  PagerPhotoViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.img_cell, parent, false);
        return new  PagerPhotoViewHodel(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  PagerPhotoViewHodel holder, int position) {
//        加载图片
        Glide.with(holder.itemView)
                .load(getItem(position).fullUrl)
                .placeholder(R.drawable.source_focus)
                .into(holder.photoView);
    }
}

class PagerPhotoViewHodel extends RecyclerView.ViewHolder {
    PhotoView photoView;
//    每个图片的layout
    public PagerPhotoViewHodel(@NonNull View itemView) {
        super(itemView);
        photoView = itemView.findViewById(R.id.img_full_photo);
    }
}
