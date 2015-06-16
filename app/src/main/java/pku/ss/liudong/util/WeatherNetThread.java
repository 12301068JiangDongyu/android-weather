package pku.ss.liudong.util;

import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import pku.ss.liudong.activity.WeatherActivity;
import pku.ss.liudong.model.Forcast;
import pku.ss.liudong.model.Weather;

/**
 * Created by liudong on 2015/3/25.
 */
public class WeatherNetThread extends Thread{
    public static final int MESSAGE_NEW_WEATHER_INFO = 1;
    public static final String TAG = "WEATHER NET THREAD";
    private String cityCode;
    public WeatherNetThread(String cityCode){
        this.cityCode = cityCode;
    }
    public void run(){
        try {
            String weatherUrl = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
            InputStream in = new GZIPInputStream(NetUtil.openHttpConnection(weatherUrl));
            Weather weather = parseWeatherInfo(in);
            Handler handler = WeatherActivity.getHandler();
            Message msg = Message.obtain();
            msg.obj = weather;
            msg.what = MESSAGE_NEW_WEATHER_INFO;
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private Weather parseWeatherInfo(InputStream in) throws IOException, XmlPullParserException {
        Weather weather = new Weather();
        Forcast[] forcastArr = weather.getForcastArr();
        int dateCount = -1;
        int highCount = -1;
        int lowCount = -1;
        int typeCount = 0;
        int fengxiangCount = -1;
        int fengliCount = -1;
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        while(parser.next() != XmlPullParser.END_DOCUMENT) {
            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    if(parser.getName().equalsIgnoreCase("city")){
                        parser.next();
                        weather.setCity(parser.getText());
                    }else if(parser.getName().equalsIgnoreCase("updatetime")){
                        parser.next();
                        weather.setUpdatetime(parser.getText());
                    }else if(parser.getName().equalsIgnoreCase("wendu")){
                        parser.next();
                        weather.setWendu(parser.getText());
                    }else if(parser.getName().equalsIgnoreCase("shidu")){
                        parser.next();
                        weather.setShidu(parser.getText());
                    }else if(parser.getName().equalsIgnoreCase("aqi")){
                        parser.next();
                        weather.setAqi(parser.getText());
                    }else if(parser.getName().equalsIgnoreCase("pm25")){
                        parser.next();
                        weather.setPm25(parser.getText());
                    }else if(parser.getName().equalsIgnoreCase("quality")){
                        parser.next();
                        weather.setQuality(parser.getText());
                    }else if(parser.getName().equalsIgnoreCase("fengli")){
                        fengliCount ++;
                        if(fengliCount == 0){
                            parser.next();
                            weather.setFengli(parser.getText());
                        }else{
                            if(fengliCount > 1 && fengliCount % 2 == 1){
                                parser.next();
                                forcastArr[(fengliCount-1)/2 - 1].setFengli(parser.getText());
                            }
                        }
                    }else if(parser.getName().equalsIgnoreCase("fengxiang")){
                        fengxiangCount ++;
                        if(fengxiangCount == 0){
                            parser.next();
                            weather.setFengxiang(parser.getText());
                        }else{
                            if(fengxiangCount > 1 && fengxiangCount % 2 == 1){
                                parser.next();
                                forcastArr[(fengxiangCount-1)/2 - 1].setFengxiang(parser.getText());
                            }
                        }
                    }else if(parser.getName().equalsIgnoreCase("date")){
                        dateCount ++;
                        if(dateCount == 0){
                            parser.next();
                            String dateStr = parser.getText();
                            weather.setDay(dateStr.substring(dateStr.length() - 3,dateStr.length()));
                        }else{
                            parser.next();
                            String dateStr2 = parser.getText();
                            forcastArr[dateCount-1].setDay(dateStr2.substring(dateStr2.length() - 3,dateStr2.length()));
                        }
                    }else if(parser.getName().equalsIgnoreCase("type")){
                        typeCount ++;
                        if(typeCount == 1){
                            parser.next();
                            weather.setTypeDay(parser.getText());
                        }else if(typeCount == 2){
                            parser.next();
                            weather.setTypeNight(parser.getText());
                        }else{
                            if(typeCount % 2 == 1){
                                parser.next();
                                forcastArr[(typeCount-1)/2 - 1].setTypeDay(parser.getText());
                            }else if(typeCount % 2 == 0){
                                parser.next();
                                forcastArr[(typeCount-2)/2 - 1].setTypeNight(parser.getText());
                            }
                        }
                    }else if(parser.getName().equalsIgnoreCase("high")){
                        highCount ++;
                        if(highCount == 0){
                            parser.next();
                            String highStr = parser.getText();
                            weather.setHigh(highStr.split(" ")[1]);
                        }else{
                            parser.next();
                            String highStr2 = parser.getText();
                            forcastArr[highCount-1].setHigh(highStr2.split(" ")[1]);
                        }
                    }
                    else if(parser.getName().equalsIgnoreCase("low")){
                        lowCount ++;
                        if(lowCount == 0){
                            parser.next();
                            String lowStr = parser.getText();
                            weather.setLow(lowStr.split(" ")[1]);
                        }else{
                            parser.next();
                            String lowStr2 = parser.getText();
                            forcastArr[lowCount-1].setLow(lowStr2.split(" ")[1]);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return weather;
    }
}
