package pku.ss.liudong.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pku.ss.liudong.model.City;
import pku.ss.liudong.model.Province;

/**
 * Created by liudong on 2015/3/29.
 */
public class CityDB {
    public static final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";
    private SQLiteDatabase db;
    public CityDB(Context context,String path){
        db = context.openOrCreateDatabase(CITY_DB_NAME,context.MODE_PRIVATE,null);
    }
    public List<City> getAllCity(){
        List<City> cityList = new ArrayList<City>();
        Cursor c = db.rawQuery("select * from " + CITY_TABLE_NAME,null);
        while(c.moveToNext()){
            String id = c.getString(c.getColumnIndex("_id"));
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPy = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            City item = new City(id,province,city,number,firstPY,allPy,allFirstPY);
            cityList.add(item);
        }
        return cityList;
    }
    public List<Province> getAllProvince(){
        List<Province> provinceList = new ArrayList<Province>();
        Cursor c = db.rawQuery("select * from " + CITY_TABLE_NAME + " group by province",null);
        provinceList.add(new Province("00000","所有城市","00000"));
        while(c.moveToNext()){
            String id = c.getString(c.getColumnIndex("_id"));
            String province = c.getString(c.getColumnIndex("province"));
            String code = c.getString(c.getColumnIndex("number")).substring(0,5);
            Province item = new Province(id,province,code);
            provinceList.add(item);
        }
        return provinceList;
    }
    public List<City> getAllCityByProvince(String pcode){
        return null;
    }
}
