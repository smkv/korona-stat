package ee.smkv.covid19.estonia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.TreeMap;

public class MovingAverage {
  private final TreeMap<LocalDate, Long> source;

  public MovingAverage(TreeMap<LocalDate, Long> source) {
    this.source = source;
  }

  public static TreeMap<LocalDate, Double> getMovingAverage(final TreeMap<LocalDate, Long> source, final int n) {
    return new MovingAverage(source).getMovingAverage(n);
  }

  public TreeMap<LocalDate, Double> getMovingAverage(final int n) {
    if (n < 1) {
      throw new IllegalArgumentException("n < 1");
    }

    final TreeMap<LocalDate, Double> result = new TreeMap<>();
    source.forEach((key, value) -> {
      List<Long> values = new ArrayList<>(n);
      for (int i = n - 1; i > 0; i--) {
        LocalDate date = key.minusDays(i);
        if (source.containsKey(date)) {
          values.add(source.get(date));
        }
      }
      values.add(value);
      OptionalDouble average = values.stream().mapToLong(Long::longValue).average();
      result.put(key, average.orElse(0));
    });
    return result;
  }
}
