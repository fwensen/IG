package com.uestc.Indoorguider.map;

public class LayerNegative1CmToPxSrategy implements XCmtoPxStrategy {

	@Override
	public int calculateX(float cm) {
		// TODO Auto-generated method stub
		
		 int map = (int) (MyWebView.offsetXNegative1-cm/MyWebView.PNegative1);
		 return map;
	}

	@Override
	public int calculateY(float cm) {
		 int map = (int) (MyWebView.offsetYNegative1-cm/MyWebView.PNegative1);
		 return map;
	}

}
