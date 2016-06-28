package com.uestc.Indoorguider.map;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SpecifiedRoute {
    static final  int subway2longDistanceBus = 0;
    static final  int subway2Bus = 1;
    static final  int bus2longDistanceBus = 2;
	
	ArrayList<int[]> pathList = new ArrayList<int[]>();
	SpecifiedRoute(){
		int[] subway2longDistanceBus = {1107, 1624, 1252, 1154};
		int[] subway2Bus = {1106, 1623, 1442, 1694, 1586, 1694};
		int[] bus2longDistanceBus = {1586,1694, 1443, 1694, 1252,1525, 1252,1154};
		pathList.add(subway2longDistanceBus);
		pathList.add(subway2Bus);
		pathList.add(bus2longDistanceBus);
	}


	//返回指定线路
	JSONObject getRoute(int index){
	  JSONObject route = new JSONObject();
	  JSONArray pathArray_px = new JSONArray();
      int[] path = pathList.get(index);
	  for(int i = 0;i<path.length;i++ )
	  {
		  JSONObject node = new JSONObject();
		  try {
			node.put("x",path[i] );
		    i++;
		    node.put("y",path[i] );
		    pathArray_px.put(node);
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
	  }
	  try {
		   route.put("path", pathArray_px);
	    } catch (JSONException e) {
		// TODO Auto-generated catch block
		    e.printStackTrace();
	    }
	  return route;
		
	}
	

}
