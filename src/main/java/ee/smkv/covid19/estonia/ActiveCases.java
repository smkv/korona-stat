package ee.smkv.covid19.estonia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ActiveCases {
  private final StatisticsByDate source;

  public ActiveCases(StatisticsByDate source) {
    this.source = source;
  }

  public static StatisticsByDate calculate(final StatisticsByDate source, final int recoveryDays) {
    return new ActiveCases(source).calculate(recoveryDays);
  }

  public StatisticsByDate calculate(final int recoveryDays) {
    if (recoveryDays < 1) {
      throw new IllegalArgumentException("recoveryDays < 1");
    }

    final TreeMap<LocalDate, Long> result = new TreeMap<>();
    source.forEach((key, value) -> {
      List<Number> values = new ArrayList<>(recoveryDays);
      for (int i = recoveryDays - 1; i > 0; i--) {
        LocalDate date = key.minusDays(i);
        if (source.containsKey(date)) {
          values.add(source.get(date));
        }
      }
      values.add(value);
      long sum = values.stream().mapToLong(Number::longValue).sum();
      result.put(key, sum);
    });
    return new StatisticsByDate(result);
  }
}
