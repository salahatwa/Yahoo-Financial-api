package com.z2d.financeData;

public enum HistoryType {
	YAHOO(1), GOOGLE(2) ;
	
	private int type;
	
	HistoryType(int type)
	{
	  this.type=type;	 
	}
	
	public int getID()
	{
		return type;
	}
}
