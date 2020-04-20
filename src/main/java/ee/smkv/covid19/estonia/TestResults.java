package ee.smkv.covid19.estonia;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestResults {
  private final List<TestResult> results;

  public TestResults(TestResult... results) {
    this.results = Arrays.asList(results);
  }

  public List<TestResult> getResults() {
    return results;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TestResults that = (TestResults)o;
    return Objects.equals(results, that.results);
  }

  @Override
  public int hashCode() {
    return Objects.hash(results);
  }
}
