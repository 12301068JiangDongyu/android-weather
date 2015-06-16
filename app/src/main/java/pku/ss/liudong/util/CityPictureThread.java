package pku.ss.liudong.util;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.zip.GZIPInputStream;

import pku.ss.liudong.activity.WeatherActivity;
import pku.ss.liudong.model.Weather;

/**
 * Created by liudong on 2015/6/13.
 */
public class CityPictureThread extends Thread{
    public static final int MESSAGE_NEW_CITY_PICTURE = 2;
    private String cityCode;
    public CityPictureThread(String code){
        this.cityCode = code;
    }
    public void run(){
        String url = this.getCityPicture();
        Drawable dr = getPictureDrawable(url);   //获取网络图片资源
        Handler handler = WeatherActivity.getHandler();
        Message msg = Message.obtain();
        msg.obj = dr;
        msg.what = MESSAGE_NEW_CITY_PICTURE;
        handler.sendMessage(msg);
    }
    //获取网络图片资源
    private Drawable getPictureDrawable(String url){
        Drawable dr = null;
        if(!url.equals("")){
            //dr = ImageUtil.getBitmapFromURL("http://" + url);
            dr = ImageUtil.getBitmapFromURL(url);
            Log.d(WeatherActivity.TAG, url);
        }
        return dr;
    }
    private String getCityPicture(){
        //String urlString = "http://mobile100.zhangqx.com/WeatherDataCrawler/cityID/" + this.cityCode + ".json";
        String urlString = "http://fordawn.sinaapp.com/index.php/city/image/" + this.cityCode;
        String jsonStr = "";
        String url = "";
        try {
            String line;
            InputStream in = NetUtil.openHttpConnection(urlString);
            if(in == null){
                //do nothing...
                //return empty picture url
            }else{
                BufferedReader bf = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                while((line = bf.readLine()) != null){
                    jsonStr += line;
                }
                JSONObject obj = JSON.parseObject(jsonStr);
                /*
                JSONArray urls = obj.getJSONObject("locPhoto").getJSONArray("photoURLs");
                if(urls.size() != 0){
                    url = urls.getString(0);
                }
                */
                url = URLDecoder.decode(obj.getJSONObject("result").getString("imgurl"),"utf-8");
                //Log.d("City Picture>>>",url);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
