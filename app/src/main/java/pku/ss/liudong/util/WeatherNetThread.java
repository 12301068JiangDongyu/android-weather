package pku.ss.liudong.util;

import android.os.Handler;
import android.os.Message;


import pku.ss.liudong.activity.WeatherActivity;
import pku.ss.liudong.model.Forcast;
import pku.ss.liudong.model.Weather;

/**
 * Created by liudong on 2015/3/25.
 */
public class WeatherNetThread extends Thread{
    public static final int MESSAGE_NEW_WEATHER_INFO = 1;
    public static final String TAG = "WEATHER NET THREAD";
    //private String cityCode;
    private WeatherUtil weatherUtil;
    public WeatherNetThread(String cityCode){
        //this.cityCode = cityCode;
        this.weatherUtil = new WeatherUtil(cityCode);
    }
    public void run(){
        Weather weather = weatherUtil.getWeather();
        Handler handler = WeatherActivity.getHandler();
        Message msg = Message.obtain();
        msg.obj = weather;
        msg.what = MESSAGE_NEW_WEATHER_INFO;
        handler.sendMessage(msg);
    }
}
