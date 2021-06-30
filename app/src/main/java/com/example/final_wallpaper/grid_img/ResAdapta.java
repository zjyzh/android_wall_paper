package com.example.final_wallpaper.grid_img;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.final_wallpaper.MainActivity;
import com.example.final_wallpaper.R;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

import static android.content.ContentValues.TAG;

public class ResAdapta extends ListAdapter<PhotoItem, MyViewHolder> {
    public ResAdapta() {
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

    }
//    public static MainActivity getActivityFromView(View view) {
//        if (null != view) {
//            Context context = view.getContext();
//            while (context instanceof ContextWrapper) {
//                if (context instanceof Activity) {
//                    return (MainActivity) context;
//                }
//                context = ((ContextWrapper) context).getBaseContext();
//            }
//        }
//        return null;
//    }



    @NonNull
    @Override
    public   MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new   MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.img_cell, parent, false));
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ArrayList l = new ArrayList(getCurrentList());
            bundle.putParcelableArrayList("PHOTO_LIST",  l );
//            bundle.putInt("PHOTO_POSITION", holder.getAdapterPosition());
            bundle.putInt("PHOTO_POSITION", holder.getAdapterPosition());
//            NavController navController = Navigation.findNavController( getActivityFromView(holder.itemView), R.id.nav_host_fragment);
//            NavController navController = Navigation.findNavController((Activity) holder.itemView.getContext(), R.id.nav_navigation);
//
//            navController.setGraph(R.navigation.muti_img_navi);
//            navController.navigate(R.id.pagerFragment,bundle);
//            NavController navController = Navigation.findNavController(holder.itemView);
//            navController.navigate(R.id.action_muti_img_to_pagerFragment, bundle);

//            FragmentManager fm = ((Activity)holder.itemView.getContext() ).getFragmentManager();
//            FragmentTransaction transaction = fm.beginTransaction();
//            PagerFragment pagerFragment = new PagerFragment();
//            transaction.replace(R.id.main_activity);
//            transaction.addToBackStack(null);
//            transaction.commit();
//            //将数据存入bundle，和Intent的使用方法有些类似
////            Bundle bundle = new Bundle();
////            bundle.putString("title",listData.get(postion).getTitle() );
////            bundle.putString("content", listData.get(postion).getContent());
//            pagerFragment.setArguments(bundle);
//            //用Log来验证是否将数据存入bundle
//            Log.i(TAG, bundle.toString());





        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull   MyViewHolder holder, int position) {
        holder.shimmerLayout.setShimmerColor(0x55ffffff);
        holder.shimmerLayout.setShimmerAngle(0);
        holder.shimmerLayout.startShimmerAnimation();

        Glide.with(holder.itemView)
                .load(getItem(position).previewUrl)
                .placeholder(R.drawable.source_focus)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       e.printStackTrace();
                        if(holder.shimmerLayout != null){
                            holder.shimmerLayout.stopShimmerAnimation();
                        }
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
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_cell);
        shimmerLayout = itemView.findViewById(R.id.shimmer_simgle);
    }
}