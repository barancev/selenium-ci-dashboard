package ru.stqa.heroku.selenium;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Storage {

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

  public Session createSession() {
    return sessionFactory.openSession();
  }

  public void store(TravisBuild build, Session session) {
    List<TravisBuild> result = session.createQuery("from TravisBuild where id=:id", TravisBuild.class).setParameter("id", build.getId()).list();
    if (result.size() > 0) {
      session.update(result.get(0).updateFrom(build));
    } else {
      session.save(build);
    }
  }

  public void store(TravisJob job, Session session) {
    List<TravisJob> result = session.createQuery("from TravisJob where id=:id", TravisJob.class).setParameter("id", job.getId()).list();
    if (result.size() > 0) {
      session.update(result.get(0).updateFrom(job));
    } else {
      session.save(job);
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
    return session.createQuery("from TravisBuild", TravisBuild.class).list();
  }

  public TravisBuild getTravisBuild(String buildId, Session session) {
    return session.createQuery("from TravisBuild where id=:id", TravisBuild.class).setParameter("id", buildId).getSingleResult();
  }

  public List<TravisJob> getTravisJobs(String buildId, Session session) {
    TravisBuild build = session.createQuery("from TravisBuild where id=:id", TravisBuild.class).setParameter("id", buildId).getSingleResult();
    return build.getJobs();
  }

  public TravisJob getTravisJob(String jobId, Session session) {
    return session.createQuery("from TravisJob where id=:id", TravisJob.class).setParameter("id", jobId).getSingleResult();
  }

}
