package com.uestc.Indoorguider.map;

import com.uestc.Indoorguider.Constant;

public class CmToPxCalculator {
	XCmtoPxStrategy myStrategy ;
	 static CmToPxCalculator getCalculator(int layer){
		CmToPxCalculator calculator = new CmToPxCalculator();
		if(layer == Constant.LAYER_NEGATIVE1)
		{
			calculator.setStrategy(new LayerNegative1CmToPxSrategy());
			
		}else{
			calculator.setStrategy(new Layer1CmToPxSrategy());
			
		}
		return calculator;
	}
	public void setStrategy(XCmtoPxStrategy strategy ){
		myStrategy = strategy;
	}
	
	public int calculatorX(float locationNow_cm){
	  return myStrategy.calculateX(locationNow_cm);
	}
	
	public int calculatorY(float locationNow_cm){
		  return myStrategy.calculateY(locationNow_cm);
		}
	

}
