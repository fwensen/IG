package com.uestc.Indoorguider.traffic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchTicket {

	
	private static SearchTicket search = new SearchTicket();
	public static SearchTicket getInstance(){
		if (search == null)
			search = new SearchTicket();
		return search;
	}

	
	public SearchTicket() {	 }
	
	//得到所有的搜索结果
	public List<Map<String, Object>> getResult(String type, String sp, String ep,  String sd) {
		
		//List<Map<String, Object>> result = null;
		if (type.equals("Train")) {
			
			return searchTrainTicket(sp, ep, sd);
			
		} else if (type.equals("Plane")) {
			
			
		} else if (type.equals("Steamer")) {
			
			
		} else {
			
			
		}
		
		return null;
	}
	
	private List<Map<String, Object>> searchTrainTicket(String sp, String ep,  String sd) {
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		//test********************
		for (int i = 1; i < 8; i++) {
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Train_cc, i);
			map.put(Constant.Train_startTime, 7+i+":"+"12");
			map.put(Constant.Train_arriveTime, 8+i+":"+"12");
			map.put(Constant.Train_totalTime, "1:00");
			map.put(Constant.Train_swz, i+1);
			map.put(Constant.Train_tdz, i*2);
			map.put(Constant.Train_ydz, i+5);
			map.put(Constant.Train_edz, i+10);
			map.put(Constant.Train_gjwp, i);
			map.put(Constant.Train_yw, i+2);
			map.put(Constant.Train_yw, i+7);
			map.put(Constant.Train_rz, i+17);
			map.put(Constant.Train_yz, i+20);
			map.put(Constant.Train_wz, i+29);
			map.put(Constant.Train_wz, i*10);
			result.add(map);
		}
		return result;
	}
	
	/////////
	
}
