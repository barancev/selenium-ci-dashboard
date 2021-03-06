package ru.stqa.heroku.selenium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ru.stqa.heroku.selenium.JsonUtils.instantOrNull;

@Entity
@Table(name = "jobs")
public class TravisJob {

  private static Logger log = Logger.getLogger(TravisJob.class.getName());

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

  @Transient
  private List<TravisJob> history;

  private TravisJob() {}

  TravisJob updateFrom(JsonNode json) {
    if (json.get("status") !=  null) {
      if (json.get("status") instanceof NullNode) {
        this.state = "running";
      } else {
        if (json.get("status").asInt() == 0) {
          this.state = "passed";
        } else {
          this.state = "failed";
          // when this.state = cancelled ?
        }
      }
    } else {
      this.state = json.get("state").asText();
    }
    this.startedAt = instantOrNull(json.get("started_at"));
    this.finishedAt = instantOrNull(json.get("finished_at"));
    return this;
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
        map.put("duration_in_seconds", Duration.between(getStartedAt(), getFinishedAt()).getSeconds());
        map.put("state", state);
      } else {
        map.put("finished", "-");
        map.put("duration", Duration.between(getStartedAt(), Instant.now()));
        map.put("duration_in_seconds", Duration.between(getStartedAt(), Instant.now()).getSeconds());
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

  public Map<String, Object> toHistoryJsonMap() {
    Map<String, Object> map = toMinJsonMap();
    int passed = 0;
    int failed = 0;
    int skipped = 0;
    for (TestRun testCase : testRuns) {
      if (testCase.getResult() != null) {
        switch (testCase.getResult()) {
          case "passed":
            passed++;
            break;
          case "failed":
            failed++;
            break;
          case "skipped":
            skipped++;
            break;
          default:
            log.info("Unknown test case result " + testCase.getResult());
        }
      }
    }
    map.put("passed", passed);
    map.put("failed", failed);
    map.put("skipped", skipped);
    map.put("total", passed + failed + skipped);
    return map;
  }

  public Map<String, Object> toFullJsonMap() {
    Map<String, Object> map = toMinJsonMap();
    map.put("build", build.toFullJsonMap());
    TestRun current = null;
    Map<String, TestClass> testClasses = new HashMap<>();
    for (TestRun testRun : testRuns) {
      if (testRun.getFinishedAt() == null) {
        current = testRun;
      }
      TestClass testClass = testClasses.computeIfAbsent(testRun.getTestClass(), k -> new TestClass(testRun.getTestClass()));
      testClass.addTestCase(testRun);
    }
    List<TestClass> list = new ArrayList<>(testClasses.values());
    list.sort(Comparator.comparing(TestClass::getName));
    map.put("testClasses", list.stream().map(TestClass::toMinJsonMap).collect(Collectors.toList()));
    if (current != null) {
      map.put("current", current.toJsonMap());
    }
    map.put("history", history.stream().map(TravisJob::toHistoryJsonMap).collect(Collectors.toList()));
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

  public void setHistory(List<TravisJob> history) {
    this.history = history;
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
