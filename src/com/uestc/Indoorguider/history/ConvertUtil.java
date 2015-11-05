package com.uestc.Indoorguider.history;
/*
 * 功能1是将数据库中的内容（表PATHPOINTS_TABLE和表HISTORY_TABLE）提取出来，并
 * 转化成ArrayList<HistoryItem>
 * 数据库中的两张表是相关联的，根据关联性进行转换。
 * 功能二是将json数据中的数据取出
 */



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;
import com.uestc.Indoorguider.history.HistoryItem;
public class ConvertUtil {

	static public ArrayList<HistoryItem> historyAndPointsToList(ArrayList<Map<String,String>> history, 
			Map<Integer, ArrayList<Site>> points) {
		
		//Log.v(TAG, "into historyAndPointsToList");
		ArrayList<HistoryItem> result = new ArrayList<HistoryItem>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		
		for (Map<String, String> hy:history) {			
			int historyId = Integer.parseInt(hy.get("historyId"));
			int mapId = Integer.parseInt(hy.get("mapId"));
			String date  = hy.get("date");
			String startTime = hy.get("starttime");
			String endTime = hy.get("endtime");			
			try {
				now = simpleDateFormat.parse(date);
			} catch (ParseException e) {				
				e.printStackTrace();
			};
			ArrayList<Site> sites = new ArrayList<Site>();
			sites = points.get(historyId);
			//Log.v(TAG, "historyid = " + historyId);
			//if(sites == null)
			
				////Log.v(TAG, "sites are null, and historyid = " + historyId);
			HistoryItem item = new HistoryItem(mapId, now, startTime, endTime, sites);
			result.add(item);
			sites = null;
		}		
		return result;
	}
	
	//将PATHPOINTS_TABLE的数据转化为Map
	static	public Map<Integer, ArrayList<Site>> pointsCursorToMap(Cursor cursor) {
					
			//Log.v(TAG, "into pointsCursorToList");
			int preId = -1;
			int currentId = -2;
			Map<Integer, ArrayList<Site>> result = new HashMap<Integer, ArrayList<Site>>();
			ArrayList<Site> tmpSite = new ArrayList<Site>();
			
			if (cursor.moveToNext()) {	
				//Log.v(TAG, "00");
				preId = cursor.getInt(0);
				tmpSite.add(new Site(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),  cursor.getString(4)));
			}
			//Log.v(TAG, "01");
			while (cursor.moveToNext()) {
				currentId = cursor.getInt(0);
				if (currentId != preId) {				
					result.put(preId, tmpSite);
					tmpSite = null;
					tmpSite = new ArrayList<Site>();
					tmpSite.add(new Site(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),  cursor.getString(4)));
					preId = currentId;
				} else {				
					tmpSite.add(new Site(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),  cursor.getString(4)));							
				}			
			}
			//Log.v(TAG, "02");
			result.put(preId, tmpSite);
			//Log.v(TAG, "pointsCursorToList over");
			return result;
		}
		
		//将HISTORY_TABLE的数据转化为ArrayList
	static	public ArrayList<Map<String, String>> historyCursorToList(Cursor cursor) {
			
			//Log.v(TAG, "into historyCursorToList");
			ArrayList<Map<String, String>> result = 
					new ArrayList<Map<String, String>>();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("historyId", cursor.getInt(0) +"");
				map.put("mapId", cursor.getInt(1)+"");
				map.put("date", cursor.getString(2));
				map.put("starttime", cursor.getString(3));
				map.put("endtime", cursor.getString(4));
				result.add(map);
			}	
			//Log.v(TAG, "historyCursorToList over");
			return result;
		}
	
	 //从JSONObject中取得HistoryItem
    public static HistoryItem getItemFromJson(JSONObject obj) {
    	
    	if (obj == null)
    		return null;
    	HistoryItem itm = null;
    	long mapId = 0;
    	Date dt = null ;
    	String startTime = null;
    	String endTime = null;
    	ArrayList<Site> pathItem = new ArrayList<Site>();
    	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd"); 
    	try {
    		//mapId = obj.getLong("mapid");
			dt = ft.parse(obj.getString("date"));
			startTime = obj.getString("starttime");
			endTime = obj.getString("endtime");
			JSONArray path = obj.getJSONArray("path");
			for (int i = 0; i < path.length(); i++) {
				JSONObject node = (JSONObject) path.get(i);
				int x = node.getInt("x");
				int y = node.getInt("y");
				int z = node.getInt("z");
				//test*********
				String time = node.getString("time");
				pathItem.add(new Site(x,  y,  z, time));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	itm = new HistoryItem(0, dt, startTime, endTime, pathItem);    	
    	return itm;
    }
	
}
