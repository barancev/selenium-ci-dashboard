package ru.stqa.heroku.selenium;

import com.faunadb.client.FaunaClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

public class FaunaStorage implements Storage {

  private static FaunaStorage singleton;

  public synchronized static FaunaStorage getInstance() {
    if (singleton == null) {
      singleton = new FaunaStorage();
    }
    return singleton;
  }

  private FaunaClient client;

  private FaunaStorage() {
    client = FaunaClient.builder().withSecret(System.getenv("FAUNADB_SECRET")).build();
  }

  @Override
  public <R> R inSession(Function<StorageSession, R> run) {
    return run.apply(new FaunaSession(client));
  }
}
