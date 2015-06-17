package pku.ss.liudong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import pku.ss.liudong.adapter.ProvinceListAdapter;
import pku.ss.liudong.app.MyApplication;
import pku.ss.liudong.model.City;
import pku.ss.liudong.model.Province;


public class ProvinceActivity extends Activity{
    private ListView mListView;
    private Button mBackBtn;

    private String currentCity;
    private Handler listViewHandler;
    private List<Province> provinceList;

    public static final int MESSAGE_GET_PROVINCE_LIST = 3;
    public static final int RESULT_CODE_PROVINCE_OK = 4;
    public static final int RESULT_CODE_PROVINCE_BACK = 6;
    public static final int CODE_PROVINCE_CITY = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_province);

        mBackBtn = (Button)findViewById(R.id.back_to_main_btn);
        mListView = (ListView)findViewById(R.id.province_list);

        Intent i = this.getIntent();
        currentCity = i.getStringExtra("city");
        initHandler();
        bindEvents();
        prepareProvinceList();
    }

    private void prepareProvinceList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplication app = (MyApplication)getApplication();
                List<Province> list = app.getmProvinceList();
                Message msg = Message.obtain();
                msg.obj = list;
                msg.what = MESSAGE_GET_PROVINCE_LIST;
                listViewHandler.sendMessage(msg);
            }
        }).start();
    }
    private void setProvinceListAdapter(){
        mListView.setAdapter(new ProvinceListAdapter(ProvinceActivity.this,provinceList));
    }

    private void initHandler(){
        listViewHandler = new Handler(){
            public void handleMessage(Message msg){
                switch(msg.what){
                    case MESSAGE_GET_PROVINCE_LIST:
                        provinceList = (ArrayList<Province>)msg.obj;
                        setProvinceListAdapter();
                        break;
                }
            }
        };
    }
    private void bindEvents(){
        mBackBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent();
                setResult(RESULT_CODE_PROVINCE_BACK,i);
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Province item = (Province)mListView.getItemAtPosition(position);
                Intent i = new Intent(ProvinceActivity.this,CityActivity.class);
                i.putExtra("province_code",item.getCode());
                i.putExtra("city",currentCity);
                startActivityForResult(i, CODE_PROVINCE_CITY);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_PROVINCE_CITY && resultCode == CityActivity.RESULT_CODE_CITY_OK){
            City city = (City)data.getSerializableExtra("city");
            Intent i = new Intent();
            i.putExtra("city",city);
            setResult(CityActivity.RESULT_CODE_CITY_OK,i);
            finish();
        }
    }

    //热门城市列表点击事件
    public void hotclick(View v) {
        String code = "";
        switch (v.getId()) {
            case R.id.beijing:
                code = "101010100";
                break;
            case R.id.shanghai:
                code = "101020100";
                break;
            case R.id.guangzhou:
                code = "101280101";
                break;
            case R.id.shenzhen:
                code = "101280601";
                break;
            case R.id.nanjing:
                code = "101190101";
                break;
            case R.id.wuhan:
                code = "101200101";
                break;
            case R.id.tianjin:
                code = "101030100";
                break;
            case R.id.chongqing:
                code = "101040100";
                break;
            case R.id.hangzhou:
                code = "101210101";
                break;
            case R.id.chengdu:
                code = "101270101";
                break;
            case R.id.wulumuqi:
                code = "101130101";
                break;
            case R.id.xian:
                code = "101110101";
                break;
        }
        City city = new City();
        city.setNumber(code);
        Intent i = new Intent();
        i.putExtra("city",city);
        setResult(CityActivity.RESULT_CODE_CITY_OK,i);
        finish();
    }
}
