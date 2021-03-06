package ru.stqa.heroku.selenium;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Storage {
  Map<String, Supplier<Storage>> storageSuppliers = ImmutableMap.of(
    "hibernate", HibernateStorage::getInstance,
    "fauna", FaunaStorage::getInstance
    //"inmemory", () -> InMemoryStorage.getInstance()
  );
  static Storage getInstance() {
    String storage = System.getenv("STORAGE");
    if (storage == null || storage.isEmpty()) {
      storage = "hibernate";
    }
    return storageSuppliers.get(storage).get();
  }

  <R> R inSession(Function<StorageSession, R> run);
}
