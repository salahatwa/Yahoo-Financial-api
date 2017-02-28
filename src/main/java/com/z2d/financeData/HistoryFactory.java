package com.z2d.financeData;

public class HistoryFactory {

	/**
	 * Factory to determine which  type is will create for running
	 * @param historyType
	 * @return
	 */
	public HistoryData getHistoryData(HistoryType historyType) {
		HistoryData historyData = null;
		switch (historyType) {
		case YAHOO:
			historyData = new YahooFinanceHistData();
			break;
		case GOOGLE:
			break;
		}

		return historyData;
	}
	
	

}
