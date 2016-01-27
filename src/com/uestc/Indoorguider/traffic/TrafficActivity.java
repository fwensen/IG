package com.uestc.Indoorguider.traffic;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.uestc.Indoorguider.R;

public class TrafficActivity extends TabActivity{

	private TabHost tabhost;
	private RadioGroup main_radiogroup;  
	private RadioButton tab_icon_train, tab_icon_plane, tab_icon_steamer,tab_icon_car;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_traffic_main);

	        main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);
	        
	        tab_icon_train = (RadioButton) findViewById(R.id.tab_icon_train);
	        tab_icon_plane = (RadioButton) findViewById(R.id.tab_icon_plane);
	        tab_icon_steamer = (RadioButton) findViewById(R.id.tab_icon_steamer);
	        tab_icon_car = (RadioButton) findViewById(R.id.tab_icon_car);
	        
	        //��TabWidget���Tab
	        tabhost = getTabHost();
	        tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this,SearchTrainActivity.class)));
	        tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this,SearchPlaneActivity.class)));
	        tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this,SearchSteamerActivity.class)));
	        tabhost.addTab(tabhost.newTabSpec("tag4").setIndicator("3").setContent(new Intent(this,SearchCarActivity.class)));
	         
	        //���ü����¼�
	        checkListener checkradio = new checkListener();
	        main_radiogroup.setOnCheckedChangeListener(checkradio);
	 }

	//������
	    public class checkListener implements OnCheckedChangeListener{
	    	@Override
	    	public void onCheckedChanged(RadioGroup group, int checkedId) {
	    		// TODO Auto-generated method stub
	    		//setCurrentTab ͨ����ǩ�������õ�ǰ��ʾ������
	    		//setCurrentTabByTag ͨ����ǩ�����õ�ǰ��ʾ������
	    		switch(checkedId){
	    		case R.id.tab_icon_train:
	    			tabhost.setCurrentTab(0);
	    			//��
	    			//tabhost.setCurrentTabByTag("tag1");
	    			break;
	    		case R.id.tab_icon_plane:
	    			tabhost.setCurrentTab(1);
	    			break;
	    		case R.id.tab_icon_steamer:
	    			tabhost.setCurrentTab(2);
	    			break;
	    		case R.id.tab_icon_car:
	    			tabhost.setCurrentTab(3);
	    			break;
	    		}

	    		
	    	}
	    }
}
