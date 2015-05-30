import java.util.ArrayList;
import java.util.List;

public class Calculation {
	public double BuildExpected(ArrayList<HistoricQuote> history){
		double thirtyAverage = ClosingPrice((ArrayList<HistoricQuote>) GetLastThirtyDays(history, 30), 3);
		List<Projection> myProjections = Projections(history);
		double nextDay = (myProjections.get(0).getNextDay() 
								+ myProjections.get(1).getNextDay() 
								+ myProjections.get(2).getNextDay()) / 3.00;
		
		double baseAmount = BaseAverage(history, 2);
		double fullAverage = ClosingPrice(history, 3);		
		double sixtyAverage = ClosingPrice((ArrayList<HistoricQuote>) GetLastThirtyDays(history, 60), 3);		
		
		double findFactor = Math.round((nextDay + baseAmount + fullAverage) / (thirtyAverage * 3));
		
		if(findFactor <= 1.0){
			findFactor = 1.0;
		}else{
			findFactor = findFactor * .875;
		}
		
		double averageAverage = ((baseAmount / findFactor) + (fullAverage / findFactor) + thirtyAverage + sixtyAverage) / 4.00;
		
		return ((nextDay / findFactor) + averageAverage) / 2.00;
	}
	private ArrayList<Projection> Projections(final ArrayList<HistoricQuote> history) {
		final double baseClosing = BaseAverage(history, 2);
		final double baseMax = BaseAverage(history, 1);
		final double baseMin = BaseAverage(history, 3);
		
		final double factorClosing = 1.00 / GetStandardDeviation(history, 2);
		final double factorMax = 1.00 / GetStandardDeviation(history, 1);
		final double factorMin = 1.00 / GetStandardDeviation(history, 3);
		
		final double sineStddivClosing = GetStandardDeviation((ArrayList<HistoricQuote>) GetLastThirtyDays(history, 30), 2) / 2.00;
		final double sineStddivMax = GetStandardDeviation((ArrayList<HistoricQuote>) GetLastThirtyDays(history, 30), 1) / 2.00;
		final double sineStddivMin = GetStandardDeviation((ArrayList<HistoricQuote>) GetLastThirtyDays(history, 30), 3) / 2.00;
		
		double previousClosing = 0.00;
		double previousMax = 0.00;
		double previousMin = 0.00;
		
		List<Double> factorsClosing = new ArrayList<Double>();
		List<Double> factorsMax = new ArrayList<Double>();
		List<Double> factorsMin = new ArrayList<Double>();
		
		double count = 0.00;

		for (HistoricQuote entity : history) {
			if (count > 0) {
				double adjustmentClosing = entity.getClosingPrice()	- baseClosing;
				double adjustmentMax = entity.getDailyMax() - baseMax;
				double adjustmentMin = entity.getDailyMinimum() - baseMin;				

				double diffClosing = adjustmentClosing - previousClosing;
				double diffMax = adjustmentMax - previousMax;
				double diffMin = adjustmentMin - previousMin;

				double buildClosing = BuildSineFactor(count, factorClosing, diffClosing);
				if(buildClosing > 0.00){
					factorsClosing.add(buildClosing);
				}
				
				double buildMax = BuildSineFactor(count, factorMax, diffMax);
				if(buildMax > 0.00){
					factorsMax.add(buildMax);
				}
				
				double buildMin = BuildSineFactor(count, factorMin, diffMin);
				if(buildMin > 0.00){
					factorsMin.add(buildMin);
				}

				previousClosing = adjustmentClosing;
				previousMax = adjustmentMax;
				previousMin = adjustmentMin;
			}
			count++;
		}
		
		final double sineFactorClosing = factorsClosing.size() > 0 ? FactorAverage(factorsClosing) : 1.00;
		final double sineFactorMax = factorsMax.size() > 0 ? FactorAverage(factorsMax) : 1.00;
		final double sineFactorMin = factorsMin.size() > 0 ? FactorAverage(factorsMin) : 1.00;
		
		List<Projection> projectionList =  new ArrayList<Projection>(); 
			
			

		projectionList.add(new Projection() {
					{
						setNextDay(BuildPrediction((double) history.size() + 1,
								factorMax, sineFactorMax, sineStddivMax) + baseMax);
						setThirtyDay(BuildPrediction(
								(double) history.size() + 30, factorMax,
								sineFactorMax, sineStddivMax) + baseMax);
						setSixtyDay(BuildPrediction(
								(double) history.size() + 30, factorMax,
								sineFactorMax, sineStddivMax) + baseMax);
					}
				});
		projectionList.add(new Projection() {
					{
						setNextDay(BuildPrediction((double) history.size() + 1,
								factorClosing, sineFactorClosing, sineStddivClosing) + baseClosing);
						setThirtyDay(BuildPrediction(
								(double) history.size() + 30, factorClosing,
								sineFactorClosing, sineStddivClosing) + baseClosing);
						setSixtyDay(BuildPrediction(
								(double) history.size() + 30, factorClosing,
								sineFactorClosing, sineStddivClosing) + baseClosing);
					}
				});
		projectionList.add(new Projection() {
					{
						setNextDay(BuildPrediction((double) history.size() + 1,
								factorMin, sineFactorMin, sineStddivMin) + baseMin);
						setThirtyDay(BuildPrediction(
								(double) history.size() + 30, factorMin,
								sineFactorMin, sineStddivMin) + baseMin);
						setSixtyDay(BuildPrediction(
								(double) history.size() + 30, factorMin,
								sineFactorMin, sineStddivMin) + baseMin);
					}
				});
		return (ArrayList<Projection>) projectionList;
	}
	
	private double BuildPrediction(double count, double baseFactor, double sineFactor, double sineMultiplier){
		return (Math.sin(count / sineFactor) * sineMultiplier) + (baseFactor * count);
	}
	
	private double FactorAverage(List<Double> factorsMin){
		double total = 0.00;
		for(double entity : factorsMin){
			total += entity;
		}
		
		return total / (double)factorsMin.size();
	}
	
 	private double BuildSineFactor(double count, double factor, double diff){	
		for(double i = 1.00; i < 1000.00; i++){
			double x = Math.sin(count / i) + (factor * count);
			
			//System.out.println(diff + " and " + x);
			
			if (Math.abs(diff / x) <= 1.10 && Math.abs(diff / x) >= .90){
				System.out.println(diff + " and " + x);
				return i;
			}
			
		}		
		
		return 0.00;
	}

	public double BaseAverage(ArrayList<HistoricQuote> history, int type) {
		if (type == 1) {
			return (DailyMax(history, 1) + DailyMax(history, 2)) / 2.00;
		} else if (type == 2) {
			return (ClosingPrice(history, 1) + ClosingPrice(history, 2)) / 2.00;
		} else if (type == 3) {
			return (DailyMin(history, 1) + DailyMin(history, 2)) / 2.00;
		}

		return 0.00;
	}

	public double GetStandardDeviation(ArrayList<HistoricQuote> history,
			int type) {
		if (type == 1) {
			return DailyMax(history, 4);
		} else if (type == 2) {
			return ClosingPrice(history, 4);
		} else if (type == 3) {
			return DailyMin(history, 4);
		}

		return 0.00;
	}

	public double GetAverage(ArrayList<HistoricQuote> history, int type) {
		if (type == 1) {
			return DailyMax(history, 3);
		} else if (type == 2) {
			return ClosingPrice(history, 3);
		} else if (type == 3) {
			return DailyMin(history, 3);
		}

		return 0.00;
	}

	public double GetMin(ArrayList<HistoricQuote> history, int type) {
		if (type == 1) {
			return DailyMax(history, 1);
		} else if (type == 2) {
			return ClosingPrice(history, 1);
		} else if (type == 3) {
			return DailyMin(history, 1);
		}

		return 0.00;
	}

	public double GetMax(ArrayList<HistoricQuote> history, int type) {
		if (type == 1) {
			return DailyMax(history, 2);
		} else if (type == 2) {
			return ClosingPrice(history, 2);
		} else if (type == 3) {
			return DailyMin(history, 2);
		}

		return 0.00;
	}

	private double ClosingPrice(ArrayList<HistoricQuote> history, int type) {
		double price = history.get(0).getClosingPrice();

		if (type == 1) {
			for (HistoricQuote entity : history) {
				if (entity.getClosingPrice() > price) {
					price = entity.getClosingPrice();
				}
			}
		} else if (type == 2) {
			for (HistoricQuote entity : history) {
				if (entity.getClosingPrice() < price) {
					price = entity.getClosingPrice();
				}
			}
		} else if (type == 3) {
			double average = 0.00;

			for (HistoricQuote entity : history) {
				average += entity.getClosingPrice();
			}
			average = average / (double) history.size();

			price = average;
		} else if (type == 4) {
			double stddiv = 0;

			for (HistoricQuote entity : history) {
				stddiv += (entity.getClosingPrice() * entity.getClosingPrice());
			}
			stddiv = Math.sqrt(stddiv) / (double) history.size();

			price = stddiv;
		}

		return price;
	}

	private double DailyMin(ArrayList<HistoricQuote> history, int type) {
		double price = history.get(0).getClosingPrice();

		if (type == 1) {
			for (HistoricQuote entity : history) {
				if (entity.getClosingPrice() > price) {
					price = entity.getClosingPrice();
				}
			}
		} else if (type == 2) {
			for (HistoricQuote entity : history) {
				if (entity.getClosingPrice() < price) {
					price = entity.getClosingPrice();
				}
			}
		} else if (type == 3) {
			double average = 0.00;

			for (HistoricQuote entity : history) {
				average += entity.getDailyMinimum();
			}
			average = average / (double) history.size();

			price = average;
		} else if (type == 4) {
			double stddiv = 0;

			for (HistoricQuote entity : history) {
				stddiv += (entity.getDailyMinimum() * entity.getDailyMinimum());
			}
			stddiv = Math.sqrt(stddiv) / (double) history.size();

			price = stddiv;
		}

		return price;
	}

	private double DailyMax(ArrayList<HistoricQuote> history, int type) {
		double price = history.get(0).getClosingPrice();

		if (type == 1) {
			for (HistoricQuote entity : history) {
				if (entity.getClosingPrice() > price) {
					price = entity.getClosingPrice();
				}
			}
		} else if (type == 2) {
			for (HistoricQuote entity : history) {
				if (entity.getClosingPrice() < price) {
					price = entity.getClosingPrice();
				}
			}
		} else if (type == 3) {
			double average = 0.00;

			for (HistoricQuote entity : history) {
				average += entity.getDailyMax();
			}
			average = average / (double) history.size();

			price = average;
		} else if (type == 4) {
			double stddiv = 0;

			for (HistoricQuote entity : history) {
				stddiv += (entity.getDailyMax() * entity.getDailyMax());
			}
			stddiv = Math.sqrt(stddiv) / (double) history.size();

			price = stddiv;
		}

		return price;
	}
	
	private List<HistoricQuote> GetLastThirtyDays(List<HistoricQuote> history, int duration){
		List<HistoricQuote> finalList = new ArrayList<HistoricQuote>();
		for(int i = history.size() - (duration + 1); i < history.size(); i++){
			finalList.add(history.get(i));
		}
		
		return finalList;
	}

}
