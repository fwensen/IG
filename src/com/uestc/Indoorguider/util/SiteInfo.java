package com.uestc.Indoorguider.util;

import com.uestc.Indoorguider.history.Site;
/**
 * 这里的buttom写错了，应该是bottom ^_^
 * @author vincent
 *
 */
public class SiteInfo {

	private int siteId;
	private double positionX;
	private double positionY;
	private double positionZ;
	private int left;
	private int top;
	private int right;
	private int buttom;
	private String siteName;
	
	public SiteInfo(){		
		this.siteId = 0;
		this.positionX = 0.0;
		this.positionY = 0.0;
		this.positionZ = 0.0;
		this.left = 0;
		this.top = 0;
		this.right = 0;
		this.buttom = 0;
		this.siteName = null;		
	}
	
	public SiteInfo(int id, double x, double y, double z, String n) {	
		this.siteId = id;
		this.positionX = x;
		this.positionY = y;
		this.positionZ = z;
		this.siteName = n;
	}
		
	public void setID(int id) {		
		this.siteId = id;
	}
	
	public void setX(double x){		
		this.positionX = x;
	}
	
	public void setY(double y) {	
		this.positionY = y;
	}
	
	public void setZ(double z) {		
		this.positionZ = z;
	}
	
	public void setSiteName(String n) {	
		this.siteName = n;
	}
	
	public int getID(){	
		return this.siteId;
	}
	
	
	public double getX() {		
		return this.positionX;
	}
	
	public double getY() {		
		return this.positionY;
	}
	
	public double getZ() {		
		return this.positionZ;
	}
	
	public String getSiteName() {		
		return this.siteName;
	}
		
	public void setTop(int t) {
		this.top = t;
	}
	
	public void setLeft(int l) {
		this.left = l;
	}
	public void setRight(int r) {
		this.right = r;
	}
	
	public void setButtom(int b) {
		this.buttom = b;
	}
	
	public int getTop() {
		return this.top;
	}
	
	public int getRight() {
		return this.right;
	}
	
	public int getLeft() {
		return this.left;
	}
	
	public int getButtom() {
		return this.buttom;
	}
}
