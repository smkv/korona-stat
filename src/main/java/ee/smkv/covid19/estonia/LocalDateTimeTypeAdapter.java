package ee.smkv.covid19.estonia;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
@Override
public void write(JsonWriter out, LocalDateTime value) throws IOException {
  out.jsonValue(value.toString());
}

@Override
public LocalDateTime read(JsonReader in) throws IOException {
  return LocalDateTime.parse(in.nextString(), DateTimeFormatter.ISO_DATE_TIME);
}
}
