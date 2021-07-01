package com.example.final_wallpaper.grid_img;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class PhotoItem implements Parcelable {
    @SerializedName("id")
    int photoId;
    @SerializedName("webformatURL")
    String previewUrl;
    @SerializedName("largeImageURL")
    String fullUrl;
    @SerializedName("webformatHeight")
    int photoHeight;

    protected PhotoItem(Parcel in) {
        photoId = in.readInt();
        previewUrl = in.readString();
        fullUrl = in.readString();
        photoHeight = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photoId);
        dest.writeString(previewUrl);
        dest.writeString(fullUrl);
        dest.writeInt(photoHeight);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        @Override
        public PhotoItem createFromParcel(Parcel in) {
            return new  PhotoItem(in);
        }

        @Override
        public PhotoItem[] newArray(int size) {
            return new  PhotoItem[size];
        }
    };
}
