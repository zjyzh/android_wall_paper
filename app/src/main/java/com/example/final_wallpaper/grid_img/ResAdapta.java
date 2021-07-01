package com.example.final_wallpaper.grid_img;

import android.app.Activity;
//import android.app.FragmentTransaction;
//import android.app.FragmentManager;
//import android.app.FragmentManager;
//import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    Fragment context;
    public ResAdapta(Fragment context) {
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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.img_cell, parent, false));


//        holder.itemView.setOnClickListener(view -> {
//            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
//
//            FragmentTransaction ft2 = manager.beginTransaction();
//            PagerFragment pagerFragment = new PagerFragment();
//            pagerFragment.setArguments(bundle);
//            ft2.replace(R.id.fragment_middle, pagerFragment ); //fragment_a为FragmentA的id
//            ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft2.addToBackStack(null);
//            ft2.commit();
//        });

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ArrayList l = new ArrayList(getCurrentList());
            bundle.putParcelableArrayList("PHOTO_LIST", l);
            bundle.putInt("PHOTO_POSITION", holder.getAdapterPosition());


            PagerFragment pagerFragment = new PagerFragment();
            pagerFragment.setArguments(bundle);
            FragmentManager manager = (context).getParentFragmentManager();

            FragmentTransaction fragmentTransaction=manager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_home,pagerFragment);
//              fragmentTransaction.add(R.id.fragment_home, pagerFragment );
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();




//            manager.beginTransaction().replace(R.id.fragment_home, pagerFragment, "fragment")
//                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commitAllowingStateLoss();


//            FragmentTransaction ft2 = manager.beginTransaction();
//
//            ft2.replace(R.id.fragment_home, pagerFragment ); //fragment_a为FragmentA的id
//            ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft2.addToBackStack(null);
//            ft2.commit();

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.shimmerLayout.setShimmerColor(0x55ffffff);
        holder.shimmerLayout.setShimmerAngle(0);
        holder.shimmerLayout.startShimmerAnimation();

//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
//        layoutParams.height = getItem(position).photoHeight;
//        holder.imageView.setLayoutParams(layoutParams);
        Glide.with(holder.itemView)
                .load(getItem(position).previewUrl)
                .placeholder(R.drawable.source_focus)
                .error(R.drawable.source_unfocus)
                .override(480, getItem(position).photoHeight)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        if (holder.shimmerLayout != null) {
                            holder.shimmerLayout.stopShimmerAnimation();
                        }
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

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    ShimmerLayout shimmerLayout;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_cell);
        shimmerLayout = itemView.findViewById(R.id.shimmer_simgle);
    }
}