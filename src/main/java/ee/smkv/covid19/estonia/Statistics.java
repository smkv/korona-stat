package ee.smkv.covid19.estonia;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Statistics {
  private final List<TestResult> source;

  public Statistics(List<TestResult> source) {
    this.source = source;
  }

  public StatisticsByDate getPositiveByDays() {
    TreeMap<LocalDate, Long> map = source.stream()
                                         .filter(TestResult::isPositive)
                                         .sorted(comparing(TestResult::getStatisticsDate))
                                         .collect(groupingBy(TestResult::getStatisticsDate, TreeMap::new, counting()));
    return new StatisticsByDate(fillMissingDates(map));
  }

  public StatisticsByDate getPositiveByDays(String county) {
    TreeMap<LocalDate, Long> map = source.stream()
                                         .filter(TestResult::isPositive)
                                         .filter(testResult -> county.equalsIgnoreCase(testResult.getCounty()))
                                         .sorted(comparing(TestResult::getStatisticsDate))
                                         .collect(groupingBy(TestResult::getStatisticsDate, TreeMap::new, counting()));
    return new StatisticsByDate(fillMissingDates(map));
  }

  private TreeMap<LocalDate, Long> fillMissingDates(TreeMap<LocalDate, Long> map) {
    LocalDate startDate = getStartDate();
    LocalDate endDate = getEndDate();
    for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
      if (!map.containsKey(date)) {
        map.put(date, 0L);
      }
    }
    return map;
  }

  private LocalDate getStartDate() {
    return source.stream().map(TestResult::getStatisticsDate).min(LocalDate::compareTo).orElseThrow(IllegalArgumentException::new);
  }

  private LocalDate getEndDate() {
    return source.stream().map(TestResult::getStatisticsDate).max(LocalDate::compareTo).orElseThrow(IllegalArgumentException::new);
  }
}
