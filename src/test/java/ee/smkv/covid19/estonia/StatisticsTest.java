package ee.smkv.covid19.estonia;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class StatisticsTest {

  @Test
  public void getPositiveByDays() {
    Statistics statistics = new Statistics(Arrays.asList(
      resultOf(LocalDate.parse("2020-04-01"), true),
      resultOf(LocalDate.parse("2020-04-01"), false),
      resultOf(LocalDate.parse("2020-04-01"), true),
      resultOf(LocalDate.parse("2020-04-02"), true),
      resultOf(LocalDate.parse("2020-04-02"), true),
      resultOf(LocalDate.parse("2020-04-03"), false),
      resultOf(LocalDate.parse("2020-04-03"), false),
      resultOf(LocalDate.parse("2020-04-04"), true)
    ));
    TreeMap<LocalDate, Long> expected = new TreeMap<>();
    expected.put(LocalDate.parse("2020-04-01"), 2L);
    expected.put(LocalDate.parse("2020-04-02"), 2L);
    expected.put(LocalDate.parse("2020-04-03"), 0L);
    expected.put(LocalDate.parse("2020-04-04"), 1L);
    StatisticsByDate actual = statistics.getPositiveByDays();
    assertEquals(new StatisticsByDate(expected), actual);
  }

  @Test
  public void testGetPositiveByDays() {
  }

  private TestResult resultOf(LocalDate statisticsDate, boolean positive) {
    TestResult result = new TestResult();
    result.setStatisticsDate(statisticsDate);
    result.setResultValue(positive ? "P" : "N");
    return result;
  }
}