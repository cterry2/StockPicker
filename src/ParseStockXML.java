import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParseStockXML {

	public List<StockXML> ParseXML() {

		try {
			String fileName = "/stockfile.xml";
			StockDataRepository repo = new StockDataRepository();
			
			Scanner scanner = new Scanner(new File(fileName));

			scanner.useDelimiter(System.getProperty("line.separator"));

			StockXML stock = new StockXML();
			List<StockXML> stockList = new ArrayList<StockXML>();

			while (scanner.hasNext()) {
				String line = scanner.next();
				int skip = 0;
				
				if (line.contains("</resource>")) {
					stockList.add(stock);
					skip = 1;
				} else if (line.contains("<resource classname=\"Quote\">")) {
					stock = new StockXML();
					skip = 1;
				}

				if (skip == 0) {
					if (line.contains("<field name=\"name\">")) {
						stock.setName(line
								.replace("<field name=\"name\">", "")
								.replace("</field>", "").trim());						
					} else

					if (line.contains("<field name=\"price\">")) {
						stock.setPrice(Float.parseFloat(line
								.replace("<field name=\"price\">", "")
								.replace("</field>", "").trim()));
					} else

					if (line.contains("<field name=\"symbol\">")) {
						String symbol = line
								.replace("<field name=\"symbol\">", "")
								.replace("</field>", "").trim();
						
						stock.setSymbol(symbol);
						
						Stock stockInfo = repo.GetListOfStocks(symbol);
						stock.setId((int) stockInfo.getId());						
					} else

					if (line.contains("<field name=\"ts\">")) {
						stock.setTs(Integer.parseInt(line
								.replace("<field name=\"ts\">", "")
								.replace("</field>", "").trim()));
					} else

					if (line.contains("<field name=\"type\">")) {
						stock.setType(line.replace("<field name=\"type\">", "")
								.replace("</field>", "").trim());
					} else

					if (line.contains("<field name=\"utctime\">")) {
						String date = line
								.replace("<field name=\"utctime\">", "")
								.replace("</field>", "").trim()
								.replace("T", " ");
						
						stock.setDate(date.substring(0, date.length() -5));
					} else

					if (line.contains("<field name=\"volume\">")) {
						stock.setVolume(Integer.parseInt(line
								.replace("<field name=\"volume\">", "")
								.replace("</field>", "").trim()));
					}
				}

			}
			System.out.println(stockList);

			scanner.close();

			return stockList;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return null;
	}
}
