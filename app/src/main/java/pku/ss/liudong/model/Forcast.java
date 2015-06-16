package pku.ss.liudong.model;

import java.io.Serializable;

/**
 * Created by liudong on 2015/6/11.
 */
public class Forcast  implements Serializable {
    private String day;
    private String low;
    private String high;
    private String fengli;
    private String fengxiang;
    private String typeDay;
    private String typeNight;
    public Forcast(){}

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
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

    public String getWeatherTypeString(){
        if(this.getTypeDay().equals(this.getTypeNight())){
            return this.getTypeDay();
        }else{
            return this.getTypeNight() + "转" + this.getTypeDay();
        }
    }
    public String getTempString(){
        return this.getLow() + "~" + this.getHigh();
    }
    @Override
    public String toString() {
        return "Forcast{" +
                "  day='" + day + '\'' +
                ", low='" + low + '\'' +
                ", high='" + high + '\'' +
                ", fengli='" + fengli + '\'' +
                ", fengxiang='" + fengxiang + '\'' +
                ", typeDay='" + typeDay + '\'' +
                ", typeNight='" + typeNight + '\'' +
                '}';
    }
}
