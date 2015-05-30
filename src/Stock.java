import javax.xml.bind.annotation.XmlElement;


public class Stock {
	long Id;
	String Symbol;
	String Name;
	
	public long getId(){
		return Id;
	}
	
	public void setId(int id){
		this.Id = id;
	}
	
	public String getName() {
		return Name;
	}
 
	@XmlElement
	public void setName(String name) {
		this.Name = name;
	}
	
	public String getSymbol() {
		return Symbol;
	}
 
	@XmlElement
	public void setSymbol(String symbol) {
		this.Symbol = symbol;
	}
}
