import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Provider {

    public static final URI DATA_URI = java.net.URI.create("https://opendata.digilugu.ee/opendata_covid19_test_results.json");
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    public List<Data> getData() throws IOException {
        try (Reader in = new InputStreamReader(DATA_URI.toURL().openStream())) {
            final Data[] data = gson.fromJson(in, Data[].class);
            return Arrays.asList(data);
        }
    }

    public Map<LocalDate, Long> getPositiveStatisticsByDays() throws IOException {
        return getData()
                .stream()
                .filter(Data::isPositive)
                .sorted(comparing(Data::getStatisticsDate))
                .collect(groupingBy(Data::getStatisticsDate, TreeMap::new, counting()));
    }

    private static class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            out.jsonValue(value.toString());
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString());
        }
    }

    private static class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.jsonValue(value.toString());
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            return LocalDateTime.parse(in.nextString(), DateTimeFormatter.ISO_DATE_TIME);
        }
    }
}
