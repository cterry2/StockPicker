import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class StockDataRepository {
	private Connection db;

	public StockDataRepository() {
		db = (Connection) new MySQLDatabase().DBConnect();
	}

	public void Dispose() throws SQLException {
		db.close();
	}

	public ArrayList<Stock> GetListOfStocks() {
		String query = "SELECT id, Symbol, Name FROM StockData.Stocks;";
		try {
			Statement sqlStatement = (Statement) db.createStatement();
			ResultSet results = sqlStatement.executeQuery(query);
			List<Stock> myStocks = new ArrayList<Stock>();

			while (results.next()) {
				Stock stock = new Stock();
				stock.setId(results.getInt("id"));
				stock.setSymbol(results.getString("Symbol"));
				stock.setName(results.getString("Name"));

				myStocks.add(stock);
			}

			return (ArrayList<Stock>) myStocks;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Stock GetListOfStocks(String symbol) {
		String query = "SELECT id, Symbol, Name FROM StockData.Stocks WHERE Symbol = " + symbol;
		try {
			Statement sqlStatement = (Statement) db.createStatement();
			ResultSet results = sqlStatement.executeQuery(query);
			
			Stock stock = new Stock();
			
			while (results.next()) {
				
				stock.setId(results.getInt("id"));
				stock.setSymbol(results.getString("Symbol"));
				stock.setName(results.getString("Name"));				
			}

			return stock;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void ImportData() {
		try {
			String fileName = "/table.csv";
			String query = "";

			Scanner scanner = new Scanner(new File(fileName));

			scanner.useDelimiter(System.getProperty("line.separator"));

			while (scanner.hasNext()) {
				String[] line = scanner.next().split("\t");

				query += "insert into StockData.HistoricQuotes ";
				query += "(stockid, StockDate, Volume, DailyMinimum, ClosingPrice,  DailyMax, PercentOfExpected, 30DayPercentOfExpected, 60DayPercentOfExpected)";
				query += " Values(";
				query += line[0] + ", '" + line[1] + "', " + line[2] + ", "
						+ line[3] + ", " + line[4] + ", " + line[5] + ", "
						+ line[6] + ", " + line[7] + ", " + line[8];
				query += ");";
				if (query != "") {
					PreparedStatement preparedStmt = (PreparedStatement) db
							.prepareStatement(query);
					preparedStmt.execute();

					query = "";
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public ArrayList<HistoricQuote> GetHistoricQuotes(int id) {
		String query = "SELECT stockid, StockDate, Volume, DailyMinimum, ClosingPrice, DailyMax, PercentOfExpected, 30DayPercentOfExpected, 60DayPercentOfExpected FROM StockData.HistoricQuotes WHERE stockid = "
				+ id + " ORDER BY StockDate asc;";

		try {
			Statement sqlStatement = (Statement) db.createStatement();
			ResultSet results = sqlStatement.executeQuery(query);
			List<HistoricQuote> myStocks = new ArrayList<HistoricQuote>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

			while (results.next()) {
				HistoricQuote stock = new HistoricQuote();
				stock.setStockId(results.getInt("stockid"));
				stock.setStockDate(df.parse(results.getString("StockDate")));
				stock.setVolume(Integer.parseInt(results.getString("Volume")));
				stock.setDailyMinimum(results.getDouble("DailyMinimum"));
				stock.setClosingPrice(results.getDouble("ClosingPrice"));
				stock.setDailyMax(results.getDouble("DailyMax"));
				stock.setPercentOfExpected(results
						.getDouble("PercentOfExpected"));
				stock.setThirtyDayPercentOfExpected(results
						.getDouble("30DayPercentOfExpected"));
				stock.setSixtyDayPercentOfExpected(results
						.getDouble("60DayPercentOfExpected"));

				myStocks.add(stock);
			}

			return (ArrayList<HistoricQuote>) myStocks;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public boolean WriteCurrentStockData(CurrentQuote quote) {
		String query = "";
		try {
			query += "insert into StockData.CurrentQuotes ";
			query += "(stockid, dateandtime, currentprice, currentvolume, metdailyexpectation)";
			query += " Values(";
			query += quote.getStockId() + ", '" + quote.getDateAndTime()
					+ "', " + quote.getCurrentPrice() + ", "
					+ quote.getCurrentVolume() + ", "
					+ quote.getMetDailyExpectation();
			query += ");";

			PreparedStatement preparedStmt = (PreparedStatement) db
					.prepareStatement(query);
			preparedStmt.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public double GetDailyForcast(Date day, long id) throws SQLException{
		String query = "SELECT ExpectedFinishPrice FROM StockData.StockForcasts where stockid = " + id + " and MarketDate = " + day;
		
		Statement sqlStatement = (Statement) db.createStatement();
		ResultSet results = sqlStatement.executeQuery(query);
		
		return results.next() ? results.getDouble("ExpectedFinishPrice") : 0.00;
	}
}
