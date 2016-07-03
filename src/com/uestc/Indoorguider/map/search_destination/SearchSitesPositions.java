package com.uestc.Indoorguider.map.search_destination;

/**
 *  ���ҳ�վ������
 *  @author vincent
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import com.uestc.Indoorguider.Constant;
import com.uestc.Indoorguider.IndoorGuiderApplication;
import com.uestc.Indoorguider.util.SiteInfo;

public class SearchSitesPositions {
	
	private Map<String, SiteInfo> allSites;
	private List<SiteInfo> sites;
	private List<String> allSitesNames;
	
	private Map<String, Integer> routeLines;
	/**
	 * singleton, ����Ҫ������ʣ����迼���̰߳�ȫ����
	 */
	private static SearchSitesPositions searchInstance = null;
	public static SearchSitesPositions getInstance() {
		if (searchInstance == null) {
			searchInstance = new SearchSitesPositions();
		}
		return searchInstance;
	}
	
	private SearchSitesPositions() {
		init();
	}
	
	/**
	 * ��ʼ������
	 * 1����ȡ�����ļ�
	 * 2������ӦӢ��ת��Ϊ�����������Map
	 */
	private void init() {
		
		allSites = new HashMap<String, SiteInfo>();
		sites = new ArrayList<SiteInfo>();
		allSitesNames = new ArrayList<String>();
		try {
			sites = IndoorGuiderApplication.getAllSitesInfo();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (SiteInfo site : sites) {
			allSites.put(Constant.sitesAndChineseMap().get(site.getSiteName()), site);
		}
		
		routeLines = new HashMap<>();
		routeLines.put("��������;����վ", 0);
		routeLines.put("����������վ", 1);
		routeLines.put("��������;����վ", 2);
	}
	
	/**
	 * �������ַ���·�߱��
	 * @param str
	 * @return
	 */
	public int getRouteLine(String str) {
		int no = 0;
		Integer ret =  routeLines.get(str);
		if (ret != null)
			no = ret;
		return no;
	}   
	
	/**
	 * ���ң����ݵ������ҵ���Ӧ����λ��
	 * @param siteName
	 * @return
	 */
	public SiteInfo search(String siteName) {
		return allSites.get(siteName);
	}
	
	/**
	 * �õ����еص��������,������
	 * @return
	 */
	public List<String> getAllSitesNames() {
		
		for (String name : Constant.sitesAndChineseMap().values()) {
			allSitesNames.add(name);
		}
		return allSitesNames;
	}
	
}
