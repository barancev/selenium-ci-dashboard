package ru.stqa.heroku.selenium;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "jobs")
public class TravisJob {

  private String buildId;
  @Id
  private String id;
  private String number;
  private String status;
  private String result;
  private Instant startedAt;
  private Instant finishedAt;
  private Duration duration;
  private String os;
  private String language;
  private String env;
  private boolean allowFailure;

  private TravisJob() {}

  TravisJob updateFrom(TravisJob other) {
    this.buildId = other.buildId;
    this.number = other.number;
    this.status = other.status;
    this.result = other.result;
    this.startedAt = other.startedAt;
    this.finishedAt = other.finishedAt;
    this.duration = other.duration;
    this.os = other.os;
    this.language = other.language;
    this.env = other.env;
    this.allowFailure = other.allowFailure;
    return this;
  }

  public String getBuildId() {
    return buildId;
  }

  private void setBuildId(String buildId) {
    this.buildId = buildId;
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

  public String getStatus() {
    return status;
  }

  private void setStatus(String status) {
    this.status = status;
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
    if (startedAt != null && finishedAt != null) {
      duration = Duration.between(startedAt, finishedAt);
    } else {
      duration = null;
    }
  }

  public Instant getFinishedAt() {
    return finishedAt;
  }

  void setFinishedAt(Instant finishedAt) {
    this.finishedAt = finishedAt;
    if (startedAt != null && finishedAt != null) {
      duration = Duration.between(startedAt, finishedAt);
    } else {
      duration = null;
    }
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

  public static Builder newBuilder() {
    return new TravisJob().new Builder();
  }

  public class Builder {

    private Builder() {
    }

    public Builder setBuildId(String buildId) {
      TravisJob.this.buildId = buildId;
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

    public Builder setStatus(String status) {
      TravisJob.this.status = status;
      return this;
    }

    public Builder setResult(String result) {
      TravisJob.this.result = result;
      return this;
    }

    public Builder setStartedAt(Instant startedAt) {
      TravisJob.this.setStartedAt(startedAt);
      return this;
    }

    public Builder setFinishedAt(Instant finishedAt) {
      TravisJob.this.setFinishedAt(finishedAt);
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
