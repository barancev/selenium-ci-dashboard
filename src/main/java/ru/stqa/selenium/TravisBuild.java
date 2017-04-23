package ru.stqa.selenium;

public class TravisBuild {

  private String id;
  private String number;
  private String status;
  private String result;
  private String startedAt;
  private String finishedAt;
  private String branch;
  private String commit;

  private TravisBuild() {}

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

  public String getBranch() {
    return branch;
  }

  public String getCommit() {
    return commit;
  }

  public static Builder newBuilder() {
    return new TravisBuild().new Builder();
  }

  public class Builder {

    private Builder() {}

    public Builder setId(String id) {
      TravisBuild.this.id = id;
      return this;
    }

    public Builder setNumber(String number) {
      TravisBuild.this.number = number;
      return this;
    }

    public Builder setStatus(String status) {
      TravisBuild.this.status = status;
      return this;
    }

    public Builder setResult(String result) {
      TravisBuild.this.result = result;
      return this;
    }

    public Builder setStartedAt(String startedAt) {
      TravisBuild.this.startedAt = startedAt;
      return this;
    }

    public Builder setFinishedAt(String finishedAt) {
      TravisBuild.this.finishedAt = finishedAt;
      return this;
    }

    public Builder setBranch(String branch) {
      TravisBuild.this.branch = branch;
      return this;
    }

    public Builder setCommit(String commit) {
      TravisBuild.this.commit = commit;
      return this;
    }

    public TravisBuild build() {
      return TravisBuild.this;
    }
  }

}
