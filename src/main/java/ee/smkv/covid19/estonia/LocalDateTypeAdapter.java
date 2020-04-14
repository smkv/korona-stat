package ee.smkv.covid19.estonia;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
  @Override
  public void write(JsonWriter out, LocalDate value) throws IOException {
    out.jsonValue(value.toString());
  }

  @Override
  public LocalDate read(JsonReader in) throws IOException {
    return LocalDate.parse(in.nextString());
  }
}
