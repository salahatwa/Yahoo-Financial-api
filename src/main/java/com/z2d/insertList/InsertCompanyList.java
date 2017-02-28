package com.z2d.insertList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z2d.beans.StockComanyInfo;
import com.z2d.db.DAO;

public class InsertCompanyList {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(InsertCompanyList.class);
	
	private DAO dao;
	
	public InsertCompanyList() {
		dao=new DAO();
	}
	
	
	public List<StockComanyInfo> readFromFile(String fileName)
	{
		
		try
		{
		File file=new File(fileName);
		List<String> lines = FileUtils.readLines(file);
		List<StockComanyInfo> comanyInfos=new LinkedList<StockComanyInfo>();
		
	    for (String line : lines) {
			String[] lineArr=line.split("\t");
			StockComanyInfo stockComanyInfo=new StockComanyInfo();
			stockComanyInfo.setCompanySymol(lineArr[0]);
			stockComanyInfo.setCompanyName(lineArr[1]);
			stockComanyInfo.setCompanyID(Integer.parseInt(lineArr[2]));
			stockComanyInfo.setStockSourceID(1);
			comanyInfos.add(stockComanyInfo);
		 }
	    
	    return comanyInfos;
		
		}
		catch(Exception ex)
		{
			LOGGER.error("",ex);
		}
		
		return null;
	}
	public static void main(String[] args) {
		InsertCompanyList companyList=new InsertCompanyList();
		List<StockComanyInfo> ls=companyList.readFromFile("input.txt");
		companyList.dao.insertCompanyList(ls);
	}
	
	

}
