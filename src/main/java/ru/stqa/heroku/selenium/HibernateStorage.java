package ru.stqa.heroku.selenium;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

public class HibernateStorage implements Storage {

  private static HibernateStorage singleton;

  public synchronized static HibernateStorage getInstance() {
    if (singleton == null) {
      singleton = new HibernateStorage();
    }
    return singleton;
  }

  private SessionFactory sessionFactory;

  private HibernateStorage() {
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

  public <R> R inSession(Function<StorageSession, R> run) {
    try (Session session = sessionFactory.openSession()) {
      return run.apply(new HibernateSession(session));
    }
  }

}
