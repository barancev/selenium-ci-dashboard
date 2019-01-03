package ru.stqa.heroku.selenium;

import java.util.function.Function;

public interface Storage {
  static Storage getInstance() {
    return HibernateStorage.getInstance();
  }

  public <R> R inSession(Function<StorageSession, R> run);
}
