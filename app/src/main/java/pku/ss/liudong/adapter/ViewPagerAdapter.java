package pku.ss.liudong.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter{
    private List<View> views;
    private Context ctx;
    public static final String TAG = "View Adapter";

    public ViewPagerAdapter(List<View> list, Context ctx){
        this.views = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        //Log.d(TAG, "get count");
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //Log.d(TAG, "is View From Object");
        return (view == object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //Log.d(TAG, "instantiate Item");
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //Log.d(TAG, "destroy Item");
        container.removeView(views.get(position));
    }
}
