package com.z2d.insertList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class YahooFinanceHistoricaleData {

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

		System.out.println("#######dATE:"+inData);
		SimpleDateFormat sdf = new SimpleDateFormat("YYY-MM-dd");
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -7);

		Stock stock = YahooFinance.get(inData.split("\t")[0]);
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
				getSymbolHistoryData(line, res);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) throws IOException {

		YahooFinanceHistoricaleData up = new YahooFinanceHistoricaleData();
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
