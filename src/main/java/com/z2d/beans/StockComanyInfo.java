package com.z2d.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockComanyInfo {

	private int companyID;
	private String companyName;
	private String companySymol;
	private Date lastUpdateDate;
	private int stockSourceID;
	private List<StockPrice> stockPriceList=new ArrayList<StockPrice>();

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanySymol() {
		return companySymol;
	}

	public void setCompanySymol(String companySymol) {
		this.companySymol = companySymol;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public int getStockSourceID() {
		return stockSourceID;
	}

	public void setStockSourceID(int stockSourceID) {
		this.stockSourceID = stockSourceID;
	}

	public List<StockPrice> getStockPriceList() {
		return stockPriceList;
	}

	public void setStockPriceList(List<StockPrice> stockPriceList) {
		this.stockPriceList = stockPriceList;
	}

}
