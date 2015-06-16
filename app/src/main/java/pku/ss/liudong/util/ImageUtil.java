package pku.ss.liudong.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by liudong on 2015/6/13.
 */
public class ImageUtil {
    public static Drawable getBitmapFromURL(String imageUrl) {
        Drawable dr = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            dr = new BitmapDrawable(myBitmap);
            return dr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
