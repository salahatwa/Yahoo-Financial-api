package com.z2d.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z2d.beans.StockComanyInfo;
import com.z2d.beans.StockPrice;
import com.z2d.financeData.HistoryType;

public class DAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAO.class);

	/**
	 * GET ALL COMPANIES WITH PRIORITY
	 * 
	 * @return
	 */
	public List<StockComanyInfo> getCompanyList(HistoryType historyType) {
		String query = "select Top 1000 s.[company_id],s.[company_name],s.[company_symbol],s.[last_update_date],s.[stock_source_id] from [dbo].[Stock_Company_Info] s "
			       	 + "inner join [dbo].[core_company] com on s .[company_id]=com.[CompanyComID] "
				     + "where [stock_source_id]=? and ( CAST(s.[last_update_date] AS DATE) < CAST(GETDATE() AS DATE) or s.[last_update_date] is null) "
				     + "order by  com.[PriorityId] asc , s.[last_update_date] asc";

		Connection dbConn = null;
		PreparedStatement ps = null;

		List<StockComanyInfo> stockComanyInfosList = new ArrayList<StockComanyInfo>();

		try {
			dbConn = DB.getConnection();
			ps = dbConn.prepareStatement(query);
			ps.setInt(1, historyType.getID());

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				StockComanyInfo stockComanyInfo = new StockComanyInfo();
				stockComanyInfo.setCompanyID(resultSet.getInt(1));
				stockComanyInfo.setCompanyName(resultSet.getString(2));
				stockComanyInfo.setCompanySymol(resultSet.getString(3));
				stockComanyInfo.setLastUpdateDate(resultSet.getDate(4));
				stockComanyInfo.setStockSourceID(resultSet.getInt(5));

				stockComanyInfosList.add(stockComanyInfo);
			}

		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			clearResources(dbConn, ps);
		}

		return stockComanyInfosList;
	}

	/**
	 * INSERT HISTORICAL DATA
	 * 
	 * @param stockComanyInfosList
	 */
	/*
	 * public void insertHistoricalData(List<StockComanyInfo>
	 * stockComanyInfosList) { String sql =
	 * "IF NOT EXISTS(SELECT top 1 [Stock_Close],[Stock_Open], [Stock_High], [Stock_Low], [Stock_Volume], [Stock_AdjClose], [Currency] FROM [Sourcing].[dbo].[StockPrice] WHERE [CompanyID] = ? and [Stock_date]= ? and [Stock_Open]= ?)"
	 * +
	 * "INSERT INTO [Sourcing].[dbo].[StockPrice]([CompanyID],[Stock_date],[EgyptID],[Stock_Close],[Stock_Open], [Stock_High], [Stock_Low], [Stock_Volume], [Stock_AdjClose], [Currency])"
	 * + "VALUES" + "(?, ? ,0,?, ?, ?, ?, ?, ?, ?)";
	 * 
	 * Connection dbConn = null; PreparedStatement ps = null; try { dbConn =
	 * DB.getConnection(); ps = dbConn.prepareStatement(sql);
	 * 
	 * for (StockComanyInfo stockComanyInfo : stockComanyInfosList) {
	 * ps.setInt(1, stockComanyInfo.getCompanyID());
	 * 
	 * List<StockPrice> stockPrices = stockComanyInfo.getStockPriceList();
	 * 
	 * 
	 * for (StockPrice stockPrice : stockPrices) { ps.setString(2,
	 * stockPrice.getStockDate()); ps.setString(3, stockPrice.getStockOpen());
	 * ps.setInt(4, stockComanyInfo.getCompanyID()); ps.setString(5,
	 * stockPrice.getStockDate()); ps.setString(6, stockPrice.getStockClose());
	 * ps.setString(7, stockPrice.getStockOpen()); ps.setString(8,
	 * stockPrice.getStockHigh()); ps.setString(9, stockPrice.getStockLow());
	 * ps.setString(10, stockPrice.getStockVolume()); ps.setString(11,
	 * stockPrice.getStockAdjClose()); ps.setString(12,
	 * stockPrice.getCurrencey());
	 * 
	 * ps.addBatch(); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * if (ps != null) { int[] arr = ps.executeBatch(); LOGGER.info(
	 * "Number Of inserted Rows:" + arr.length); }
	 * 
	 * } catch (Exception ex) { LOGGER.error("", ex); } }
	 */

	/**
	 * Insert Stock price for given Data
	 * 
	 * @param stockComanyInfo
	 */
	public void insertHistoricalData(StockComanyInfo stockComanyInfo) {
		String sql = "IF NOT EXISTS(SELECT top 1 [Stock_ID] FROM [dbo].[StockPrice] WHERE [CompanyID] = ? and [Stock_date]= ? and [Stock_Open]= ?)"
				+ "INSERT INTO [dbo].[StockPrice]([CompanyID],[Stock_date],[Market_Cap],[EgyptID],[FiscalYear],[Type],[Stock_Close],[Stock_Open], [Stock_High], [Stock_Low], [Stock_Volume], [Stock_AdjClose], [Currency])"
				+ "VALUES" + "(?, ? ,?,?,?,?, ?, ?, ?,?, ?, ?, ?)";

		Connection dbConn = null;
		PreparedStatement ps = null;
		try {
			dbConn = DB.getConnection();
			ps = dbConn.prepareStatement(sql);
			dbConn.setAutoCommit(false);
			
			ps.setInt(1, stockComanyInfo.getCompanyID());

			List<StockPrice> stockPrices = stockComanyInfo.getStockPriceList();

			for (StockPrice stockPrice : stockPrices) {
				ps.setString(2, stockPrice.getStockDate());
				ps.setString(3, stockPrice.getStockOpen());
				ps.setInt(4, stockComanyInfo.getCompanyID());
				ps.setString(5, stockPrice.getStockDate());
				ps.setString(6, "");
				ps.setInt(7, 0);
				ps.setString(8, "");
				ps.setString(9, "");
				ps.setString(10, stockPrice.getStockClose());
				ps.setString(11, stockPrice.getStockOpen());
				ps.setString(12, stockPrice.getStockHigh());
				ps.setString(13, stockPrice.getStockLow());
				ps.setString(14, stockPrice.getStockVolume());
				ps.setString(15, stockPrice.getStockAdjClose());
				ps.setString(16, stockPrice.getCurrencey());

				ps.addBatch();
			}

			if (ps != null) {
				int[] arr = ps.executeBatch();
				LOGGER.info("Number Of inserted Rows:" + arr.length);
				dbConn.commit();
			}

		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
		finally {
			clearResources(dbConn, ps);
		}
	}

	/**
	 * UPDATE STOCK SOURCE ID
	 * With SourceID= 1 OR 2
	 * @param stockComanyInfo
	 */
	public void updateStockCompanyInfoSourceID(StockComanyInfo stockComanyInfo)
	{
		String sql = "update [dbo].[Stock_Company_Info] set  [stock_source_id]= ? ,[last_update_date]=GETDATE() where [company_id]= ?";

		Connection dbConn = null;
		PreparedStatement ps = null;
		try {
			dbConn = DB.getConnection();
			ps = dbConn.prepareStatement(sql);
			dbConn.setAutoCommit(false);
			ps.setInt(1, stockComanyInfo.getStockSourceID());
			ps.setInt(2,stockComanyInfo.getCompanyID());
			ps.executeUpdate();
			dbConn.commit();
			
			LOGGER.info("Success Update :"+stockComanyInfo.getCompanySymol()+"\t"+stockComanyInfo.getCompanyID()+"\t"+stockComanyInfo.getStockSourceID());

		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
		finally {
			clearResources(dbConn, ps);	
		}
	}
	
	/**
	 * insert multiple record only data into stock company info table
	 */
	public void insertCompanyList(List<StockComanyInfo> stockComanyInfos) {
		String query = "INSERT INTO dbo.Stock_Company_Info (company_id, company_name , company_symbol ,stock_source_id) VALUES (?,?,?,?)";

		Connection dbConn = null;
		PreparedStatement ps = null;

		try {
			dbConn = DB.getConnection();
			ps = dbConn.prepareStatement(query);

			for (StockComanyInfo stockComanyInfo : stockComanyInfos) {
				ps.setInt(1, stockComanyInfo.getCompanyID());
				ps.setString(2, stockComanyInfo.getCompanyName());
				ps.setString(3, stockComanyInfo.getCompanySymol());
				ps.setInt(4, stockComanyInfo.getStockSourceID());

				ps.addBatch();
			}

			if (ps != null) {
				int[] arr = ps.executeBatch();
				LOGGER.info("Number Of inserted Rows:" + arr.length + " size of list:" + stockComanyInfos.size());
			}

		} catch (Exception e) {
			LOGGER.error("Found ", e);
		} finally {
			clearResources(dbConn, ps);
		}
	}

	/**
	 * Close Data base Connections
	 * 
	 * @param dbConn
	 * @param ps
	 */
	private void clearResources(Connection dbConn, Statement ps) {
		try {

			if (null != ps) {
				ps.close();
			}
			if (null != dbConn) {
				dbConn.commit();
				dbConn.close();
			}
		} catch (SQLException e) {
			LOGGER.error("failed to clear DB Resources", e);
		}
	}

}
