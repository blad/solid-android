package com.btellez.solidandroid.network;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public interface NetworkBitmapClient {
    public void downloadInto(String url, ImageView imageView);

    public static class PicassoBitmapClient implements NetworkBitmapClient {
        @Override public void downloadInto(String url, ImageView imageView) {
            Picasso.with(imageView.getContext()).load(url).into(imageView);
        }
    }
}
