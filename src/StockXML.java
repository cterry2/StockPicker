
import javax.xml.bind.annotation.XmlElement;


public class StockXML extends Stock{
	double price;
	
	int ts;
	
	String type;
	
	String utcdatetime;
	
	int Volume;
	
	
	public double getPrice() {
		return price;
	}
 
	@XmlElement
	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getTs() {
		return ts;
	}
 
	@XmlElement
	public void setTs(int ts) {
		this.ts = ts;
	}
	
	public String getType() {
		return type;
	}
 
	@XmlElement
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDate() {
		return utcdatetime;
	}
 
	@XmlElement
	public void setDate(String utcdatetime) {
		this.utcdatetime = utcdatetime;
	}
	
	public int getVolume() {
		return Volume;
	}
 
	@XmlElement
	public void setVolume(int volume) {
		this.Volume = volume;
	}
}
