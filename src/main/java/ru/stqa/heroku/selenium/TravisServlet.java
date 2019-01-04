package ru.stqa.heroku.selenium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.stqa.heroku.selenium.JsonUtils.stringOrNull;
import static ru.stqa.heroku.selenium.JsonUtils.instantOrNull;

@Path("travis")
public class TravisServlet {

  private static Logger log = Logger.getLogger(TravisServlet.class.getName());

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void doPost(@FormParam("payload") String payload) throws IOException {
    log.info(payload);
    JsonNode json = new ObjectMapper().readTree(payload);
    if (json.get("pull_request").asBoolean()) {
      // don't track pull requests
      return;
    }
    String buildId = json.get("id").asText();
    Storage.getInstance().inSession((session) -> {
      TravisBuild build = session.getTravisBuild(buildId);
      session.beginTransaction();
      if (build == null) {
        build = jsonToTravisBuild(json);
        session.save(build);
        List<TravisJob> jobs = jsonToTravisJobs(json, build);
        jobs.forEach(session::save);

      } else {
        session.save(build.updateFrom(json));
        Map<String, JsonNode> jobObjects = getJobObjects(json);
        build.getJobs().forEach(job -> job.updateFrom(jobObjects.get(job.getId())));
      }
      session.commitTransaction();
      return null;
    });
  }

  private Map<String,JsonNode> getJobObjects(JsonNode json) {
    Map<String,JsonNode> map = new HashMap<>();
    StreamSupport.stream(json.get("matrix").spliterator(), false)
      .forEach(jobObject -> map.put(jobObject.get("id").asText(), jobObject));
    return map;
  }

  private TravisBuild jsonToTravisBuild(JsonNode json) {
    return TravisBuild.newBuilder()
      .setId(json.get("id").asText())
      .setNumber(json.get("number").asText())
      .setState(stringOrNull(json.get("status")))
      .setStartedAt(instantOrNull(json.get("started_at")))
      .setFinishedAt(instantOrNull(json.get("finished_at")))
      .setBranch(stringOrNull(json.get("branch")))
      .setCommit(stringOrNull(json.get("commit")))
      .setCommitMessage(stringOrNull(json.get("message")))
      .setCommitAuthor(stringOrNull(json.get("author_name")))
      .build();
  }

  private List<TravisJob> jsonToTravisJobs(JsonNode json, TravisBuild build) {
    return StreamSupport.stream(json.get("matrix").spliterator(), false)
      .map((job) -> jsonToTravisJob(job, build)).collect(Collectors.toList());
  }

  private TravisJob jsonToTravisJob(JsonNode json, TravisBuild build) {
    JsonNode config = json.get("config");
    return TravisJob.newBuilder()
      .setBuild(build)
      .setId(json.get("id").asText())
      .setNumber(json.get("number").asText())
      .setState(stringOrNull(json.get("status")))
      .setStartedAt(instantOrNull(json.get("started_at")))
      .setFinishedAt(instantOrNull(json.get("finished_at")))
      .setOs(stringOrNull(config.get("os")))
      .setLanguage(stringOrNull(config.get("language")))
      .setEnv(stringOrNull(config.get("env")))
      .setAllowFailure(json.get("allow_failure").asBoolean())
      .build();
  }
}
