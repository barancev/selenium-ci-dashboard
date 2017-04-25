package ru.stqa.heroku.selenium;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public class Storage {

  private static Logger log = Logger.getLogger("db");

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

  public void store(TravisBuild build) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      List<TravisBuild> result = session.createQuery("from TravisBuild where id=:id", TravisBuild.class).setParameter("id", build.getId()).list();
      if (result.size() > 0) {
        session.update(result.get(0).updateFrom(build));
      } else {
        session.save(build);
      }
      session.getTransaction().commit();
    }
  }

  public void store(TravisJob job) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      List<TravisJob> result = session.createQuery("from TravisJob where id=:id", TravisJob.class).setParameter("id", job.getId()).list();
      if (result.size() > 0) {
        session.update(result.get(0).updateFrom(job));
      } else {
        session.save(job);
      }
      session.getTransaction().commit();
    }
  }

  public Long store(TestRun testRun) {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
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
      session.getTransaction().commit();
      return testRun.getId();
    }
  }

  public List<TravisBuild> getTravisBuilds() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("from TravisBuild", TravisBuild.class).list();
    }
  }

  public TravisBuild getTravisBuild(String buildId) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("from TravisBuild where id=:id", TravisBuild.class).setParameter("id", buildId).getSingleResult();
    }
  }

  public List<TravisJob> getTravisJobs(String buildId) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("from TravisJob where buildId=:buildId", TravisJob.class).setParameter("buildId", buildId).list();
    }
  }

  public TravisJob getTravisJob(String jobId) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("from TravisJob where id=:id", TravisJob.class).setParameter("id", jobId).getSingleResult();
    }
  }

  public List<TestRun> getTestRuns(String jobId) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("from TestRun where jobId=:jobId", TestRun.class).setParameter("jobId", jobId).list();
    }
  }

}
