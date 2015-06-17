package pku.ss.liudong.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import pku.ss.liudong.fragment.ForcastFragment;
import pku.ss.liudong.model.City;
import pku.ss.liudong.model.Weather;
import pku.ss.liudong.service.NotifyService;
import pku.ss.liudong.util.CityPictureThread;
import pku.ss.liudong.util.LocationUtil;
import pku.ss.liudong.util.WeatherNetThread;
import pku.ss.liudong.util.NetUtil;
import pku.ss.liudong.util.PinYin;

public class WeatherActivity extends Activity {
    public static final String TAG = "WeatherActivity>>> ";
    private static final int CODE_CITY_ACTIVITY = 1;
    private static final int CODE_PROVINCE_ACTIVITY = 2;
    public static final String SETTING_CONFIG = "config";
    public static final String MAIN_CITY_CODE = "main_city_code";
    private static final String LAST_UPDATE_INFO = "last_update_info";
    private static final String LAST_UPDATE_PICTURE = "last_update_picture";

    private static Handler handler = null;

    private ImageView titleUpdateBtn,chooseCityBtn,locationBtn,weatherImg,pmImg;
    private TextView titleCityName,cityTv,timeTv,humidityTv,dayTv,pmDataTv,pmQualityTv,temperatureTv,climateTv,windTv;
    private ProgressBar titleUpdateProgressBar;
    private RelativeLayout viewMain;
    private ForcastFragment forcastFragment;

    private String currentCity,cityCode;

    private LocationUtil locationUtil;

    public static Handler getHandler(){
        return handler;
    }
    //初始化今天天气组件
    private void initView(){
        viewMain = (RelativeLayout)findViewById(R.id.weather_main);
        titleCityName = (TextView)findViewById(R.id.title_city_name);
        chooseCityBtn = (ImageView)findViewById(R.id.title_my_city);
        locationBtn = (ImageView)findViewById(R.id.title_location);
        titleUpdateBtn = (ImageView)findViewById(R.id.title_update_btn);
        titleUpdateProgressBar = (ProgressBar)findViewById(R.id.title_update_progress);
        titleUpdateProgressBar.setVisibility(View.INVISIBLE);
        cityTv = ((TextView)findViewById(R.id.city));
        timeTv = ((TextView)findViewById(R.id.time));
        humidityTv = ((TextView)findViewById(R.id.humidity));
        dayTv = ((TextView)findViewById(R.id.week_today));
        pmDataTv = ((TextView)findViewById(R.id.pm_data));
        pmQualityTv = ((TextView)findViewById(R.id.pm2_5_quality));
        temperatureTv = ((TextView)findViewById(R.id.temperature));
        climateTv = ((TextView)findViewById(R.id.climate));
        windTv =((TextView)findViewById(R.id.wind));
        weatherImg = (ImageView)findViewById(R.id.weather_img);
        pmImg = (ImageView)findViewById(R.id.pm2_5_img);
    }
    //初始化未来天气组件
    private void initForcast(){
        forcastFragment = new ForcastFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.forcast_container, forcastFragment).commit();
    }
    //初始化天气数据
    public void initWeatherData(){
        Weather weather = getLastUpdateWeather();
        if(weather != null ){
            updateWeatherInfoOnScreen(weather);
            forcastFragment.setWeather(weather);    //  更新未来天气数据
        }else{
            cityTv.setText("N/A");
            timeTv.setText("N/A");
            humidityTv.setText("N/A");
            dayTv.setText("N/A");
            pmDataTv.setText("N/A");
            pmQualityTv.setText("N/A");
            temperatureTv.setText("N/A");
            climateTv.setText("N/A");
            windTv.setText("N/A");
            updateWeather();
        }
    }
    //初始化背景图片
    private void initBackground(){
        SharedPreferences sp = getSharedPreferences(SETTING_CONFIG,MODE_PRIVATE);
        String picUrl = sp.getString(LAST_UPDATE_PICTURE,"");
        if(picUrl.equals("")){
            updateBackgroundPicture();
        }
    }
    //获取上次选择的城市代码
    private String getCityCode(){
        SharedPreferences sp = getSharedPreferences(SETTING_CONFIG,MODE_PRIVATE);
        this.cityCode = sp.getString(MAIN_CITY_CODE,"101010100");
        return cityCode;
    }
    //获取上次更新的天气信息
    private Weather getLastUpdateWeather(){
        Weather weather = null;
        SharedPreferences sp = getSharedPreferences(SETTING_CONFIG,MODE_PRIVATE);
        String lastUpdate = sp.getString(LAST_UPDATE_INFO,"");
        if(!(lastUpdate.equals(""))){
            weather = JSON.parseObject(lastUpdate,Weather.class);
        }
        return weather;
    }

    //更新天气信息
    private void updateWeather(){
        if(NetUtil.getNetWorkState(WeatherActivity.this) == NetUtil.NETWORK_NONE){
            Toast.makeText(WeatherActivity.this,"网络挂了！",Toast.LENGTH_SHORT).show();
        }else{
            titleUpdateBtn.setVisibility(View.INVISIBLE);
            titleUpdateProgressBar.setVisibility(View.VISIBLE);
            new WeatherNetThread(this.cityCode).start();
        }
    }
    private void updateWeatherInfoOnScreen(Weather weather){
        Log.d(this.TAG, weather.toString());
        currentCity = weather.getCity();    //保存当前城市名称
        pmImg.setImageResource(PinYin.getPmImageResourceId(weather.getAqi())); //更新pm图片
        weatherImg.setImageResource(PinYin.getWeatherImageResourceId(weather.getTypeDay()));  //更新天气图片
        titleCityName.setText(weather.getCity() + "天气");
        cityTv.setText(weather.getCity());
        timeTv.setText(weather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + weather.getShidu());
        pmDataTv.setText(weather.getAqi());
        pmQualityTv.setText(weather.getQuality());
        dayTv.setText(weather.getDay());
        temperatureTv.setText(weather.getLow() + "~" + weather.getHigh());
        climateTv.setText(weather.getWeatherTypeString());
        windTv.setText(weather.getFengli());
        titleUpdateBtn.setVisibility(View.VISIBLE);
        titleUpdateProgressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(WeatherActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }

    //更新背景图片
    private void updateBackgroundPicture(){
        if(NetUtil.getNetWorkState(WeatherActivity.this) == NetUtil.NETWORK_NONE){
            //Toast.makeText(WeatherActivity.this,"网络挂了！",Toast.LENGTH_SHORT).show();
        }else{
            new CityPictureThread(this.cityCode).start();
        }
    }
    private void updateBackgroundPictureOnScreen(Drawable dr){
        viewMain.setBackgroundDrawable(dr);
    }

    //更新上一次选中的城市
    private void updateCityCode(String cityCode){
        this.cityCode = cityCode;
        SharedPreferences sp = getSharedPreferences(SETTING_CONFIG,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(MAIN_CITY_CODE,cityCode);
        editor.commit();
    }
    //初始化子线程更新天气信息handler
    private void initHandler(){
        handler = new Handler(){
            public void handleMessage(Message msg){
                switch(msg.what){
                    case WeatherNetThread.MESSAGE_NEW_WEATHER_INFO:
                        Weather weather = (Weather)msg.obj;
                        SharedPreferences sp = getSharedPreferences(SETTING_CONFIG,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(LAST_UPDATE_INFO,JSON.toJSONString(weather));
                        editor.commit();
                        updateWeatherInfoOnScreen(weather);
                        forcastFragment.setWeather(weather);    //  更新未来天气数据
                        forcastFragment.setForcastInfo();
                        break;
                    case CityPictureThread.MESSAGE_NEW_CITY_PICTURE:
                        Drawable dr = (Drawable)msg.obj;
                        if(dr == null){
                            dr = getResources().getDrawable(R.drawable.biz_plugin_weather_shenzhen_bg);
                        }
                        updateBackgroundPictureOnScreen(dr);
                        break;
                    case LocationUtil.MESSAGE_GET_LOCATION_CODE:
                        City city = (City)msg.obj;
                        if(city != null){
                            //城市已定位
                            if(!WeatherActivity.this.cityCode.equals(city.getNumber())){
                                updateCityCode(city.getNumber());
                                updateWeather();
                                updateBackgroundPicture();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }
    //绑定事件
    private void bindEvents(){
        //刷新按钮
        titleUpdateBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updateWeather();
            }
        });

        //选择城市按钮
        chooseCityBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(WeatherActivity.this,ProvinceActivity.class);
                i.putExtra("city",currentCity);
                startActivityForResult(i, CODE_PROVINCE_ACTIVITY);
            }
        });
        //定位
        locationBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                locationUtil.getLocation();
                //getLocation(WeatherActivity.this.getApplicationContext());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_PROVINCE_ACTIVITY && resultCode == CityActivity.RESULT_CODE_CITY_OK){
            City city = (City)data.getSerializableExtra("city");
            if(!this.cityCode.equals(city.getNumber())){
                updateCityCode(city.getNumber());
                updateBackgroundPicture();
            }
            updateWeather();
        }else if(requestCode == CODE_PROVINCE_ACTIVITY && resultCode == ProvinceActivity.RESULT_CODE_PROVINCE_BACK){
            //do nothing...
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "MainActivity -> OnCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去掉ActionBar
        setContentView(R.layout.activity_weather);
        initView();
        initForcast();
        getCityCode();
        initHandler();

        initBackground();
        initWeatherData();

        bindEvents();
        this.locationUtil = new LocationUtil(this);
        startService(new Intent(getBaseContext(), NotifyService.class));
    }
}
