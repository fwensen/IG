package com.uestc.Indoorguider.map;

public class Layer1CmToPxSrategy implements XCmtoPxStrategy {


	@Override
	public int calculateX(float cm) {
		// TODO Auto-generated method stub
		 int map = (int) (MyWebView.offsetX-cm/MyWebView.P);
		 return map;
	}

	@Override
	public int calculateY(float cm) {
		// TODO Auto-generated method stub
		 int map = (int) (MyWebView.offsetY-cm/MyWebView.P);
		 return map;
	}

}
