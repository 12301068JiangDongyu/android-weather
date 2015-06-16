package pku.ss.liudong.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by liudong on 2015/3/25.
 */
public class Weather implements Serializable{
    private String city;
    private String updatetime;
    private String date;
    private String day;
    private String wendu;
    private String shidu;
    private String fengli;
    private String fengxiang;
    private String typeDay;
    private String typeNight;
    private String aqi;
    private String pm25;
    private String quality;
    private String high;
    private String low;

    private Forcast[] forcastArr;


    public Weather() {
        forcastArr = new Forcast[4];
        for(int i = 0; i < 4; i++){
            forcastArr[i] = new Forcast();
        }
    }

    public String getDate() {
        return date;
    }

    public Forcast[] getForcastArr() {
        return forcastArr;
    }

    public void setForcastArr(Forcast[] forcastArr) {
        this.forcastArr = forcastArr;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }


    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }


    public String getTypeDay() {
        return typeDay;
    }

    public void setTypeDay(String typeDay) {
        this.typeDay = typeDay;
    }

    public String getTypeNight() {
        return typeNight;
    }

    public void setTypeNight(String typeNight) {
        this.typeNight = typeNight;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }
    public String getWeatherTypeString(){
        if(this.getTypeDay().equals(this.getTypeNight())){
            return this.getTypeDay();
        }else{
            return this.getTypeNight() + "转" + this.getTypeDay();
        }
    }
    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", wendu='" + wendu + '\'' +
                ", shidu='" + shidu + '\'' +
                ", fengli='" + fengli + '\'' +
                ", fengxiang='" + fengxiang + '\'' +
                ", typeDay='" + typeDay + '\'' +
                ", typeNight='" + typeNight + '\'' +
                ", aqi='" + aqi + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", quality='" + quality + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", forcastArr=" + Arrays.toString(forcastArr) +
                '}';
    }
}
