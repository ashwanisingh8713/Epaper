package com.theartofdev.edmodo.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.signature.ObjectKey;


/**
 * Created by ashwanisingh on 21/09/18.
 */

public class GlideUtil {

    public static void loadImageFromFragment(Fragment fragment, ImageView imageView, String url, int placeholderResId) {
        GlideApp.with(fragment)
                .load(url)
                .placeholder(placeholderResId)
                .into(imageView);
    }


    public static void loadImageFromFragment(Fragment fragment, ImageView imageView, String url) {
        GlideApp.with(fragment)
                .load(url)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(imageView);

    }

    public static void loadProfileImage(Context context, ImageView imageView, String url, int placeholderResId) {
        GlideApp.with(context)
                .load(url)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .placeholder(placeholderResId)
                .into(imageView);

    }
}
