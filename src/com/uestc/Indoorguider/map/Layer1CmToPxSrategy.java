package com.uestc.Indoorguider.map;

public class Layer1CmToPxSrategy implements XCmtoPxStrategy {

	@Override
	public int calculateX(float cm) {
		// TODO Auto-generated method stub
		
		 int map = (int) (MyWebView.offsetX1-cm/MyWebView.P1);
		 return map;
	}

	@Override
	public int calculateY(float cm) {
		 int map = (int) (MyWebView.offsetY1-cm/MyWebView.P1);
		 return map;
	}

}
