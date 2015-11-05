package com.uestc.Indoorguider.history;

import java.io.Serializable;

public class Site  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000220223322L;
	private int x;
	private int y;
	private int z;
	private String time;
	
	public Site() {		
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.time = "";
	}
	public Site(int newX, int newY, String newTime){
		this.x = newX;
		this.y = newY;		
		this.time = newTime;
	}
	
	public Site(int x, int y , int z, String time) {
		this.x = x;
		this.y = y;		
		this.z = z;
		this.time = time;
	}
	
	public void setX(int newX) {		
		x = newX;
	}
	
	public void setY(int newY) {		
		y = newY;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public int getX(){		
		return this.x;
	}
	
	public int getY(){		
		return this.y;
	}
	public String getTime()
	{
		return this.time;
	}
}
