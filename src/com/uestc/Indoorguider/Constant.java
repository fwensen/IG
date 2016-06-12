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
	
	
	//Ĭ������µ�ip��point

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
	
	
	
	
//BasicInforActivity handler��Ϣ����
	
	//ע��
	public static final int REGISTER_REQUEST_TEL       	= 0;
	public static final int REGISTER_REQUEST_NAME      	= 1;
//  public static final int REGISTER_REQUEST_VALIDATE  	= 10;
//	public static final int REGISTER_RESPONSE         	= 11;
//	public static final int REGISTER_RESPONSE_VALIDATE 	= 110;
	public static final int REGISTER_SUCCESS           	= 500;
	public static final int REGISTER_ERROR_REUES       	= 501;
	public static final int REGISTER_ERROR_RETRY       	= 502;
	
	//��¼ 
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

	
	
	//������
	public static final int GUIDE_REQUEST  				= 1010;
	
	public static final int GUIDE_RESPONSE 				= 151;
	public static final int GUIDE_SUCCESS  				= 1510;
	public static final int GUIDE_ERROR    				= 1511;	
	
	//վ̨��ѯ
	public static final int PLATFORM_REQUEST 		    = 1020;
	
	public static final int PLATFORM_RESPONSE           = 152;
	public static final int PLATFORM_SUCCESS 			= 1520;
	public static final int PLATFORM_ERROR   			= 1521;
    
	//��Ʊ��ѯ
	public static final int TICKET_REQUEST         		= 1030;
	
	public static final int TICKET_RESPONSE        		= 153;
	public static final int INQUIRE_TICKET_SUCCESS 		= 1530;
	public static final int INQUIRE_TICKET_ERROR   		= 1531;
	
	//WIFI��λ
	public static final int LOCATION_REQUEST_WIFI 		= 1040;
	public static final int LOCATION_REQUEST_WIFI2 		= 1042;//δ�ڶ�λ����
	
	public static final int LOCATION_WIFI_RESPONSE     		= 154;
	public static final int LOCATION_WIFI_SUCCESS      		= 1540;
	public static final int LOCATION_WIFI_ERROR        		= 1541;
	
	//ɨ�붨λ
	public static final int LOCATION_REQUEST_QR   		    = 1050;
	
	public static final int LOCATION_QR_RESPONSE     		= 155;
	public static final int LOCATION_QR_SUCCESS      		= 1550;
	public static final int LOCATION_QR_ERROR        		= 1551;
	
	//��ʷ��¼************************
	//��ѯ
	public static final int HISTORY_QUERY = 1060;
	public static final int HISTORY_QR_SUCCESS = 1560; 
	public static final int HISTORY_QR_FAIL = 1561;
	
	//ɾ������
	public static final int HISTORY_DELALL_REQUEST = 1070;
	public static final int HISTORY_DELALL_RQ_SUCCESS = 1570;
	public static final int HISTORY_DELALL_RQ_FAIL = 1571;
	//ɾ���ض���Ŀ
	public static final int HISTORY_DELGIVEN_REQUEST = 1075;
	public static final int HISTORY_DELGIVEN_RQ_SUCCESS = 1575;
	public static final int HISTORY_DELGIVEN_RQ_FAIL = 1576;
	//*****************************
	
	//�������
	public static final int NETWORK_EXCEPTION = 20000;
	
	//���˷�λ
	public static final int ORIENTATION = 10000;
	public static final int ACCELERATOR = 10001;
	
	//վ������
	public static final int FACILITY_INFOR = 10100;

	//Ŀ�ĵ�
	public static final int SET_DESTINATION = 40;
	
	
	
	
	
	public void initUserInfor(Context context)
	{
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		userName = sp.getString("UserName", "");
		
	}
	
	/**
	 * ���еص��Ӧ��������
	 * @author vincent
	 * @return
	 */
	 public static Map<String, String> sitesAndChineseMap() {
		 
		 Map<String, String> sacMap = new HashMap<String, String>();
		 sacMap.put("WashRoom", "ϴ�ּ�");//
		 sacMap.put("FoodSquare", "��ʳ�㳡");//
		 sacMap.put("McDonald", "����");//
		 sacMap.put("ATM_1", "ȡ���(������ʳ�㳡)");//
		 sacMap.put("ATM_2", "ȡ���(������ѯ��)");//
		 sacMap.put("DiTie", "�������");//
		 sacMap.put("FoodCity", "��ʳ��");//
		 sacMap.put("InqueryOffice", "��ѯ��");//
		 sacMap.put("Coach", "��;����վ");
		 sacMap.put("Bus", "��;����վ");
		 sacMap.put("Bus_418", "���� 418 վ̨");//
		 sacMap.put("Bus_107", "���� 107 ��ֱ����Ŧվ-��ʯ�Ŷ�");//
		 sacMap.put("Bus_123", "���� 123 ��ֱ����Ŧվ-�����ǰٻ���");//
		 sacMap.put("Bus_416", "���� 416 ��ֱ����Ŧվ-����Ӫ���Ŷ�");//
		 sacMap.put("Bus_404", "���� 404 ��ֱ����Ŧվ-����Ӫ���Ŷ�");//
		 sacMap.put("Bus_132", "���� 132 ��ֱ����Ŧվ-������·����");//
		 sacMap.put("Bus_401", "���� 401 ��ֱ����Ŧվ-�������̳�");//
		 sacMap.put("Coach_852", "��;���� 852 ƽ�ȷ���");
		 sacMap.put("Coach_918", "��;���� 918 ƽ�ȷ���");
		 sacMap.put("Coach_866", "��;���� 866 ���ܷ���");
		 sacMap.put("Coach_916", "��;���� 916 ���᷽��");
		 sacMap.put("Coach_915", "��;���� 915 ˳���ϲʷ���");
		 sacMap.put("Coach_916F", "��;���� 915�� ˳���ϲʷ���");
		 sacMap.put("Coach_980", "��;���� 980 ���Ʒ���");
		 sacMap.put("Coach_106", "��;���� 106 ������վ");
		 return sacMap;
	 }
	 

	public static final float[] subWayElevator ={1059.86f , 1430.48f};
	public static final float[] subWayStair ={1057.86f,  1660.56f};
	
	//��λԭ��
	//2796��,1926 px
	public static final int  LAYER_NEGATIVE1 =2;
	public static final int LAYER_1 = 1;

}
