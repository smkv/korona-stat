package ee.smkv.covid19.estonia;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Statistics {
  private final List<Data> source;

  public Statistics(List<Data> source) {
    this.source = source;
  }

  public TreeMap<LocalDate, Long> getPositiveByDays() {
    return source.stream()
                 .filter(Data::isPositive)
                 .sorted(comparing(Data::getStatisticsDate))
                 .collect(groupingBy(Data::getStatisticsDate, TreeMap::new, counting()));
  }

  public TreeMap<LocalDate, Long> getPositiveByDays(String county) {
    return source.stream()
                 .filter(Data::isPositive)
                 .filter(data -> county.equalsIgnoreCase(data.getCounty()))
                 .sorted(comparing(Data::getStatisticsDate))
                 .collect(groupingBy(Data::getStatisticsDate, TreeMap::new, counting()));
  }

  public TreeMap<LocalDate, Double> getPositiveMovingAverageByDays(int period) {
    return new MovingAverage(getPositiveByDays()).getMovingAverage(period);
  }

  public TreeMap<LocalDate, Double> getPositiveMovingAverageByDays(String county, int period) {
    return new MovingAverage(getPositiveByDays(county)).getMovingAverage(period);
  }
}
