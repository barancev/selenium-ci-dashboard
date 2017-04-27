package ru.stqa.heroku.selenium;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Path("testrun")
public class TestRunServlet {

  private Storage db = Storage.getInstance();

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public String doPost(String payload) {
    JsonObject json = new JsonParser().parse(payload).getAsJsonObject();
    return db.store(jsonToTestRun(json)).toString();
  }

  private TestRun jsonToTestRun(JsonObject json) {
    return TestRun.newBuilder()
      .setId(numberOrNull(json.get("id")))
      .setJobId(stringOrNull(json.get("job_id")))
      .setTestClass(stringOrNull(json.get("testclass")))
      .setTestCase(json.get("testcase").getAsString())
      .setResult(stringOrNull(json.get("result")))
      .setStartedAt(instantOrNull(json.get("started_at")))
      .setFinishedAt(instantOrNull(json.get("finished_at")))
      .setException(stringOrNull(json.get("exception")))
      .setMessage(stringOrNull(json.get("message")))
      .setStacktrace(stringOrNull(json.get("stacktrace")))
      .build();
  }

  private String stringOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : json.getAsString();
  }

  private Long numberOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : json.getAsLong();
  }

  private Instant instantOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : Instant.ofEpochMilli(json.getAsLong());
  }

}
