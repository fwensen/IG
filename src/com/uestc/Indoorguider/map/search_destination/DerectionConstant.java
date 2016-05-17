package com.uestc.Indoorguider.map.search_destination;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DerectionConstant {

	//just for test
	public ArrayList<String> DERECTION_ALL = new ArrayList<>();
	public void fillDerection() {
		DERECTION_ALL.add(DERECTION_13);
		DERECTION_ALL.add(DERECTION_852);
		DERECTION_ALL.add(DERECTION_866);
		DERECTION_ALL.add(DERECTION_915);
		DERECTION_ALL.add(DERECTION_916);
		DERECTION_ALL.add(DERECTION_916F);
		DERECTION_ALL.add(DERECTION_918);
		DERECTION_ALL.add(DERECTION_980);
		DERECTION_ALL.add(DERECTION_AIRPORT);
		DERECTION_ALL.add(DERECTION_3);
	}
	
	public final static String DERECTION_852 = "852 平谷方向";
	public final static String DERECTION_918 = "918 平谷方向";
	public final static String DERECTION_866 = "866 雁栖方向";
	public final static String DERECTION_916 = "916 怀柔方向";
	public final static String DERECTION_915 = "915 顺义南彩方向";
	public final static String DERECTION_916F = "915快 顺义南彩方向";
	public final static String DERECTION_AIRPORT = "机场方向";
	public final static String DERECTION_13 = "13号线";
	public final static String DERECTION_980 = "密云方向";
	public final static String DERECTION_3 = "北京西站";
	public final static String DERECTION_106 = "北京南站";
	
}
