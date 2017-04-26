package ru.stqa.heroku.selenium;

import com.google.gson.*;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class ServletBase {

  public static class InstantSerializer implements JsonSerializer<Instant> {
    @Override
    public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
      return new JsonPrimitive(
        DateTimeFormatter.ofPattern("MMM dd, HH:mm:ss").withLocale(Locale.UK).withZone(ZoneId.of("UTC")).format(instant));
    }
  }

  public static class DurationSerializer implements JsonSerializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext jsonSerializationContext) {
      return new JsonPrimitive(formatDuration(duration));
    }

    private static String formatDuration(Duration duration) {
      long seconds = duration.getSeconds();
      long absSeconds = Math.abs(seconds);
      String positive = String.format(
        "%d:%02d:%02d",
        absSeconds / 3600,
        (absSeconds % 3600) / 60,
        absSeconds % 60);
      return seconds < 0 ? "-" + positive : positive;
    }
  }

  protected Gson gson() {
    GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC);
    gsonBuilder.registerTypeAdapter(Instant.class, new InstantSerializer());
    gsonBuilder.registerTypeAdapter(Duration.class, new DurationSerializer());
    return gsonBuilder.create();
  }
}
