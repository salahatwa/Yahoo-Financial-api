package com.z2d.financeData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z2d.beans.StockComanyInfo;
import com.z2d.beans.StockPrice;
import com.z2d.db.DAO;

public class Finance implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(Finance.class);
	private DAO dao;
	private HistoryFactory historyFactory;
	private HistoryType historyType;

	public Finance(HistoryType historyType) {
		this.historyType = historyType;
		dao = new DAO();
		historyFactory = new HistoryFactory();
	}

	public void run() {
		try {
			while (true) 
			{
				getHistoryData();
				LOGGER.info("NOT Found any data to process Finished begin sleeping 10 minuts...");
				TimeUnit.MINUTES.sleep(10);
			}
		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
	}

	/**
	 * GET HISTORY DATA First get information like company name , company symbol
	 * , companyid Second get list of historical data based on first step [ get
	 * historical data from yahoo finanace by companySymbol] we make update by
	 * companyID one by one cause historical data bigger so it cause out memory
	 * heap space in future
	 */
	public void getHistoryData() {

		try {
			LOGGER.info("Begin get data from database....");
			
			HistoryData historyData = historyFactory.getHistoryData(historyType);
			List<StockComanyInfo> stockComanyInfos = dao.getCompanyList(historyType);

			for (StockComanyInfo stockComanyInfo : stockComanyInfos) {
				LOGGER.info("GETTING Stock Price List [" + stockComanyInfos.size() + "] for:"
						+ stockComanyInfo.getCompanyID() + "\t" + stockComanyInfo.getCompanySymol() + "\t"
						+ stockComanyInfo.getLastUpdateDate());
				
				StockComanyInfo updatedStockComanyInfo = historyData.getSymbolHistoryData(stockComanyInfo);

				
				/**
				 * if stockPrice List is not empty update stock data in table with stock_source_id=1
				 * else update stock company info with source 2 for make it run
				 * on Google finance in the future
				 */
				if (updatedStockComanyInfo.getStockPriceList().size() != 0) {
					LOGGER.info("Begin Insert Stock Price List [" + updatedStockComanyInfo.getStockPriceList().size()
							+ "]  For " + updatedStockComanyInfo.getCompanyID() + "...");
					dao.insertHistoricalData(updatedStockComanyInfo);
				}
				
			
				if(stockComanyInfo.getStockSourceID()!=0)
			 	dao.updateStockCompanyInfoSourceID(updatedStockComanyInfo);
			}

			// LOGGER.info("Begin Insert Stock Price List...");
			// dao.insertHistoricalData(stockComanyInfos);

		} catch (Exception ex) {
			LOGGER.error("", ex);
		}

	}

}
