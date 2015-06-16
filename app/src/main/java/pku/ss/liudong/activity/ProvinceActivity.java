package pku.ss.liudong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pku.ss.liudong.adapter.CityListAdapter;
import pku.ss.liudong.adapter.ProvinceListAdapter;
import pku.ss.liudong.app.MyApplication;
import pku.ss.liudong.model.City;
import pku.ss.liudong.model.Province;


public class ProvinceActivity extends Activity {
    private ListView mListView;
    private Button mBackBtn;

    private String currentCity;
    private Handler listViewHandler;
    private List<Province> provinceList;

    public static final int MESSAGE_GET_PROVINCE_LIST = 3;
    public static final int RESULT_CODE_PROVINCELIST_OK = 4;
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

    private void initHandler(){
        listViewHandler = new Handler(){
            public void handleMessage(Message msg){
                switch(msg.what){
                    case MESSAGE_GET_PROVINCE_LIST:
                        provinceList = (ArrayList<Province>)msg.obj;
                        initProvinceList();
                        break;
                }
            }
        };
    }
    private void initProvinceList(){
        setProvinceListAdapter();
    }
    private void setProvinceListAdapter(){

        mListView.setAdapter(new ProvinceListAdapter(ProvinceActivity.this,provinceList));

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
                //setResult(RESULT_CODE_PROVINCELIST_OK,i);
                //finish();
                //Log.i("Province Activity", "你选择的省份是：" + item.toString());
                //Toast.makeText(ProvinceActivity.this, "你选择的省份是：" + item.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_PROVINCE_CITY && resultCode == CityActivity.RESULT_CODE_CITYLIST_OK){
            City city = (City)data.getSerializableExtra("city");
            Intent i = new Intent();
            i.putExtra("city",city);
            setResult(CityActivity.RESULT_CODE_CITYLIST_OK,i);
            finish();
        }
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
}
