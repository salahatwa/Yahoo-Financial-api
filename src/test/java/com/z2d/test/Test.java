package com.z2d.test;

import java.util.Calendar;
import com.z2d.db.DAO;

public class Test {
	private static DAO dao=new DAO();
	
	public static void main(String[] args) {
//	List<StockComanyInfo> infos=dao.getCompanyList();
//	for (StockComanyInfo stockComanyInfo : infos) {
//		System.out.println(stockComanyInfo.getCompanyName());
//	}
//	
//	 System.out.println(infos.size());
		
		Calendar from = Calendar.getInstance();
		from.set(2010, 0, 1, 0, 0, 0);
		
		System.out.println("Altered year is :" + from.getTime());
	}

}
