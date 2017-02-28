package com.z2d.financeData;

public class App {
	
	
	public static void main(String[] args) {
		Finance finance=new Finance(HistoryType.YAHOO);
		Thread thread=new Thread(finance);
		thread.start();
	}

}
