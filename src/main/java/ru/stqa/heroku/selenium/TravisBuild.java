package ru.stqa.heroku.selenium;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "builds")
public class TravisBuild {

  @Id
  private String id;
  private String number;
  private String status;
  private String result;
  private Instant startedAt;
  private Instant finishedAt;
  private Duration duration;
  private String branch;
  private String commit;
  private String commitMessage;
  private String commitAuthor;
  private boolean pullRequest;
  private String pullRequestNumber;
  private String pullRequestTitle;

  TravisBuild() {}

  TravisBuild updateFrom(TravisBuild other) {
    this.number = other.number;
    this.status = other.status;
    this.result = other.result;
    this.startedAt = other.startedAt;
    this.finishedAt = other.finishedAt;
    this.duration = other.duration;
    this.branch = other.branch;
    this.commit = other.commit;
    this.commitMessage = other.commitMessage;
    this.commitAuthor = other.commitAuthor;
    this.pullRequest = other.pullRequest;
    this.pullRequestNumber = other.pullRequestNumber;
    this.pullRequestTitle = other.pullRequestTitle;
    return this;
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

  public Duration getDuration() {
    return duration;
  }

  public String getBranch() {
    return branch;
  }

  private void setBranch(String branch) {
    this.branch = branch;
  }

  public String getCommit() {
    return commit;
  }

  private void setCommit(String commit) {
    this.commit = commit;
  }

  public String getCommitMessage() {
    return commitMessage;
  }

  private void setCommitMessage(String commitMessage) {
    this.commitMessage = commitMessage;
  }

  public String getCommitAuthor() {
    return commitAuthor;
  }

  private void setCommitAuthor(String commitAuthor) {
    this.commitAuthor = commitAuthor;
  }

  public boolean isPullRequest() {
    return pullRequest;
  }

  private void setPullRequest(boolean pullRequest) {
    this.pullRequest = pullRequest;
  }

  public String isPullRequestNumber() {
    return pullRequestNumber;
  }

  private void setPullRequestNumber(String pullRequestNumber) {
    this.pullRequestNumber = pullRequestNumber;
  }

  public String isPullRequestTitle() {
    return pullRequestTitle;
  }

  private void setPullRequestTitle(String pullRequestTitle) {
    this.pullRequestTitle = pullRequestTitle;
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

    public Builder setStartedAt(Instant startedAt) {
      TravisBuild.this.setStartedAt(startedAt);
      return this;
    }

    public Builder setFinishedAt(Instant finishedAt) {
      TravisBuild.this.setFinishedAt(finishedAt);
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

    public Builder setCommitMessage(String commitMessage) {
      TravisBuild.this.commitMessage = commitMessage;
      return this;
    }

    public Builder setCommitAuthor(String commitAuthor) {
      TravisBuild.this.commitAuthor = commitAuthor;
      return this;
    }

    public Builder setPullRequest(boolean pullRequest) {
      TravisBuild.this.pullRequest = pullRequest;
      return this;
    }

    public Builder setPullRequestNumber(String pullRequestNumber) {
      TravisBuild.this.pullRequestNumber = pullRequestNumber;
      return this;
    }

    public Builder setPullRequestTitle(String pullRequestTitle) {
      TravisBuild.this.pullRequestTitle = pullRequestTitle;
      return this;
    }

    public TravisBuild build() {
      return TravisBuild.this;
    }
  }

}