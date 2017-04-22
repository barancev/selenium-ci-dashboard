package ru.stqa.selenium;

public class TravisBuild {

  private String id;
  private String startedAt;
  private String finishedAt;
  private String status;
  private String branch;
  private String commit;
  private String url;

  private TravisBuild() {}

  public String getId() {
    return id;
  }

  public String getStartedAt() {
    return startedAt;
  }

  public String getFinishedAt() {
    return finishedAt;
  }

  public String getStatus() {
    return status;
  }

  public String getBranch() {
    return branch;
  }

  public String getCommit() {
    return commit;
  }

  public String getUrl() {
    return url;
  }

  public static Builder newBuilder() {
    return new TravisBuild().new Builder();
  }

  public class Builder {

    public Builder setId(String id) {
      TravisBuild.this.id = id;
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

    public Builder setStatus(String status) {
      TravisBuild.this.status = status;
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

    public Builder setUrl(String url) {
      TravisBuild.this.url = url;
      return this;
    }

    public TravisBuild build() {
      return TravisBuild.this;
    }
  }

}
