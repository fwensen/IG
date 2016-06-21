package com.uestc.Indoorguider.traffic;
/**
 * ������·
 * @author vincent
 *
 */
public class SubwayLine {

	/**
	 * ��·���
	 */
	private int lineNo;
	/**
	 * ��·��
	 */
	private String lineName;
	/**
	 * ����һ������
	 */
	private String extra1;
	/**
	 * ����һ������
	 */
	private String extra2;
	
	public SubwayLine(int no, String name, String e1, String e2) {
		this.lineNo = no;
		this.lineName = name;
		this.extra1 = e1;
		this.extra2 = e2;
	}
	
	public int getLineNo() {
		return this.lineNo;
	}
	
	public void setLineNo(int no) {
		this.lineNo = no;
	}
	
	public String getLineName() {
		return this.lineName;
	}
	
	public void setLineName(String name) {
		this.lineName = name;
	}
	
	public String getExtra1() {
		return this.extra1;
	}
	
	public void setExtra1(String e1) {
		this.extra1 = e1;
	}
	
	public String getExtra2() {
		return this.extra2;
	}
	
	public void setExtra2(String e2) {
		this.extra2 = e2;
	}
	
}
