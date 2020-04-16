package ee.smkv.covid19.estonia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DigiliguOpenDataProvider {

  public static final URI COVID19_TEST_RESULTS_URI = URI.create("https://opendata.digilugu.ee/opendata_covid19_test_results.json");
  private final Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter().nullSafe())
    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter().nullSafe())
    .create();

  public List<TestResult> getCovid19TestResultsOnline() throws IOException {
    try (Reader reader = new InputStreamReader(COVID19_TEST_RESULTS_URI.toURL().openStream())) {
      return getTestResults(reader);
    }
  }

  public List<TestResult> getCovid19TestResultsOffline(File file) throws IOException {
    try (Reader reader = new FileReader(file)) {
      return getTestResults(reader);
    }
  }

  private List<TestResult> getTestResults(Reader reader) {
    final TestResult[] results = gson.fromJson(reader, TestResult[].class);
    return Arrays.asList(results);
  }
}
