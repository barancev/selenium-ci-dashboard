package ru.stqa.heroku.selenium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Logger;

import static ru.stqa.heroku.selenium.JsonUtils.*;

@Path("testrun")
public class TestRunServlet {

  private static Logger log = Logger.getLogger(TravisServlet.class.getName());

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public String doPost(String payload) throws IOException {
    log.info(payload);
    JsonNode json = new ObjectMapper().readTree(payload);
    return Storage.getInstance().inSession((session) -> {
      session.beginTransaction();
      TestRun test = jsonToTestRun(json);
      String jobId = stringOrNull(json.get("job_id"));
      test.setJob(jobId !=  null ? session.getTravisJob(jobId) : null);
      String result = session.store(test).toString();
      session.commitTransaction();
      return result;
    });
  }

  private TestRun jsonToTestRun(JsonNode json) {
    return TestRun.newBuilder()
      .setId(numberOrNull(json.get("id")))
      .setTestClass(stringOrNull(json.get("testclass")))
      .setTestCase(json.get("testcase").asText())
      .setResult(stringOrNull(json.get("result")))
      .setStartedAt(instantOrNull(json.get("started_at")))
      .setFinishedAt(instantOrNull(json.get("finished_at")))
      .setException(stringOrNull(json.get("exception")))
      .setMessage(stringOrNull(json.get("message")))
      .setStacktrace(stringOrNull(json.get("stacktrace")))
      .build();
  }
}
