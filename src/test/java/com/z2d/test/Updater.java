package com.z2d.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.io.FileUtils;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class Updater {

	private List<String> readInput(String file) {
		List<String> inputList = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();

			while ((line = reader.readLine()) != null) {
				inputList.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputList;
	}

	private void getSymbolHistoryData(String inData, File results) throws IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("YYY-MM-dd");
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
//		from.add(Calendar.YEAR, -7);
		Date lastUpdateDate=new Date();
		System.out.println(lastUpdateDate);
//		lastUpdateDate.setYear(2017);
//		lastUpdateDate.setMonth(1);
//		lastUpdateDate.setDate(16);
//		GregorianCalendar lastUpdateDate = new GregorianCalendar(2017, 1, 17);
		System.out.println(lastUpdateDate.getYear()+"\t"+lastUpdateDate.getMonth()+"\t"+lastUpdateDate.getDate());
//		from.setTime(lastUpdateDate);
		from.set(lastUpdateDate.getYear(), lastUpdateDate.getMonth(), lastUpdateDate.getDay(), 0, 0, 0);
//		from.setTime(lastUpdateDate);
//		from.add(Calendar.DATE, -3);
		
		Stock stock = YahooFinance.get(inData.split("\t")[0]);
		//ARM	ARM Holdings plc 1000423
		//CIMT	CIMATRON LIMITED	1001174
		//CSR	CSR plc	1001446	false::::::
		
		if(stock.isValid())
		{
		//Checks if the returned name is null.
	System.out.println(	inData.split("\t")[0]+"\t"+inData.split("\t")[1]+"\t"+inData.split("\t")[2]+"\t"+stock.isValid() +"::::::");
		if (stock != null) {

			List<HistoricalQuote> symbolHistQuotes = stock.getHistory(from, to, Interval.DAILY);

			if (symbolHistQuotes != null && stock.getName() != "null" && stock.getCurrency() != "null") {

				for (HistoricalQuote historicalQuote : symbolHistQuotes) {

					StringBuilder sb = new StringBuilder();
					sb.append(inData).append("\t");
					sb.append(stock.getName()).append("\t");
					sb.append(stock.getCurrency()).append("\t");
					sb.append(sdf.format(historicalQuote.getDate().getTime())).append("\t");
					sb.append(historicalQuote.getOpen()).append("\t");
					sb.append(historicalQuote.getHigh()).append("\t");
					sb.append(historicalQuote.getLow()).append("\t");
					sb.append(historicalQuote.getClose()).append("\t");
					sb.append(historicalQuote.getVolume()).append("\t");
					sb.append(historicalQuote.getAdjClose()).append("\n");

					FileUtils.writeStringToFile(results, sb.toString(), true);
				}
			}
		}
		}
	}

	public void startRunning(String file) {
		
		List<String> in = readInput(file);

		File res = new File(file.replace(".txt", "_results.txt"));
		try {
			FileUtils.writeStringToFile(res,
					"Symbol\tZ company name\tZ company ID\tFoundCompany\tCurrency\tDate\tOpen\tHigh\tLow\tClose\tVolume\tAdj Close\n",
					false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < in.size(); i++) {

			System.out.println("Get Data for Symbol " + i + " out of " + in.size());
			String line = in.get(i);

			try {
				System.out.println("asdasd");
				getSymbolHistoryData(line, res);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) throws IOException {

		Updater up = new Updater();
		up.startRunning("input.txt");

		// Stock stock = YahooFinance.get("INTC");
		//
		// BigDecimal price = stock.getQuote().getPrice();
		// BigDecimal change = stock.getQuote().getChangeInPercent();
		// BigDecimal peg = stock.getStats().getPeg();
		// BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
		//
		// stock.print();

		// Calendar from = Calendar.getInstance();
		// Calendar to = Calendar.getInstance();
		// from.add(Calendar.YEAR, -1); // from 1 year ago
		//
		// Stock google = YahooFinance.get("GOOG");
		// List<HistoricalQuote> googleHistQuotes = google.getHistory(from, to,
		// Interval.DAILY);
		// SimpleDateFormat sdf = new SimpleDateFormat("YYY-MM-dd");
		// for (HistoricalQuote historicalQuote : googleHistQuotes) {
		//
		// System.out.println(sdf.format(historicalQuote.getDate().getTime()) +
		// "\t" + historicalQuote.getOpen() + "\t"
		// + historicalQuote.getHigh() + "\t" + historicalQuote.getLow() + "\t"
		// + historicalQuote.getClose()
		// + "\t" + historicalQuote.getVolume() + "\t" +
		// historicalQuote.getAdjClose());

		// }

	}

}
