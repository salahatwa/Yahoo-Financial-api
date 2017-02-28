package com.z2d.financeData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z2d.beans.StockComanyInfo;
import com.z2d.beans.StockPrice;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class YahooFinanceHistData extends HistoryData {

	/**
	 * THIS METHOD CONNECT TO YAHOO API and get Last historical data IF DATE ==
	 * NULL it will return all historical data for given company from 2010-1-1
	 * IF DATE != NULL it fromDate.set(int year,int month,int day,int hourOfDay,
	 * int minute,int second)
	 */
	@Override
	public StockComanyInfo getSymbolHistoryData(StockComanyInfo stockComanyInfo) {

		List<StockPrice> stockPriceList = new ArrayList<StockPrice>();

		SimpleDateFormat sdf = new SimpleDateFormat("YYY-MM-dd");
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		// from.add(Calendar.YEAR, -7);
		if (stockComanyInfo.getLastUpdateDate() == null)
			fromDate.set(2010, 0, 1, 0, 0, 0);
		else
			fromDate.setTime(stockComanyInfo.getLastUpdateDate());

		try {
			Stock stock = YahooFinance.get(stockComanyInfo.getCompanySymol());

			// Checks if the returned name is null.

			if (stock.isValid()) {

				stockComanyInfo.setStockSourceID(HistoryType.YAHOO.getID());

				List<HistoricalQuote> symbolHistQuotes = stock.getHistory(fromDate, toDate, Interval.DAILY);

				if (symbolHistQuotes != null) {
					for (HistoricalQuote historicalQuote : symbolHistQuotes) {
						// sb.append(stock.getName()).append("\t");
						StockPrice stockPrice = new StockPrice();

						if (stock.getCurrency() == "null")
							stockPrice.setCurrencey("");
						else
							stockPrice.setCurrencey(stock.getCurrency());

						stockPrice.setStockDate(sdf.format(historicalQuote.getDate().getTime()));
						stockPrice.setStockOpen(String.valueOf(historicalQuote.getOpen()));
						stockPrice.setStockHigh(String.valueOf(historicalQuote.getHigh()));
						stockPrice.setStockLow(String.valueOf(historicalQuote.getLow()));
						stockPrice.setStockClose(String.valueOf(historicalQuote.getClose()));
						stockPrice.setStockVolume(String.valueOf(historicalQuote.getVolume()));
						stockPrice.setStockAdjClose(String.valueOf(historicalQuote.getAdjClose()));
						stockPriceList.add(stockPrice);
					}

					stockComanyInfo.setStockPriceList(stockPriceList);

				} else {
					stockComanyInfo.setStockSourceID(HistoryType.GOOGLE.getID());
				}

			}
		} catch (FileNotFoundException ex) {
			LOGGER.error("Can't Get Stock historical data for :" + stockComanyInfo.getCompanySymol() + " : "
					+ stockComanyInfo.getCompanyID(), ex);
			stockComanyInfo.setStockSourceID(0);

		} catch (IOException e) {
			LOGGER.error("Error network can't connect to network problem in the following company:"
					+ stockComanyInfo.getCompanySymol() + " : " + stockComanyInfo.getCompanyID(), e);
			LOGGER.error("", e);
			stockComanyInfo.setStockSourceID(0);
		} catch (NullPointerException er) {
			LOGGER.error("Can't Get Stock historical data for :" + stockComanyInfo.getCompanySymol() + " : "
					+ stockComanyInfo.getCompanyID(), er);
			stockComanyInfo.setStockSourceID(0);
		}
		return stockComanyInfo;
	}

}
