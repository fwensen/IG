package com.uestc.Indoorguider.traffic.utils;

import java.util.List;

import android.content.Context;

import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.map.search_destination.CommonAdapter;
import com.uestc.Indoorguider.map.search_destination.ViewHolder;
import com.uestc.Indoorguider.traffic.SubwayLine;


public class SubwayAdapter extends CommonAdapter<SubwayLine>{

	 public SubwayAdapter(Context context, List<SubwayLine> data, int layoutId) {
	        super(context, data, layoutId);
	    }

	@Override
	public void convert(ViewHolder holder, int position) {
		holder.setText(R.id.tv_subway_line, mData.get(position).getLineName())
			   .setText(R.id.tv_subway_extr1, mData.get(position).getExtra1())
			   .setText(R.id.tv_subway_extr2, mData.get(position).getExtra2());
	}
}
