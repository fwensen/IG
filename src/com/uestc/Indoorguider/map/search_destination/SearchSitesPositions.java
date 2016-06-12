package com.uestc.Indoorguider.map.search_destination;

/**
 *  查找车站的主类
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
import com.uestc.Indoorguider.site_show.SiteInfo;

public class SearchSitesPositions {
	
	private Map<String, SiteInfo> allSites;
	private List<SiteInfo> sites;
	private List<String> allSitesNames;
	/**
	 * singleton, 不需要互斥访问，无需考虑线程安全问题
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
	 * 初始化工作
	 * 1、读取配置文件
	 * 2、将对应英文转化为中文名后插入Map
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
	}
	
	/**
	 * 查找，根据地名查找到相应坐标位置
	 * @param siteName
	 * @return
	 */
	public SiteInfo search(String siteName) {
		return allSites.get(siteName);
	}
	
	/**
	 * 得到所有地点的中文名,并返回
	 * @return
	 */
	public List<String> getAllSitesNames() {
		
		for (String name : Constant.sitesAndChineseMap().values()) {
			allSitesNames.add(name);
		}
		return allSitesNames;
	}
	
}
