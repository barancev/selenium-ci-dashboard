package ru.stqa.heroku.selenium;

import com.faunadb.client.FaunaClient;

import java.util.List;
import java.util.logging.Logger;

import static com.faunadb.client.query.Language.*;

public class FaunaSession implements StorageSession {

  private static Logger log = Logger.getLogger(HibernateStorage.class.getName());

  private FaunaClient client;

  public FaunaSession(FaunaClient client) {
    this.client = client;
  }

  @Override
  public Long store(TestRun testRun) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<TravisBuild> getTravisBuilds() {
    throw new UnsupportedOperationException();
  }

  @Override
  public TravisBuild getTravisBuild(String buildId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TravisJob getTravisJob(String jobId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TravisJob populateJobHistory(TravisJob job) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBuilds() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void save(TravisBuild build) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void save(TravisJob travisJob) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void beginTransaction() {
  }

  @Override
  public void commitTransaction() {
  }
}
