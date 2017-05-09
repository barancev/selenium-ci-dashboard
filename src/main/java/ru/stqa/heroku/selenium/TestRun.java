package ru.stqa.heroku.selenium;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "tests")
public class TestRun {

  @Id
  @GeneratedValue
  private Long id;
  private String testClass;
  private String testCase;
  private String result;
  private Instant startedAt;
  private Instant finishedAt;
  private String exception;
  @Type(type="text")
  private String message;
  @Type(type="text")
  private String stacktrace;

  @ManyToOne(fetch = FetchType.LAZY)
  private TravisJob job;

  TestRun() {}

  TestRun updateFrom(TestRun other) {
    this.id = other.id;
    this.testClass = other.testClass;
    this.testCase = other.testCase;
    this.result = other.result;
    this.startedAt = other.startedAt;
    this.finishedAt = other.finishedAt;
    this.exception = other.exception;
    this.message = other.message;
    this.stacktrace = other.stacktrace;
    return this;
  }

  public Long getId() {
    return id;
  }

  private void setId(Long id) {
    this.id = id;
  }

  public String getTestClass() {
    return testClass;
  }

  private void setTestClass(String testClass) {
    this.testClass = testClass;
  }

  public String getTestCase() {
    return testCase;
  }

  private void setTestCase(String testCase) {
    this.testCase = testCase;
  }

  public TravisJob getJob() {
    return job;
  }

  public void setJob(TravisJob job) {
    this.job = job;
  }

  public String getResult() {
    return result;
  }

  private void setResult(String result) {
    this.result = result;
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

  private void setFinishedAt(Instant finishedAt) {
    this.finishedAt = finishedAt;
  }

  public String getException() {
    return exception;
  }

  private void setException(String exception) {
    this.exception = exception;
  }

  public String getMessage() {
    return message;
  }

  private void setMessage(String message) {
    this.message = message;
  }

  public String getStacktrace() {
    return stacktrace;
  }

  private void setStacktrace(String stacktrace) {
    this.stacktrace = stacktrace;
  }

  public Map<String, Object> toJsonMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", getTestCase());
    map.put("testClass", getTestClass());
    if (getStartedAt() != null) {
      map.put("started", getStartedAt());
      if (getFinishedAt() != null) {
        map.put("finished", getFinishedAt());
        map.put("duration", Duration.between(getStartedAt(), getFinishedAt()));
        map.put("state", getResult());
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
    map.put("exception", exception);
    map.put("message", message);
    map.put("stacktrace", stacktrace);
    return map;
  }

  public static Builder newBuilder() {
    return new TestRun().new Builder();
  }

  public class Builder {

    private Builder() {}

    public Builder setId(Long id) {
      TestRun.this.id = id;
      return this;
    }

    public Builder setTestClass(String testClass) {
      TestRun.this.testClass = testClass;
      return this;
    }

    public Builder setTestCase(String testcase) {
      TestRun.this.testCase = testcase;
      return this;
    }

    public Builder setResult(String result) {
      TestRun.this.result = result;
      return this;
    }

    public Builder setStartedAt(Instant startedAt) {
      TestRun.this.startedAt = startedAt;
      return this;
    }

    public Builder setFinishedAt(Instant finishedAt) {
      TestRun.this.finishedAt = finishedAt;
      return this;
    }

    public Builder setException(String exception) {
      TestRun.this.exception = exception;
      return this;
    }

    public Builder setMessage(String message) {
      TestRun.this.message = message;
      return this;
    }

    public Builder setStacktrace(String stacktrace) {
      TestRun.this.stacktrace = stacktrace;
      return this;
    }

    public TestRun build() {
      return TestRun.this;
    }
  }

}
