package ru.stqa.selenium;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Path("travis")
public class TravisServlet {

  private static Logger log = Logger.getLogger("travis");

  private H2Storage h2 = H2Storage.getInstance();

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void doPost(@FormParam("payload") String payload) throws ClassNotFoundException, SQLException {
    JsonObject json = new JsonParser().parse(payload).getAsJsonObject();
    TravisBuild build = jsonToTravisBuild(json);
    List<TravisJob> jobs = jsonToTravisJobs(json);

    h2.store(build);
    jobs.forEach(j -> h2.store(j));
  }

  private TravisBuild jsonToTravisBuild(JsonObject json) {
    return TravisBuild.newBuilder()
      .setId(json.get("id").getAsString())
      .setNumber(json.get("number").getAsString())
      .setStatus(stringOrNull(json.get("status")))
      .setResult(stringOrNull(json.get("result")))
      .setStartedAt(stringOrNull(json.get("started_at")))
      .setFinishedAt(stringOrNull(json.get("finished_at")))
      .setBranch(stringOrNull(json.get("branch")))
      .setCommit(stringOrNull(json.get("commit")))
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
      .setStartedAt(stringOrNull(json.get("started_at")))
      .setFinishedAt(stringOrNull(json.get("finished_at")))
      .setOs(stringOrNull(config.get("os")))
      .setLanguage(getLanguage(config))
      .setEnv(getEnv(config))
      .setAllowFailure(json.get("allow_failure").getAsBoolean())
      .build();
  }

  private String stringOrNull(JsonElement json) {
    return json instanceof JsonNull ? null : json.getAsString();
  }

  private String getLanguage(JsonObject config) {
    return stringOrNull(config.get("language"));
  }

  private String getEnv(JsonObject config) {
    return config.get("env").toString();
  }

}
