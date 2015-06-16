package pku.ss.liudong.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pku.ss.liudong.model.Province;
import pku.ss.liudong.util.CityDB;
import pku.ss.liudong.model.City;

/**
 * Created by liudong on 2015/3/29.
 */
public class MyApplication extends Application{
    private static final String TAG = "MyAPP";
    private static Application mApplication;
    private List<City> mCityList = null;
    private List<Province> mProvinceList = null;
    private CityDB mCityDB = null;
    public void onCreate(){
        mApplication = this;
        mCityDB = openCityDB();
        initCityList();
    }
    public static Application getInstance(){
        return mApplication;
    }
    public CityDB openCityDB(){
        String path = "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases"
                + File.separator + CityDB.CITY_DB_NAME;
        File db = new File(path);
        File parent = db.getParentFile();
        if(!parent.exists()){
            Log.d(TAG,"databases directory does not exist");
            parent.mkdir();
        }
        if(!db.exists()){
            Log.d(TAG,"db file does not exist");
            try {
                db.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }
                fos.close();
                is.close();
            }catch(IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }

    private void initCityList(){
        mCityList = new ArrayList<City>();
        mProvinceList = new ArrayList<Province>();
        new Thread(new Runnable(){
            public void run(){
                prepareCityList();
            }
        }).start();
    }
    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity();
        mProvinceList = mCityDB.getAllProvince();
        return true;
    }
    public List<City> getmCityList(){
        return mCityList;
    }
    public List<Province> getmProvinceList(){
        return mProvinceList;
    }
}
