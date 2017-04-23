package ru.stqa.selenium;

public class TravisJob {

  private String buildId;
  private String id;
  private String number;
  private String status;
  private String result;
  private String startedAt;
  private String finishedAt;
  private String os;
  private String language;
  private String env;
  private boolean allowFailure;

  private TravisJob() {}

  public String getBuildId() {
    return buildId;
  }

  public String getId() {
    return id;
  }

  public String getNumber() {
    return number;
  }

  public String getStatus() {
    return status;
  }

  public String getResult() {
    return result;
  }

  public String getStartedAt() {
    return startedAt;
  }

  public String getFinishedAt() {
    return finishedAt;
  }

  public String getOs() {
    return os;
  }

  public String getLanguage() {
    return language;
  }

  public String getEnv() {
    return env;
  }

  public boolean isAllowFailure() {
    return allowFailure;
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

    public Builder setStartedAt(String startedAt) {
      TravisJob.this.startedAt = startedAt;
      return this;
    }

    public Builder setFinishedAt(String finishedAt) {
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
