import ee.smkv.covid19.estonia.Chart;
import ee.smkv.covid19.estonia.TestResult;
import ee.smkv.covid19.estonia.DigiliguOpenDataProvider;
import ee.smkv.covid19.estonia.MovingAverage;
import ee.smkv.covid19.estonia.Statistics;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

public class Main {
  public static void main(String[] args) throws IOException {
    final DigiliguOpenDataProvider provider = new DigiliguOpenDataProvider();
    List<TestResult> covid19TestResults = provider.getCovid19TestResults();
    final Statistics statistics = new Statistics(covid19TestResults);
    TreeMap<LocalDate, Long> positiveInEstonia = statistics.getPositiveByDays();
    TreeMap<LocalDate, Long> positiveInHarjuCounty = statistics.getPositiveByDays("Harju maakond");

    Chart chart = new Chart("COVID19 Estonia", "Time", "Count", 1200, 600);
    chart.addSeries("Estonia", positiveInEstonia);
    chart.addSeries("Harju maakond", positiveInHarjuCounty);
    chart.addSeries("Moving avg(7)", MovingAverage.getMovingAverage(positiveInEstonia, 7));
    chart.addSeries("Harju maakond avg(7)", MovingAverage.getMovingAverage(positiveInHarjuCounty, 7));
    chart.show();
  }
}
