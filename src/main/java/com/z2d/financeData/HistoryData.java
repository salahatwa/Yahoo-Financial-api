package com.z2d.financeData;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z2d.beans.StockComanyInfo;


public abstract class HistoryData {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(YahooFinanceHistData.class);
	
//	public Stock getStock(){};
	
	public abstract <T extends Object> StockComanyInfo getSymbolHistoryData(StockComanyInfo stockComanyInfo);

}
