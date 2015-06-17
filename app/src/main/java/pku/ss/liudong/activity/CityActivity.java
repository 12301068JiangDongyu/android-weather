package pku.ss.liudong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pku.ss.liudong.app.MyApplication;
import pku.ss.liudong.model.City;
import pku.ss.liudong.adapter.CityListAdapter;
import pku.ss.liudong.model.Province;

/**
 * Created by liudong on 2015/3/26.
 */
public class CityActivity extends Activity{
    private static final int MESSAGE_GET_CITY_LIST = 1;
    public static final int RESULT_CODE_CITY_OK = 2;
    private TextView mTitleCity;
    private ImageView mTitleBackBtn;
    private ListView mListView;
    private EditText mSearchCityText;
    private Handler listViewHandler;
    private List<City> cityList;
    private ArrayList<City> filterCityList;

    private String provinceCode;
    private String currentCity;

    private void initHandler(){
        listViewHandler = new Handler(){
            public void handleMessage(Message msg){
                switch(msg.what){
                    case MESSAGE_GET_CITY_LIST:
                        cityList = getCityListByProvinceCode((ArrayList<City>)msg.obj);
                        filterCityList = new ArrayList<City>();
                        initCityList();
                        break;
                }
            }
        };
    }
    private List<City> getCityListByProvinceCode(List<City> list){
        if(provinceCode.equals("00000")){
            return list;
        }
        List<City> result = new ArrayList<City>();
        Iterator<City> iter = list.iterator();
        while(iter.hasNext()){
            City c = iter.next();
            if(c.getNumber().indexOf(provinceCode) == 0){
                result.add(c);
            }
        }
        return result;
    }
    private void initCityList(){
        setCityListAdapter();
    }
    private void setCityListAdapter(){
        if(filterCityList.size() == 0){
            mListView.setAdapter(new CityListAdapter(CityActivity.this,cityList));
        }else{
            mListView.setAdapter(new CityListAdapter(CityActivity.this,filterCityList));
        }
    }
    private void bindEvents(){
        mTitleBackBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.title_back:
                        CityActivity.this.finish();
                        break;
                    default:
                        break;
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City item = (City)mListView.getItemAtPosition(position);
                Intent i = new Intent();
                i.putExtra("city",item);
                setResult(RESULT_CODE_CITY_OK,i);
                finish();
                //Toast.makeText(CityActivity.this,"你选择的城市是："+city+",城市代码是"+number,Toast.LENGTH_LONG).show();
            }
        });
        mSearchCityText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
                filterCityList.clear();
                for(City item : cityList){
                    if(item.toString().contains(mSearchCityText.getText().toString().trim())){
                        filterCityList.add(item);
                    }
                }
                setCityListAdapter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void prepareCityList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplication app = (MyApplication)getApplication();
                List<City> list = app.getmCityList();
                Message msg = Message.obtain();
                msg.obj = list;
                msg.what = MESSAGE_GET_CITY_LIST;
                listViewHandler.sendMessage(msg);
            }
        }).start();
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_city);

        mTitleCity = (TextView)findViewById(R.id.title_name);
        mTitleBackBtn = (ImageView)findViewById(R.id.title_back);
        mListView = (ListView)findViewById(R.id.city_list);
        mSearchCityText = (EditText)findViewById(R.id.search_city_text);

        Intent i = this.getIntent();
        currentCity = i.getStringExtra("city");
        provinceCode  = i.getStringExtra("province_code");

        mTitleCity.setText("当前城市：" + currentCity);

        initHandler();
        bindEvents();
        prepareCityList();
    }
}
