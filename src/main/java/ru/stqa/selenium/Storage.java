package ru.stqa.selenium;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Storage {

  private static Storage singleton;

  public static Storage getInstance() {
    if (singleton == null) {
      singleton = new Storage();
    }
    return singleton;
  }

  private SessionFactory sessionFactory;

  private Storage() {
    sessionFactory = new Configuration().configure().buildSessionFactory();
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
}
