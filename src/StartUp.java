
import java.sql.SQLException;
import java.util.ArrayList;


public class StartUp {
	public static void main(String[] args) throws SQLException {

		boolean success = new WriteXMLFile().WriteFile();
		if (success) {
			ParseStockXML parser = new ParseStockXML();
			parser.ParseXML();
			StockDataRepository repo = new StockDataRepository();
			Calculation calculator = new Calculation();
			try {
				ArrayList<Stock> stocks = repo.GetListOfStocks();
				//repo.ImportData();
				for (Stock entity : stocks) {
					ArrayList<HistoricQuote> myQuotes = repo.GetHistoricQuotes((int) entity.getId());
					if (myQuotes.size() > 0) {
						double base = calculator.BaseAverage(myQuotes, 2);
						double average = calculator.GetAverage(myQuotes, 2);
						double stddiv = calculator.GetStandardDeviation(myQuotes, 1);
						double myProjections = calculator.BuildExpected(myQuotes);

						String x = "";
					}
				}

			} finally {
				repo.Dispose();
			}
		}
	}
}
