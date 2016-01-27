package com.uestc.Indoorguider.traffic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.uestc.Indoorguider.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TicketShowListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> valueList;
	private Holder holder = null;

	public TicketShowListAdapter(Context content,
			List<Map<String, Object>> valueList) {
		this.mContext = content;
		this.valueList = valueList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return valueList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.train_ticket_detail_listview, null);
			holder = new Holder();
			holder.indexText = (TextView) convertView
					.findViewById(R.id.train_indextxt);
			holder.trainOrderText = (TextView) convertView
					.findViewById(R.id.train_ordertxt);
			holder.startTimeTxt = (TextView) convertView
					.findViewById(R.id.train_starttimetxt);
			holder.arriveTimeTxt = (TextView) convertView
					.findViewById(R.id.train_arrivetimetxt);
			holder.totalTimeTxt = (TextView) convertView
					.findViewById(R.id.train_totaltimetxt);

			convertView.setTag(holder);
		} else {

			holder = (Holder) convertView.getTag();
		}

		Map<String, Object> map = valueList.get(arg0);
		holder.indexText.setText(Common.ObjectToString(map.get("序号")));
		holder.trainOrderText.setText(Common.ObjectToString(map.get("车次")));
		holder.startTimeTxt.setText(Common.ObjectToString(map.get("出发时间")));
		holder.arriveTimeTxt.setText(Common.ObjectToString(map.get("到达时间")));
		holder.totalTimeTxt.setText(Common.ObjectToString(map.get("全程耗时")));
		return convertView;
	}

	class Holder {

		TextView indexText;
		TextView trainOrderText;
		TextView startTimeTxt;
		TextView arriveTimeTxt;
		TextView totalTimeTxt;
	}

}
