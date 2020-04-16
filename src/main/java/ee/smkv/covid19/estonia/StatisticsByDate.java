package ee.smkv.covid19.estonia;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiConsumer;

public class StatisticsByDate {
  private final TreeMap<LocalDate, ? extends Number> data;

  public StatisticsByDate(TreeMap<LocalDate, ? extends Number> data) {
    this.data = data;
  }

  public TreeMap<LocalDate, ? extends Number> getData() {
    return data;
  }

  public void forEach(BiConsumer<? super LocalDate, ? super Number> action) {
    data.forEach(action);
  }

  public Number getMaxValue() {
    Optional<? extends Number> max = data.values().stream().max(Comparator.comparing(Number::doubleValue));
    return max.isPresent() ? max.get() : Double.valueOf(0);
  }

  public boolean containsKey(LocalDate key) {
    return data.containsKey(key);
  }

  public Number get(LocalDate key) {
    return data.get(key);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticsByDate that = (StatisticsByDate)o;
    return data.equals(that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data);
  }

  @Override
  public String toString() {
    return "StatisticsByDate{" +
           "data=" + data +
           '}';
  }

  public int size() {
    return data.size();
  }
}
