package com.uestc.Indoorguider.orientation;

import org.json.JSONException;
import org.json.JSONObject;

import com.uestc.Indoorguider.Constant;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class OrientationTool {
	static float[] accelerometerValues = new float[3];  
    static float[] magneticFieldValues = new float[3];
    protected static Handler handlerMain;//MainActivity的handler
    private static final String TAG = "sensor";  
    public static double angle = 0;
	
	public static SensorEventListener sensorEventListener = new SensorEventListener() {  
	    public void onSensorChanged(SensorEvent sensorEvent) {  
	          
	    if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)  
	    magneticFieldValues = sensorEvent.values;  
	    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)  
	        accelerometerValues = sensorEvent.values;  
	    calculateOrientation();  
	    }  
	    
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		
	    };  
	  
	      
	    private static  void calculateOrientation() {  
	          float[] values = new float[3];  
	          float[] R = new float[9];  
	          SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);           
	          SensorManager.getOrientation(R, values);  
	         
	          // 要经过一次数据格式的转换，转换为度  
	          angle= Math.toDegrees(values[0]);
	          if(angle >= 0 && angle < 180)
	          {
	        	  angle -= 80;
	        	  
	          }
	          else{
	        	  angle = angle + 360 - 80;
	          }
	          sendHandlerMsg(handlerMain,angle);
	         // Log.i(TAG, values[0]+"");  
	        }  
	    
	  //向MainActivity发送角度消息
		private static void sendHandlerMsg(Handler h, double angle)
		{
			JSONObject obj= new JSONObject();
			try {
				obj.put("typecode", Constant.ORIENTATION);
				obj.put("angle",angle);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = h.obtainMessage();
	    	msg.obj = obj;
			h.sendMessage(msg);
			
		}
		public static void setMainHandler(Handler h){
		    handlerMain = h;
		}

}
