package ee.smkv.covid19.estonia;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DigiliguOpenDataProviderTest {

  private DigiliguOpenDataProvider provider = new DigiliguOpenDataProvider();

  @Test
  public void getCovid19TestResultsOffline() throws IOException {
    List<TestResult> results = provider.getCovid19TestResultsOffline(new File("src/test/resources/test.json"));

    List<TestResult> expected = Arrays.asList(
      new TestResult("95013b64dd5ff18548a92eb5375d9c4a1881467390fed4a1c084253ef72be9ea",
                     "M",
                     "10-14",
                     "Eesti",
                     "Tartu maakond",
                     "N",
                     LocalDate.parse("2020-03-10"),
                     LocalDateTime.parse("2020-03-06T18:44:00+02:00", DateTimeFormatter.ISO_DATE_TIME),
                     LocalDateTime.parse("2020-03-10T16:01:55+02:00", DateTimeFormatter.ISO_DATE_TIME)
      ),
      new TestResult("71fab95aa66a3976b9d9f2868482192fc2bb77ac07d6808b1b19079e045fe6de",
                     "M",
                     "5-9",
                     "Eesti",
                     "Tartu maakond",
                     "P",
                     LocalDate.parse("2020-03-10"),
                     LocalDateTime.parse("2020-03-06T13:28:00+02:00", DateTimeFormatter.ISO_DATE_TIME),
                     LocalDateTime.parse("2020-03-10T16:05:53+02:00", DateTimeFormatter.ISO_DATE_TIME)
      ),
      new TestResult("e474cb8d21136013c9c90877592ee8d6b20d1bd72ef48a7a916030211f15833f",
                     "M",
                     "20-24",
                     "Eesti",
                     "Harju maakond",
                     "N",
                     LocalDate.parse("2020-03-10"),
                     LocalDateTime.parse("2020-03-05T00:00:00+02:00", DateTimeFormatter.ISO_DATE_TIME),
                     LocalDateTime.parse("2020-03-10T15:53:52+02:00", DateTimeFormatter.ISO_DATE_TIME)
      ));

    assertEquals(expected, results);
  }
}