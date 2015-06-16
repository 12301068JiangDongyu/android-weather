package pku.ss.liudong.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.lang.reflect.Field;

import pku.ss.liudong.activity.R;

/**
 * Created by liudong on 2015/4/4.
 */
public class PinYin {
    public static String converterToSpell(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i],
                            defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    public static int getPmImageResourceId(String currentPM){
        Class drawableClass = R.drawable.class;
        int pmId = -1;
        String pmStr = "0_50";
        if(currentPM != null){
            int pm = Integer.parseInt(currentPM);
            if(pm <= 50){
                pmStr = "0_50";
            }else if(pm <= 100){
                pmStr = "51_100";
            }else if(pm <= 150){
                pmStr = "101_150";
            }else if(pm <= 200){
                pmStr = "151_200";
            }else{
                pmStr = "201_300";
            }
        }
        //更新pm图片
        try{
            String pmImgName = "biz_plugin_weather_" + pmStr;
            Field pmField = drawableClass.getField(pmImgName);
            pmId = pmField.getInt(null);
        }catch (Exception e){
            if(-1 == pmId){
                pmId = R.drawable.biz_plugin_weather_51_100;
            }
        }finally {
            //pmImg.setImageResource(pmId);
        }
        return pmId;
    }

    public static int getWeatherImageResourceId(String currentWeather){
        Class drawableClass = R.drawable.class;
        int weatherId = -1;
        //更新温度图片
        try{
            String typeImgName = "biz_plugin_weather_" + PinYin.converterToSpell(currentWeather);
            Field imgField = drawableClass.getField(typeImgName);
            weatherId = imgField.getInt(null);
        }catch (Exception e){
            if(-1 == weatherId){
                weatherId = R.drawable.biz_plugin_weather_qing;
            }
        }finally {
            //weatherImg.setImageResource(weatherId);
        }
        return weatherId;
    }

}

