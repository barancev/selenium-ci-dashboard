package ru.stqa.heroku.selenium;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Storage {

  private static Logger log = Logger.getLogger(Storage.class.getName());

  private static Storage singleton;

  public synchronized static Storage getInstance() {
    if (singleton == null) {
      singleton = new Storage();
    }
    return singleton;
  }

  private SessionFactory sessionFactory;

  private Storage() {
    Configuration configuration = new Configuration().configure();
    String dbUrl = System.getenv("DATABASE_URL");
    if (dbUrl != null) {
      URI dbUri;
      try {
        dbUri = new URI(dbUrl);
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }

      String username = dbUri.getUserInfo().split(":")[0];
      String password = dbUri.getUserInfo().split(":")[1];
      dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
      configuration.setProperty("hibernate.connection.url", dbUrl);
      configuration.setProperty("hibernate.connection.username", username);
      configuration.setProperty("hibernate.connection.password", password);
    }
    sessionFactory = configuration.buildSessionFactory();
  }

  public <R> R inSession(Function<Session, R> run) {
    try (Session session = sessionFactory.openSession()) {
      return run.apply(session);
    }
  }

  public Long store(TestRun testRun, Session session) {
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

  public List<TravisBuild> getTravisBuilds(Session session) {
    return session.createQuery("from TravisBuild b order by b.id desc", TravisBuild.class).setMaxResults(40).list();
  }

  public TravisBuild getTravisBuild(Session session, String buildId) {
    List<TravisBuild> builds = session.createQuery("from TravisBuild where id=:id", TravisBuild.class).setParameter("id", buildId).list();
    return builds.size() > 0 ? builds.get(0) : null;
  }

  public TravisJob getTravisJob(Session session, String jobId) {
    return session.createQuery("from TravisJob where id=:id", TravisJob.class).setParameter("id", jobId).getSingleResult();
  }

  public TravisJob populateJobHistory(Session session, TravisJob job) {
    job.setHistory(session.createQuery("from TravisJob j where j.env=:env order by j.id", TravisJob.class).setParameter("env", job.getEnv()).list());
    return job;
  }

  public void updateBuilds(Session session) {
    Instant checkPoint = Instant.now().minusSeconds(15);
    List<TravisBuild> toCheck = session.createQuery("from TravisBuild where finishedAt=null or state='running'", TravisBuild.class).list()
      .stream().filter(build -> build.getCheckedAt().isBefore(checkPoint)).collect(Collectors.toList());

    if (toCheck.size() > 0) {
      session.beginTransaction();
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
            TravisJob job = getTravisJob(session, jobObject.get("id").getAsString());
            job.updateFrom(jobObject);
            session.save(job);
          }
        } catch (IOException e) {
          log.log(Level.WARNING, e.getMessage(), e);
        }
      }
      session.getTransaction().commit();
    }
  }

  private String stringOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : json.getAsString();
  }

  private Instant instantOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : Instant.parse(json.getAsString());
  }

}
