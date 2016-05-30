package com.uestc.Indoorguider.map;

public class CmToPxCalculator {
	XCmtoPxStrategy myStrategy ;
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
