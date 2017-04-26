package ru.stqa.heroku.selenium;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tests")
public class TestRun {

  @Id
  @GeneratedValue
  private Long id;
  private String testClass;
  private String testCase;
  private String jobId;
  private String result;
  private String startedAt;
  private String finishedAt;
  @Type(type="text")
  private String stacktrace;

  TestRun() {}

  TestRun updateFrom(TestRun other) {
    this.id = other.id;
    this.testClass = other.testClass;
    this.testCase = other.testCase;
    this.jobId = other.jobId;
    this.result = other.result;
    this.startedAt = other.startedAt;
    this.finishedAt = other.finishedAt;
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

  public String getJobId() {
    return jobId;
  }

  private void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getResult() {
    return result;
  }

  private void setResult(String result) {
    this.result = result;
  }

  public String getStartedAt() {
    return startedAt;
  }

  private void setStartedAt(String startedAt) {
    this.startedAt = startedAt;
  }

  public String getFinishedAt() {
    return finishedAt;
  }

  private void setFinishedAt(String finishedAt) {
    this.finishedAt = finishedAt;
  }

  public String getStacktrace() {
    return stacktrace;
  }

  private void setStacktrace(String stacktrace) {
    this.stacktrace = stacktrace;
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

    public Builder setJobId(String jobId) {
      TestRun.this.jobId = jobId;
      return this;
    }

    public Builder setResult(String result) {
      TestRun.this.result = result;
      return this;
    }

    public Builder setStartedAt(String startedAt) {
      TestRun.this.startedAt = startedAt;
      return this;
    }

    public Builder setFinishedAt(String finishedAt) {
      TestRun.this.finishedAt = finishedAt;
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
