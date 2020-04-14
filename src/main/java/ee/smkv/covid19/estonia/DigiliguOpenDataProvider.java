package ee.smkv.covid19.estonia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

public class DigiliguOpenDataProvider {

  public static final URI COVID19_TEST_RESULTS_URI = URI.create("https://opendata.digilugu.ee/opendata_covid19_test_results.json");
  private final Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
    .create();

  public List<Data> getCovid19TestResults() throws IOException {
    try (Reader in = new InputStreamReader(COVID19_TEST_RESULTS_URI.toURL().openStream())) {
      final Data[] data = gson.fromJson(in, Data[].class);
      return Arrays.asList(data);
    }
  }
}
