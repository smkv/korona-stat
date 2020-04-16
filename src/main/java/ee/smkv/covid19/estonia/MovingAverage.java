package ee.smkv.covid19.estonia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.TreeMap;

public class MovingAverage {
  private final StatisticsByDate source;

  public MovingAverage(StatisticsByDate source) {
    this.source = source;
  }

  public static StatisticsByDate getMovingAverage(final StatisticsByDate source, final int n) {
    return new MovingAverage(source).getMovingAverage(n);
  }

  public StatisticsByDate getMovingAverage(final int n) {
    if (n < 1) {
      throw new IllegalArgumentException("n < 1");
    }

    final TreeMap<LocalDate, Double> result = new TreeMap<>();
    source.forEach((key, value) -> {
      List<Number> values = new ArrayList<>(n);
      for (int i = n - 1; i > 0; i--) {
        LocalDate date = key.minusDays(i);
        if (source.containsKey(date)) {
          values.add(source.get(date));
        }
      }
      values.add(value);
      OptionalDouble average = values.stream().mapToDouble(Number::doubleValue).average();
      result.put(key, average.orElse(0));
    });
    return new StatisticsByDate(result);
  }
}
