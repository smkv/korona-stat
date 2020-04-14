import ee.smkv.covid19.estonia.Chart;
import ee.smkv.covid19.estonia.Data;
import ee.smkv.covid19.estonia.DigiliguOpenDataProvider;
import ee.smkv.covid19.estonia.Statistics;

import java.io.IOException;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    final DigiliguOpenDataProvider provider = new DigiliguOpenDataProvider();
    List<Data> covid19TestResults = provider.getCovid19TestResults();
    final Statistics statistics = new Statistics(covid19TestResults);

    Chart chart = new Chart("CONVID19 Estonia", "Time", "Count", 1200, 600);
    chart.addSeries("Estonia", statistics.getPositiveByDays());
    chart.addSeries("Harju maakond", statistics.getPositiveByDays("Harju maakond"));
    chart.addSeries("Moving avg(7)", statistics.getMovingAverageByDays(7));
    chart.addSeries("Harju maakond avg(7)", statistics.getMovingAverageByDays("Harju maakond", 7));
    chart.show();
  }
}
