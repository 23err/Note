package com.example.note;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private String TAG = "DownloadImageTask";

    public DownLoadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String avatarUrl = strings[0];
        Bitmap avatar = null;
        try {
            InputStream is = new URL(avatarUrl).openStream();
            avatar = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return avatar;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }


}
