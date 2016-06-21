package com.uestc.Indoorguider.traffic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.uestc.Indoorguider.R;
import com.uestc.Indoorguider.traffic.utils.SubwayAdapter;

public class SearchSubwayActivity extends Activity{
	
	ListView mListView; 
	List<SubwayLine> subwayLines;
	SubwayAdapter mAdaper;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_subway_show);
		initViews();
	}
	 
	 
	 
	private void initViews() {
		mListView = (ListView) findViewById(R.id.subway_list);
		subwayLines = new ArrayList<SubwayLine>();
		prepareData();
		mAdaper = new SubwayAdapter(this, subwayLines, R.layout.subway_line_item);
		mListView.setAdapter(mAdaper);
		
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Bundle data = new Bundle();
				data.putInt("line_no", position);
				Intent intent = new Intent(SearchSubwayActivity.this, ShowSubwayDetails.class);
				intent.putExtras(data);
				//����intent��Ӧ��Activity
				startActivity(intent);
			}
			
		});
	}
	
	/**
	 * ׼��������·����
	 */
	private void prepareData() {
		
		 subwayLines.add(new SubwayLine(1, "1������ĩ��ʱ�̱�", "���Ļݶ�����", "��ƻ��԰����"));
		 subwayLines.add(new SubwayLine(2, "2������ĩ��ʱ�̱�", "�⻷(��ֱ��-������-��ֱ��-��ֱ��)", "�ڻ�(��ˮ̶-��ֱ��-������-��ˮ̶)"));
		 subwayLines.add(new SubwayLine(3, "4����/��������ĩ��ʱ�̱�", "����", "����"));
		 subwayLines.add(new SubwayLine(4, "5������ĩ��ʱ�̱�", "������ͨԷ������", "�����μ�ׯ����"));
		 subwayLines.add(new SubwayLine(5, "6������ĩ��ʱ�̱�", "����������·�ӷ���", "����º�Ƿ���"));
		 subwayLines.add(new SubwayLine(6, "7������ĩ��ʱ�̱�", "����������վ����", "����������վ����"));
		 subwayLines.add(new SubwayLine(7, "8������ĩ��ʱ�̱�", "����������﷽��", "��������ׯ����"));
		 subwayLines.add(new SubwayLine(8, "9������ĩ��ʱ�̱�", "��������ׯ����", "��������ͼ��ݷ���"));
		 subwayLines.add(new SubwayLine(9, "10������ĩ��ʱ�̱�", "����(�ڻ�)�͹�-��ó-�μ�ׯ-����������", "����(�⻷)������-�μ�ׯ-��ó-�͹�����"));
		 subwayLines.add(new SubwayLine(10, "13������ĩ��ʱ�̱�", "����ֱ�� ����ֱ�� ����ֱ��", "����ֱ�� ����Ӫվ ��������վ"));
		 subwayLines.add(new SubwayLine(11, "14����(����)��ĩ��ʱ�̱�", "�������ַ���", "�����Ź�ׯ����"));
		 subwayLines.add(new SubwayLine(12, "14���߶���(���ж�)��ĩ��ʱ�̱�", "�����Ƹ�ׯ����", "����������վ����"));
		 subwayLines.add(new SubwayLine(13, "15������ĩ��ʱ�̱�", "�����廪��·���ڷ���", "����ٺ������"));
		 subwayLines.add(new SubwayLine(14, "��ͨ����ĩ��ʱ�̱�", "�Ļݡ�����", "���š��Ļ�"));
		 subwayLines.add(new SubwayLine(15, "��ƽ����ĩ��ʱ�̱�", "�������췽�� ����ƽ��ɽ�ڷ���", "�������췽�� ����ƽ��ɽ�ڷ���"));
		 subwayLines.add(new SubwayLine(16, "��ׯ����ĩ��ʱ�̱�", "����������", "���μ�ׯ����"));
		 subwayLines.add(new SubwayLine(17, "4����/��������ĩ��ʱ�̱�", "����", "����"));
		 subwayLines.add(new SubwayLine(18, "��ɽ����ĩ��ʱ�̱�", "����ׯ����", "������ׯ����"));
		 subwayLines.add(new SubwayLine(19, "��������ĩ��ʱ�̱�", "���С�������", "���С�������"));
	}
}
