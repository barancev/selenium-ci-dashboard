package ru.stqa.heroku.selenium;

import java.util.List;

public interface StorageSession {
  Long store(TestRun testRun);
  List<TravisBuild> getTravisBuilds();
  TravisBuild getTravisBuild(String buildId);
  TravisJob getTravisJob(String jobId);
  TravisJob populateJobHistory(TravisJob job);
  void updateBuilds();
  void save(TravisBuild build);
  void save(TravisJob travisJob);
  void beginTransaction();
  void commitTransaction();
}
