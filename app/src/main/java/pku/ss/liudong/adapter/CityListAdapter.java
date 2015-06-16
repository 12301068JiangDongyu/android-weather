package pku.ss.liudong.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pku.ss.liudong.model.City;
import pku.ss.liudong.activity.R;

/**
 * Created by liudong on 2015/4/4.
 */
public class CityListAdapter extends BaseAdapter{
    private Activity context;
    private List<City> cityList;
    public CityListAdapter(Activity cntx,List<City> list){
        this.context = cntx;
        this.cityList = list;
    }
    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(cityList.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.city_item,null);
        TextView tv = (TextView)row.findViewById(R.id.city_item);
        tv.setText(cityList.get(position).getCity());
        return row;
    }
}
