package com.uestc.Indoorguider.wifi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.R.id;
import com.uestc.Indoorguider.map.MapActivity;
import com.uestc.Indoorguider.util.ClientAgent;
import com.uestc.Indoorguider.util.ConnectTool;
import com.uestc.Indoorguider.util.SendToServerThread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

public class WifiStateReceiver extends BroadcastReceiver {
	int N = 3;//ɨ��N�κ���ƽ������
	int[] count = new int[50];//N��ɨ���ÿ��wifi�ź���ƽ����������

	int num = 0 ;//�ɼ�������
	    private HashMap<String,Integer>[]  mapArray = new HashMap[3];
	    private  List<ScanResult> listWifi =new ArrayList<ScanResult>();;
        WifiManager wifi ;
        JSONArray array ;
        int id = 0;
        
        public WifiStateReceiver( WifiManager wifi )
        {
    	   this.wifi = wifi;
    	   mapArray[0] = new HashMap<String,Integer>();
    	   mapArray[1] = new HashMap<String,Integer>();
    	   mapArray[2] = new HashMap<String,Integer>();
    	   
        }
	    public void onReceive(Context context, Intent intent) {
	    	listWifi = wifi.getScanResults();
	    	HashMap<String,Integer> temp = wifiLocationNew(listWifi);
	    	sendTo(temp);
	    	
	    	//wifiLocationSingle();
	    	
//		    List<ScanResult> temp = new ArrayList<ScanResult>();
//		    // []ssid={"20:dc:e6:6d:13:0e","30:49:3b:09:68:25","30:49:3b:09:68:27","30:49:3b:09:6a:4f","30:49:3b:09:6b:49"};
//		    if(num == 0)
//		    {
		    	listWifi = wifi.getScanResults();
//		    	num ++;
//		    }
//		    else{
//		    	temp =  wifi.getScanResults();
//		    	sumRssi(temp);
//		    	num ++;
//		    	if(num == N)
//		    	{
//		    		wifiLocation();
//		    		num =0;
//		    		listWifi =  new ArrayList<ScanResult>();//һ�ֽ��������
//		    	    count = new int[50];//���
//		    	}
//		    }
//		
	}
	    
	    private HashMap<String,Integer> wifiLocationNew( List<ScanResult> wifiList){
	    	
	    	Iterator<ScanResult> iter = wifiList.iterator();
	    	int mapId = id %3;
	    	if(mapArray[mapId]!= null)
	    	{
	    		mapArray[mapId].clear();//���
	    	}
	    	while(iter.hasNext()){
	    		ScanResult result = iter.next();
	    		mapArray[mapId].put(result.BSSID, result.level);
	    	}
	    	HashMap<String,Integer> temp = new HashMap();
	    	//����ƽ��ֵ
	    	for(int k = 1;k<3;k++){
	    		int id2 = (id+k)%3;
		    	HashMap<String,Integer> map2 = mapArray[id2];
		    	if(!map2.isEmpty()){
		    		Iterator<Map.Entry<String, Integer>> iterator = mapArray[mapId].entrySet().iterator();
		    		while(iterator.hasNext()){
		    			
		    			Entry<String,Integer> entry = iterator.next();
		    			String bssid = entry.getKey() ;
		    			if(!temp.containsKey(bssid))
		    			{
		    				temp.put(bssid, entry.getValue());
		    			}
		    			if(map2.containsKey(bssid)){
		    				temp.put(bssid, (temp.get(bssid).intValue()+map2.get(bssid).intValue())/2);
		    				
		    			}
		    		}
		    		
		    	}
	    		
	    	}
	    	id++;
	    	if(temp.isEmpty()){
	    		return mapArray[mapId];
	    	}else{
	    		return temp;
	    	}
	    	
	    	
	    }
	    
	    public  void sendTo(HashMap<String,Integer> tempMap)
		{   
			String rssi= "";
			int i = 0;
			Iterator<Map.Entry<String, Integer>>  it= tempMap.entrySet().iterator();
			while(it.hasNext()) 
			{
				Entry<String,Integer> entry = it.next();
				//����count��ʼ��Ϊ0 ���ʼ���1
			     rssi += entry.getKey()+ "," + entry.getValue()+";";
			}
			int length = rssi.length();
			String rssi2 = rssi.substring(0,length-1); 
				JSONObject msgObj = new JSONObject();
				try {
					if(MapActivity.isForeground == true)
					{
						msgObj.put("typecode", Constant.LOCATION_REQUEST_WIFI);
					}else{
						msgObj.put("typecode", Constant.LOCATION_REQUEST_WIFI2);
					}
					msgObj.put("rssi", rssi2);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Handler handler = SendToServerThread.getHandler();
				if(handler!= null)
				{
					Message msg = handler.obtainMessage();
					msg.obj = msgObj;		
					handler.sendMessage(msg);
				}
		}
	    
	public  void wifiLocation()
	{   
		String rssi= "";
		int i = 0;
		
		for (; i < listWifi.size()-1; i++) 
		{
			//����count��ʼ��Ϊ0 ���ʼ���1
		     rssi += listWifi.get(i).BSSID + "," + (int)(listWifi.get(i).level/(count[i]+1))+";";
		}
		rssi += listWifi.get(i).BSSID + "," + (int)(listWifi.get(i).level/(count[i]+1));
			JSONObject msgObj = new JSONObject();
			try {
				if(MapActivity.isForeground == true)
				{
					msgObj.put("typecode", Constant.LOCATION_REQUEST_WIFI);
				}else{
					msgObj.put("typecode", Constant.LOCATION_REQUEST_WIFI2);
				}
				msgObj.put("rssi", rssi);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Handler handler = SendToServerThread.getHandler();
			if(handler!= null)
			{
				Message msg = handler.obtainMessage();
				msg.obj = msgObj;		
				handler.sendMessage(msg);
			}
	}
	
	private void sumRssi( List<ScanResult> temp)
	{
		   
	      	for(int i = 0; i<temp.size();i++)
	      	{
	      		//�Ȳ鿴��ͬ�±���ǲ���ͬһ���ź����ƣ��ӿ������ٶ�
	      		String bssid = temp.get(i).BSSID;
	      		
	      		if( i < listWifi.size() && bssid.equals( listWifi.get(i).BSSID))
	      		{
	      			listWifi.get(i).level += temp.get(i).level;
	      			count[i]++;
	      		}
	      		else
	      		{
	      			Boolean flag =  true;//�Ƿ���ҵ�
	      			int j = 0;
	      			//���֮ǰû���ҵ�����������
	      			for(; j<listWifi.size();j++)
	      			{
	      				if(bssid.equals( listWifi.get(j).BSSID))
	    	      		{
	    	      			listWifi.get(j).level += temp.get(i).level;
	    	      			count[j]++;
	    	      			flag = false;
	    	      		}
	      				
	      			}
	      			if(flag)//û�в��ҵ�
		      		{
	      				//count[listWifi.size()]++;//���ã���count���¼����
	      				listWifi.add(temp.get(i));//���µ�WiFi��¼��ӵ��б�
	      				
		      			
		      		}
	      		}
	      		
	      	}
	}
	
	

}
