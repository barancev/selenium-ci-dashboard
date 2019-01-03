package ru.stqa.heroku.selenium;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HibernateSession implements StorageSession {

  private static Logger log = Logger.getLogger(HibernateStorage.class.getName());

  private Session session;

  public HibernateSession(Session session) {
    this.session = session;
  }

  @Override
  public Long store(TestRun testRun) {
    if (testRun.getId() != null) {
      List<TestRun> result = session.createQuery("from TestRun where id=:id", TestRun.class).setParameter("id", testRun.getId()).list();
      if (result.size() > 0) {
        session.update(result.get(0).updateFrom(testRun));
      } else {
        session.save(testRun);
      }
    } else {
      session.save(testRun);
    }
    return testRun.getId();
  }

  @Override
  public List<TravisBuild> getTravisBuilds() {
    return session.createQuery("from TravisBuild b order by b.id desc", TravisBuild.class).setMaxResults(40).list();
  }

  @Override
  public TravisBuild getTravisBuild(String buildId) {
    List<TravisBuild> builds = session.createQuery("from TravisBuild where id=:id", TravisBuild.class).setParameter("id", buildId).list();
    return builds.size() > 0 ? builds.get(0) : null;
  }

  @Override
  public TravisJob getTravisJob(String jobId) {
    return session.createQuery("from TravisJob where id=:id", TravisJob.class).setParameter("id", jobId).getSingleResult();
  }

  @Override
  public TravisJob populateJobHistory(TravisJob job) {
    job.setHistory(session.createQuery("from TravisJob j where j.env=:env order by j.id", TravisJob.class).setParameter("env", job.getEnv()).list());
    return job;
  }

  @Override
  public void updateBuilds() {
    Instant checkPoint = Instant.now().minusSeconds(15);
    List<TravisBuild> toCheck = session.createQuery("from TravisBuild where finishedAt=null or state='running'", TravisBuild.class).list()
      .stream().filter(build -> build.getCheckedAt().isBefore(checkPoint)).collect(Collectors.toList());

    if (toCheck.size() > 0) {
      beginTransaction();
      for (TravisBuild build : toCheck) {
        try {
          CloseableHttpClient httpClient = HttpClients.createDefault();
          HttpGet request = new HttpGet(String.format("https://api.travis-ci.org/build/%s?include=job.state,job.started_at,job.finished_at", build.getId()));
          request.setHeader("Travis-API-Version", "3");
          request.setHeader("User-Agent", "Selenium CI Dashboard");
          request.setHeader("Authorization", String.format("token %s", System.getenv("TRAVIS_TOKEN")));
          CloseableHttpResponse response = httpClient.execute(request);
          String body = EntityUtils.toString(response.getEntity());
          log.info(body);

          JsonObject json = new JsonParser().parse(body).getAsJsonObject();
          build.updateFrom(json);
          session.save(build);

          for (JsonElement jobElement : json.get("jobs").getAsJsonArray()) {
            JsonObject jobObject = jobElement.getAsJsonObject();
            TravisJob job = getTravisJob(jobObject.get("id").getAsString());
            job.updateFrom(jobObject);
            session.save(job);
          }
        } catch (IOException e) {
          log.log(Level.WARNING, e.getMessage(), e);
        }
      }
      commitTransaction();
    }
  }

  @Override
  public void save(TravisBuild build) {
    session.save(build);
  }

  @Override
  public void save(TravisJob travisJob) {
    session.save(travisJob);
  }

  @Override
  public void beginTransaction() {
    session.beginTransaction();
  }

  @Override
  public void commitTransaction() {
    session.getTransaction().commit();
  }
}
