import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CurrentQuote {
	long stockid;
	Date DateAndTime;
	double CurrentPrice;
	long CurrentVolume;
	boolean MetDailyExpectation;

	public void setStockId(long stockid) {
		this.stockid = stockid;
	}

	public long getStockId() {
		return stockid;
	}

	public void setDateAndTime(Date dateandtime) {
		this.DateAndTime = dateandtime;
	}

	public Date getDateAndTime() {
		return DateAndTime;
	}

	public void setCurrentPrice(double currentPrice) {
		this.CurrentPrice = currentPrice;
	}

	public double getCurrentPrice() {
		return CurrentPrice;
	}

	public void setCurrentVolume(long currentVolume) {
		this.CurrentVolume = currentVolume;
	}

	public long getCurrentVolume() {
		return CurrentVolume;
	}

	public void setMetDailyExpectation(boolean met) {
		this.MetDailyExpectation = met;
	}

	public boolean getMetDailyExpectation() {
		return MetDailyExpectation;
	}

	public void MapStockXMLToCurrentQuote(StockXML stockQuote) throws SQLException{
		StockDataRepository repo = new StockDataRepository();
		try {
			CurrentQuote newQuote = new CurrentQuote();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.ENGLISH);
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			Date quoteDate = df.parse(stockQuote.getDate());
			
			Date noTimeDate = formatter.parse(formatter.format(quoteDate));
			
			double expected = repo.GetDailyForcast(noTimeDate, stockQuote.getId());
			
			newQuote.setStockId(stockQuote.getId());
			newQuote.setCurrentPrice(stockQuote.getPrice());
			newQuote.setCurrentVolume(stockQuote.getVolume());
			newQuote.setDateAndTime(df.parse(stockQuote.getDate()));
			newQuote.setMetDailyExpectation(expected > 0.00 && stockQuote.getPrice() > expected);
			
			if(!repo.WriteCurrentStockData(newQuote))
				throw new SQLException();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			repo.Dispose();
		}
	}
}
