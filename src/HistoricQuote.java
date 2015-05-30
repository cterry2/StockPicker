import java.util.Date;


public class HistoricQuote {
	int stockid;
	Date StockDate;
	long Volume;
	double DailyMinimum;
	double ClosingPrice;
	double DailyMax;
	double PercentOfExpected;
	double ThirtyDayPercentOfExpected;
	double SixtyDayPercentOfExpected;
	
	public void setStockId(int id){
		this.stockid = id;
	}
	
	public int getStockId(){
		return stockid;
	}
	
	public void setStockDate(Date stockdate){
		this.StockDate = stockdate;
	}
	
	public Date getStockDate(){
		return StockDate;
	}
	
	public void setVolume(long volume){
		this.Volume = volume;
	}
	
	public long getVolume(){
		return Volume;
	}
	
	public void setDailyMinimum(double dailyminimum){
		this.DailyMinimum = dailyminimum;
	}
	
	public double getDailyMinimum(){
		return DailyMinimum;
	}
	
	public void setClosingPrice(double closingprice){
		this.ClosingPrice = closingprice;
	}
	
	public double getClosingPrice(){
		return ClosingPrice;
	}
	
	public void setDailyMax(double dailymax){
		this.DailyMax = dailymax;
	}
	
	public double getDailyMax(){
		return DailyMax;
	}
	
	public void setPercentOfExpected(double poe){
		this.PercentOfExpected = poe;
	}
	
	public double getPercentOfExpected(){
		return PercentOfExpected;
	}
	
	public void setThirtyDayPercentOfExpected(double poe){
		this.ThirtyDayPercentOfExpected = poe;
	}
	
	public double getThirtyDayPercentOfExpected(){
		return ThirtyDayPercentOfExpected;
	}
	
	public void setSixtyDayPercentOfExpected(double poe){
		this.SixtyDayPercentOfExpected = poe;
	}
	
	public double getSixtyDayPercentOfExpected(){
		return SixtyDayPercentOfExpected;
	}
}
