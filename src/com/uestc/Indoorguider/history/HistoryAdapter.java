package com.uestc.Indoorguider.history;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uestc.Indoorguider.R;
//import com.uestc.Indoorguider.history.HistoryRecorder.HistoryAdapter.ViewHolder;

public class HistoryAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<HistoryItem> items;
	private String TAG = "HISTORY";
	Context ctx;
    public HistoryAdapter(Context context, List<HistoryItem> itms) {  	
    	ctx = context;
    	items = itms;
        this.mInflater = LayoutInflater.from(context);        
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub		
		HistoryItem item = items.get(position);
		ViewHolder viewHolder = null;
		//Log.v(TAG, "into getView");
		RelativeLayout layout = null;
		if(convertView == null) {			
			convertView = mInflater.inflate(R.layout.history_item, null);
			//Log.v(TAG, " getView !!");
	    	viewHolder = new ViewHolder();
	    	viewHolder.llHistoryItem = (RelativeLayout) convertView.findViewById(R.id.history_item_date);
	    	viewHolder.tvDate = (TextView) convertView.findViewById(R.id.history_clock_tv);
	    	viewHolder.tvStartToEndTime = (TextView) convertView.findViewById(R.id.history_start_to_endtime_tv);
			convertView.setTag(viewHolder);
			viewHolder.llHistoryItem.setTag(position);
			//layout.setTag(viewHolder);
			//Log.v(TAG, "getView findViewById over!");
		} else {			
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String dateTime = item.getDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		try {
			now = simpleDateFormat.parse(dateTime);
		} catch (ParseException e1) {
			
			e1.printStackTrace();
		};
		
		if (position > 0) {			
			String preday = items.get(position-1).getDate();			
			Date pre = null;
			try {			
				pre = simpleDateFormat.parse(preday);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!pre.before(now) && !pre.after(now)) {			
				viewHolder.llHistoryItem.setVisibility(View.GONE);
			} else {			
				viewHolder.llHistoryItem.setVisibility(View.VISIBLE);
				dateTime+=" "+getWeekOfDate(now);
				viewHolder.tvDate.setText(dateTime);
			}
		} else {
			
			viewHolder.llHistoryItem.setVisibility(View.VISIBLE);
			dateTime+=" "+getWeekOfDate(now);
			viewHolder.tvDate.setText(dateTime);
		}		
		viewHolder.tvStartToEndTime.setText(item.getStartTime()+" - "+item.getEndTime());
		return convertView;
	}
	
	class ViewHolder{			
		public RelativeLayout llHistoryItem;
	    public TextView  tvDate;
	    public TextView tvStartToEndTime;
	    //public TextView tvEndTime;
	}
	
	public  String getWeekOfDate(Date date) { 
		  String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" }; 
		  Calendar calendar = Calendar.getInstance(); 
		  calendar.setTime(date); 
		  int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; 
		  return weekDaysName[intWeek]; 
		} 
		
} 