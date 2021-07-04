package com.example.little_wallpaper.grid_img;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

;
/*
 * @author zjy
 * @param null
 * @return
 * @date 16:42 2021-07-04
 * @description 这个是主页上，有关网络的那个类的适配器
 */
public class WebRecycleViewAdapter extends ListAdapter<PhotoItem, MyViewHolder> {
    Fragment context;
    public WebRecycleViewAdapter(Fragment context) {
        super(new DiffUtil.ItemCallback<PhotoItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull PhotoItem oldItem, @NonNull PhotoItem newItem) {
                return oldItem.photoId == newItem.photoId;
            }

            @Override
            public boolean areContentsTheSame(@NonNull PhotoItem oldItem, @NonNull PhotoItem newItem) {
                return oldItem.previewUrl.equals(newItem.previewUrl);
            }
        });
        this.context = context;

    }

    PagerFragment pagerFragment;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.img_cell, parent, false));
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ArrayList l = new ArrayList(getCurrentList());
            bundle.putParcelableArrayList("PHOTO_LIST", l);
            bundle.putInt("PHOTO_POSITION", holder.getAdapterPosition());
            FragmentManager manager = (context).getParentFragmentManager();
            FragmentTransaction fragmentTransaction=manager.beginTransaction();

//            只会保存一个pagerFragment对象，然后通过hide和show显示
            if(pagerFragment == null){
                pagerFragment = new PagerFragment((context).getParentFragmentManager());
                fragmentTransaction.add(R.id.fragment_home,pagerFragment);
            }
            pagerFragment.setArguments(bundle);
            fragmentTransaction
                    .show(pagerFragment);
            fragmentTransaction.commit();
        });
        return holder;
    }

    /*
     * @author zjy
     * @param holder
     * @param position
     * @return void
     * @date 16:47 2021-07-04
     * @description 加载单个图片卡片
     */

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.shimmerLayout.setShimmerColor(0x55ffffff);
        holder.shimmerLayout.setShimmerAngle(0);
        holder.shimmerLayout.startShimmerAnimation();

        Glide.with(holder.itemView)
                .load(getItem(position).previewUrl)
                .placeholder(R.drawable.source_focus)
                .error(R.drawable.source_unfocus)
                .override(480, getItem(position).photoHeight)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (holder.shimmerLayout != null) {
                            holder.shimmerLayout.stopShimmerAnimation();
                        }

                        return false;
                    }
                })
                .into(holder.imageView);
    }



}

/*
 * @author zjy
 * @param null
 * @return
 * @date 16:45 2021-07-04
 * @description 单个图片卡片的布局
 */
class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    ShimmerLayout shimmerLayout;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_cell);
        shimmerLayout = itemView.findViewById(R.id.shimmer_simgle);
    }
}