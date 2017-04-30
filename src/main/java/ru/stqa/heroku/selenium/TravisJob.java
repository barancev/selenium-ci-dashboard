package ru.stqa.heroku.selenium;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Entity
@Table(name = "jobs")
public class TravisJob {

  @Id
  private String id;
  private String number;
  private String state;
  private Instant startedAt;
  private Instant finishedAt;
  private String os;
  private String language;
  private String env;
  private boolean allowFailure;

  @ManyToOne(fetch = FetchType.LAZY)
  private TravisBuild build;

  @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
  private List<TestRun> testRuns = new ArrayList<>();

  private TravisJob() {}

  TravisJob updateFrom(JsonObject json) {
    if (json.get("state") != null) {
      this.state = json.get("state").getAsString();
    } else {
      if (json.get("status").getAsInt() == 0) {
        this.state = "passed";
      } else {
        this.state = "failed";
        // when this.state = cancelled ?
      }
    }
    this.startedAt = instantOrNull(json.get("started_at"));
    this.finishedAt = instantOrNull(json.get("finished_at"));
    return this;
  }

  private String stringOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : json.getAsString();
  }

  private Instant instantOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : Instant.parse(json.getAsString());
  }

  public TravisBuild getBuild() {
    return build;
  }

  private void setBuild(TravisBuild build) {
    this.build = build;
  }

  public String getId() {
    return id;
  }

  private void setId(String id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  private void setNumber(String number) {
    this.number = number;
  }

  public String getState() {
    return state;
  }

  void setState(String status) {
    this.state = state;
  }

  public Instant getStartedAt() {
    return startedAt;
  }

  private void setStartedAt(Instant startedAt) {
    this.startedAt = startedAt;
  }

  public Instant getFinishedAt() {
    return finishedAt;
  }

  void setFinishedAt(Instant finishedAt) {
    this.finishedAt = finishedAt;
  }

  public String getOs() {
    return os;
  }

  private void setOs(String os) {
    this.os = os;
  }

  public String getLanguage() {
    return language;
  }

  private void setLanguage(String language) {
    this.language = language;
  }

  public String getEnv() {
    return env;
  }

  private void setEnv(String env) {
    this.env = env;
  }

  public boolean isAllowFailure() {
    return allowFailure;
  }

  private void setAllowFailure(boolean allowFailure) {
    this.allowFailure = allowFailure;
  }

  public List<TestRun> getTestRuns() {
    return testRuns;
  }

  public Map<String, Object> toMinJsonMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", getId());
    map.put("number", getNumber());
    if (getStartedAt() != null) {
      map.put("started", getStartedAt());
      if (getFinishedAt() != null) {
        map.put("finished", getFinishedAt());
        map.put("duration", Duration.between(getStartedAt(), getFinishedAt()));
        map.put("state", state);
      } else {
        map.put("finished", "-");
        map.put("duration", Duration.between(getStartedAt(), Instant.now()));
        map.put("state", "running");
        //map.put("state", "cancelled");
      }
    } else {
      map.put("started", "-");
      map.put("finished", "-");
      map.put("duration", "-");
      map.put("state", "pending");
    }
    map.put("os", getOs());
    map.put("language", getLanguage());
    map.put("env", getEnv());
    return map;
  }

  public Map<String, Object> toFullJsonMap() {
    Map<String, Object> map = toMinJsonMap();
    map.put("build", build.toFullJsonMap());
    Map<String, TestClass> testClasses = new HashMap<>();
    for (TestRun testRun : testRuns) {
      TestClass testClass = testClasses.computeIfAbsent(testRun.getTestClass(), k -> new TestClass(testRun.getTestClass()));
      testClass.addTestCase(testRun);
    }
    List<TestClass> list = Lists.newArrayList(testClasses.values());
    list.sort(Comparator.comparing(TestClass::getName));
    map.put("testClasses", list.stream().map(TestClass::toMinJsonMap).collect(Collectors.toList()));
    return map;
  }

  public Map<String, Object> toFullJsonMap(String testClassName) {
    Map<String, Object> map = toMinJsonMap();
    map.put("build", build.toFullJsonMap());
    TestClass testClass = new TestClass(testClassName);
    for (TestRun testRun : testRuns) {
      if (testRun.getTestClass().equals(testClassName)) {
        testClass.addTestCase(testRun);
      }
    }
    map.put("testClass", testClass.toFullJsonMap());
    return map;
  }

  public Map<String, Object> toFullJsonMap(String testClassName, String testCaseName) {
    Map<String, Object> map = toMinJsonMap();
    map.put("build", build.toFullJsonMap());
    TestClass testClass = new TestClass(testClassName);
    for (TestRun testRun : testRuns) {
      if (testRun.getTestClass().equals(testClassName) && testRun.getTestCase().equals(testCaseName)) {
        testClass.addTestCase(testRun);
      }
    }
    map.put("testClass", testClass.toFullJsonMap());
    return map;
  }

  public static Builder newBuilder() {
    return new TravisJob().new Builder();
  }

  public class Builder {

    private Builder() {
    }

    public Builder setBuild(TravisBuild build) {
      TravisJob.this.build = build;
      return this;
    }

    public Builder setId(String id) {
      TravisJob.this.id = id;
      return this;
    }

    public Builder setNumber(String number) {
      TravisJob.this.number = number;
      return this;
    }

    public Builder setState(String state) {
      TravisJob.this.state = state;
      return this;
    }

    public Builder setStartedAt(Instant startedAt) {
      TravisJob.this.startedAt = startedAt;
      return this;
    }

    public Builder setFinishedAt(Instant finishedAt) {
      TravisJob.this.finishedAt = finishedAt;
      return this;
    }

    public Builder setOs(String os) {
      TravisJob.this.os = os;
      return this;
    }

    public Builder setLanguage(String language) {
      TravisJob.this.language = language;
      return this;
    }

    public Builder setEnv(String env) {
      TravisJob.this.env = env;
      return this;
    }

    public Builder setAllowFailure(boolean allowFailure) {
      TravisJob.this.allowFailure = allowFailure;
      return this;
    }

    public TravisJob build() {
      return TravisJob.this;
    }
  }

}
