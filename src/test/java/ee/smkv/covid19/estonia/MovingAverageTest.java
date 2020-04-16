package ee.smkv.covid19.estonia;

import org.junit.Test;

import java.time.LocalDate;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class MovingAverageTest {

  @Test
  public void getMovingAverageEmpty() {
    TreeMap<LocalDate, Long> source = new TreeMap<>();
    StatisticsByDate actual = MovingAverage.getMovingAverage(new StatisticsByDate(source), 1);
    assertEquals(0, actual.size());
  }

  @Test
  public void getMovingAverageEmptyOne() {
    TreeMap<LocalDate, Long> source = new TreeMap<>();
    source.put(LocalDate.now(), 10L);
    StatisticsByDate actual = MovingAverage.getMovingAverage(new StatisticsByDate(source), 1);
    assertEquals(10D, actual.get(LocalDate.now()).doubleValue(), 0);
    assertEquals(1, actual.size());
  }

  @Test
  public void getMovingAverage() {
    TreeMap<LocalDate, Long> source = new TreeMap<>();
    source.put(LocalDate.now(), 10L);
    source.put(LocalDate.now().plusDays(1), 20L);
    source.put(LocalDate.now().plusDays(2), 30L);
    source.put(LocalDate.now().plusDays(3), 40L);
    StatisticsByDate actual = MovingAverage.getMovingAverage(new StatisticsByDate(source), 2);
    assertEquals(10D, actual.get(LocalDate.now()).doubleValue(), 0);
    assertEquals(15D, actual.get(LocalDate.now().plusDays(1)).doubleValue(), 0);
    assertEquals(25D, actual.get(LocalDate.now().plusDays(2)).doubleValue(), 0);
    assertEquals(35D, actual.get(LocalDate.now().plusDays(3)).doubleValue(), 0);
    assertEquals(4, actual.size());
  }
}