package ru.stqa.heroku.selenium;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Path("travis")
public class TravisServlet {

  private Storage db = Storage.getInstance();

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void doPost(@FormParam("payload") String payload) {
    JsonObject json = new JsonParser().parse(payload).getAsJsonObject();
    TravisBuild build = jsonToTravisBuild(json);
    List<TravisJob> jobs = jsonToTravisJobs(json);

    db.store(build);
    jobs.forEach(j -> db.store(j));
  }

  private TravisBuild jsonToTravisBuild(JsonObject json) {
    return TravisBuild.newBuilder()
      .setId(json.get("id").getAsString())
      .setNumber(json.get("number").getAsString())
      .setStatus(stringOrNull(json.get("status")))
      .setResult(stringOrNull(json.get("result")))
      .setStartedAt(instantOrNull(json.get("started_at")))
      .setFinishedAt(instantOrNull(json.get("finished_at")))
      .setBranch(stringOrNull(json.get("branch")))
      .setCommit(stringOrNull(json.get("commit")))
      .setCommitMessage(stringOrNull(json.get("message")))
      .setCommitAuthor(stringOrNull(json.get("author_name")))
      .setPullRequest(json.get("pull_request").getAsBoolean())
      .setPullRequestNumber(stringOrNull(json.get("pull_request_number")))
      .setPullRequestTitle(stringOrNull(json.get("pull_request_title")))
      .build();
  }

  private List<TravisJob> jsonToTravisJobs(JsonObject json) {
    return StreamSupport.stream(json.get("matrix").getAsJsonArray().spliterator(), false)
      .map(this::jsonToTravisJob).collect(Collectors.toList());
  }

  private TravisJob jsonToTravisJob(JsonElement jsonElement) {
    JsonObject json = jsonElement.getAsJsonObject();
    JsonObject config = json.get("config").getAsJsonObject();
    return TravisJob.newBuilder()
      .setBuildId(json.get("parent_id").getAsString())
      .setId(json.get("id").getAsString())
      .setNumber(json.get("number").getAsString())
      .setStatus(stringOrNull(json.get("status")))
      .setResult(stringOrNull(json.get("result")))
      .setStartedAt(instantOrNull(json.get("started_at")))
      .setFinishedAt(instantOrNull(json.get("finished_at")))
      .setOs(stringOrNull(config.get("os")))
      .setLanguage(getLanguage(config))
      .setEnv(getEnv(config))
      .setAllowFailure(json.get("allow_failure").getAsBoolean())
      .build();
  }

  private String stringOrNull(JsonElement json) {
    return json instanceof JsonNull ? null : json.getAsString();
  }

  private Instant instantOrNull(JsonElement json) {
    return json instanceof JsonNull ? null : Instant.parse(json.getAsString());
  }

  private String getLanguage(JsonObject config) {
    return stringOrNull(config.get("language"));
  }

  private String getEnv(JsonObject config) {
    return config.get("env").toString();
  }

}
