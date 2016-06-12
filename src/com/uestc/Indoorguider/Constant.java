package com.uestc.Indoorguider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.jdom.Content;

import com.uestc.Indoorguider.util.ClientAgent;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Constant {
     
	/**
	 * @param args
	 */
	public static final String RESETPASSWORD="8888";
	public static  String IP;
	public static  int PORT;
	 
	public static  String userName = "";
	
	
	//默认情况下的ip和point

	//public static final String ip="222.197.180.140";

	
	//public static final String ip="121.49.97.136";
	public static final String ip="101.201.113.254";
	public static final int  port  = 40015;



	//public static final String ip="222.197.180.14";
	//public static final int port  = 40010;

	//Main
	public static final int INDOOR = 2;
	public static final int OUTDOOR = 1;
	public static final int DIALOG_FROM = 2;
	
	
	
	
//BasicInforActivity handler消息类型
	
	//注册
	public static final int REGISTER_REQUEST_TEL       	= 0;
	public static final int REGISTER_REQUEST_NAME      	= 1;
//  public static final int REGISTER_REQUEST_VALIDATE  	= 10;
//	public static final int REGISTER_RESPONSE         	= 11;
//	public static final int REGISTER_RESPONSE_VALIDATE 	= 110;
	public static final int REGISTER_SUCCESS           	= 500;
	public static final int REGISTER_ERROR_REUES       	= 501;
	public static final int REGISTER_ERROR_RETRY       	= 502;
	
	//登录 
	public static final int LOGIN_REQUEST_NAME         	= 10;
	public static final int LOGIN_REQUEST_TEL          	= 11;
	public static final int LOGIN_REQUEST_UNREGISTER    = 12;
	public static final int LOGOUT_REQUEST     		  	= 210;
	

	public static final int LOGIN_SUCCESS      			= 510;
	public static final int LOGIN_ERROR_PS      	    = 511;
	public static final int LOGIN_ERROR_NO     			= 512;
	//public static final int LOGIN_ERROR_NO     			= 512;
	public static final int LOGIN_ERROR_RETRY  			= 514;
	//logout
	public static final int LOGOUT_RESPONSE     	    = 31;
	public static final int LOGOUT_SUCCESS     			= 310;
	public static final int LOGOUT_ERROR       			= 311;
	
	//password
	
	public static final int PASSWORD_REQUEST_NAME  		= 400;
	public static final int PASSWORD_REQUEST_TEL   		= 401;
	
	public static final int PASSWORD_RESPONSE  		    = 50;

	
	
	//请求导引
	public static final int GUIDE_REQUEST  				= 1010;
	
	public static final int GUIDE_RESPONSE 				= 151;
	public static final int GUIDE_SUCCESS  				= 1510;
	public static final int GUIDE_ERROR    				= 1511;	
	
	//站台查询
	public static final int PLATFORM_REQUEST 		    = 1020;
	
	public static final int PLATFORM_RESPONSE           = 152;
	public static final int PLATFORM_SUCCESS 			= 1520;
	public static final int PLATFORM_ERROR   			= 1521;
    
	//车票查询
	public static final int TICKET_REQUEST         		= 1030;
	
	public static final int TICKET_RESPONSE        		= 153;
	public static final int INQUIRE_TICKET_SUCCESS 		= 1530;
	public static final int INQUIRE_TICKET_ERROR   		= 1531;
	
	//WIFI定位
	public static final int LOCATION_REQUEST_WIFI 		= 1040;
	public static final int LOCATION_REQUEST_WIFI2 		= 1042;//未在定位界面
	
	public static final int LOCATION_WIFI_RESPONSE     		= 154;
	public static final int LOCATION_WIFI_SUCCESS      		= 1540;
	public static final int LOCATION_WIFI_ERROR        		= 1541;
	
	//扫码定位
	public static final int LOCATION_REQUEST_QR   		    = 1050;
	
	public static final int LOCATION_QR_RESPONSE     		= 155;
	public static final int LOCATION_QR_SUCCESS      		= 1550;
	public static final int LOCATION_QR_ERROR        		= 1551;
	
	//历史记录************************
	//查询
	public static final int HISTORY_QUERY = 1060;
	public static final int HISTORY_QR_SUCCESS = 1560; 
	public static final int HISTORY_QR_FAIL = 1561;
	
	//删除所有
	public static final int HISTORY_DELALL_REQUEST = 1070;
	public static final int HISTORY_DELALL_RQ_SUCCESS = 1570;
	public static final int HISTORY_DELALL_RQ_FAIL = 1571;
	//删除特定条目
	public static final int HISTORY_DELGIVEN_REQUEST = 1075;
	public static final int HISTORY_DELGIVEN_RQ_SUCCESS = 1575;
	public static final int HISTORY_DELGIVEN_RQ_FAIL = 1576;
	//*****************************
	
	//网络错误
	public static final int NETWORK_EXCEPTION = 20000;
	
	//行人方位
	public static final int ORIENTATION = 10000;
	public static final int ACCELERATOR = 10001;
	
	//站点详情
	public static final int FACILITY_INFOR = 10100;

	//目的地
	public static final int SET_DESTINATION = 40;
	
	
	
	
	
	public void initUserInfor(Context context)
	{
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		userName = sp.getString("UserName", "");
		
	}
	
	/**
	 * 所有地点对应的中文名
	 * @author vincent
	 * @return
	 */
	 public static Map<String, String> sitesAndChineseMap() {
		 
		 Map<String, String> sacMap = new HashMap<String, String>();
		 sacMap.put("WashRoom", "洗手间");//
		 sacMap.put("FoodSquare", "美食广场");//
		 sacMap.put("McDonald", "麦当劳");//
		 sacMap.put("ATM_1", "取款机(靠近美食广场)");//
		 sacMap.put("ATM_2", "取款机(靠近咨询室)");//
		 sacMap.put("DiTie", "地铁入口");//
		 sacMap.put("FoodCity", "美食城");//
		 sacMap.put("InqueryOffice", "咨询室");//
		 sacMap.put("Coach", "长途汽车站");
		 sacMap.put("Bus", "短途汽车站");
		 sacMap.put("Bus_418", "公交 418 站台");//
		 sacMap.put("Bus_107", "公交 107 东直门枢纽站-白石桥东");//
		 sacMap.put("Bus_123", "公交 123 东直门枢纽站-金五星百货城");//
		 sacMap.put("Bus_416", "公交 416 东直门枢纽站-来东营西桥东");//
		 sacMap.put("Bus_404", "公交 404 东直门枢纽站-来东营西桥东");//
		 sacMap.put("Bus_132", "公交 132 东直门枢纽站-望京北路东口");//
		 sacMap.put("Bus_401", "公交 401 东直门枢纽站-酒仙桥商场");//
		 sacMap.put("Coach_852", "长途汽车 852 平谷方向");
		 sacMap.put("Coach_918", "长途汽车 918 平谷方向");
		 sacMap.put("Coach_866", "长途汽车 866 雁栖方向");
		 sacMap.put("Coach_916", "长途汽车 916 怀柔方向");
		 sacMap.put("Coach_915", "长途汽车 915 顺义南彩方向");
		 sacMap.put("Coach_916F", "长途汽车 915快 顺义南彩方向");
		 sacMap.put("Coach_980", "长途汽车 980 密云方向");
		 sacMap.put("Coach_106", "长途汽车 106 北京南站");
		 return sacMap;
	 }
	 

	public static final float[] subWayElevator ={1059.86f , 1430.48f};
	public static final float[] subWayStair ={1057.86f,  1660.56f};
	
	//定位原点
	//2796，,1926 px
	public static final int  LAYER_NEGATIVE1 =2;
	public static final int LAYER_1 = 1;

}
