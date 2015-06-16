package pku.ss.liudong.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pku.ss.liudong.activity.R;
import pku.ss.liudong.model.City;
import pku.ss.liudong.model.Province;

/**
 * Created by liudong on 2015/4/4.
 */
public class ProvinceListAdapter extends BaseAdapter{
    private Activity context;
    private List<Province> provinceList;
    public ProvinceListAdapter(Activity cntx, List<Province> list){
        this.context = cntx;
        this.provinceList = list;
    }
    @Override
    public int getCount() {
        return provinceList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(provinceList.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.province_item,null);
        TextView tv = (TextView)row.findViewById(R.id.province_item);
        tv.setText(provinceList.get(position).getName());
        return row;
    }
}
