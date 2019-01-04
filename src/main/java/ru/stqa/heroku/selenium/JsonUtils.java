package ru.stqa.heroku.selenium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;

import java.time.Instant;

public class JsonUtils {

  public static String stringOrNull(JsonNode json) {
    return json == null || json instanceof NullNode ? null : json.asText();
  }

  public static Instant instantOrNull(JsonNode json) {
    return json == null || json instanceof NullNode ? null : Instant.ofEpochMilli(json.asLong());
  }

  public static Long numberOrNull(JsonNode json) {
    return json == null || json instanceof NumericNode ? null : json.asLong();
  }

}
