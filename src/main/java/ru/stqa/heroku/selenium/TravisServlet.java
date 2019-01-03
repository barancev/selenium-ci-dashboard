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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Path("travis")
public class TravisServlet {

  private static Logger log = Logger.getLogger(TravisServlet.class.getName());

  private HibernateStorage db = HibernateStorage.getInstance();

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void doPost(@FormParam("payload") String payload) {
    log.info(payload);
    JsonObject json = new JsonParser().parse(payload).getAsJsonObject();
    if (json.get("pull_request").getAsBoolean()) {
      // don't track pull requests
      return;
    }
    String buildId = json.get("id").getAsString();
    db.inSession((session) -> {
      TravisBuild build = session.getTravisBuild(buildId);
      session.beginTransaction();
      if (build == null) {
        build = jsonToTravisBuild(json);
        session.save(build);
        List<TravisJob> jobs = jsonToTravisJobs(json, build);
        jobs.forEach(session::save);

      } else {
        session.save(build.updateFrom(json));
        Map<String, JsonObject> jobObjects = getJobObjects(json);
        build.getJobs().forEach(job -> job.updateFrom(jobObjects.get(job.getId())));
      }
      session.commitTransaction();
      return null;
    });
  }

  private Map<String,JsonObject> getJobObjects(JsonObject json) {
    Map<String,JsonObject> map = new HashMap<>();
    for (JsonElement jobElement : json.get("matrix").getAsJsonArray()) {
      JsonObject jobObject = jobElement.getAsJsonObject();
      map.put(jobObject.get("id").getAsString(), jobObject);
    }
    return map;
  }

  private TravisBuild jsonToTravisBuild(JsonObject json) {
    return TravisBuild.newBuilder()
      .setId(json.get("id").getAsString())
      .setNumber(json.get("number").getAsString())
      .setState(stringOrNull(json.get("status")))
      .setStartedAt(instantOrNull(json.get("started_at")))
      .setFinishedAt(instantOrNull(json.get("finished_at")))
      .setBranch(stringOrNull(json.get("branch")))
      .setCommit(stringOrNull(json.get("commit")))
      .setCommitMessage(stringOrNull(json.get("message")))
      .setCommitAuthor(stringOrNull(json.get("author_name")))
      .build();
  }

  private List<TravisJob> jsonToTravisJobs(JsonObject json, TravisBuild build) {
    return StreamSupport.stream(json.get("matrix").getAsJsonArray().spliterator(), false)
      .map((job) -> jsonToTravisJob(job, build)).collect(Collectors.toList());
  }

  private TravisJob jsonToTravisJob(JsonElement jsonElement, TravisBuild build) {
    JsonObject json = jsonElement.getAsJsonObject();
    JsonObject config = json.get("config").getAsJsonObject();
    return TravisJob.newBuilder()
      .setBuild(build)
      .setId(json.get("id").getAsString())
      .setNumber(json.get("number").getAsString())
      .setState(stringOrNull(json.get("status")))
      .setStartedAt(instantOrNull(json.get("started_at")))
      .setFinishedAt(instantOrNull(json.get("finished_at")))
      .setOs(stringOrNull(config.get("os")))
      .setLanguage(stringOrNull(config.get("language")))
      .setEnv(stringOrNull(config.get("env")))
      .setAllowFailure(json.get("allow_failure").getAsBoolean())
      .build();
  }

  private String stringOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : json.getAsString();
  }

  private Instant instantOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : Instant.parse(json.getAsString());
  }

}
