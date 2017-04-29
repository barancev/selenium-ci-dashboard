package ru.stqa.heroku.selenium;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
  private Instant lastCheck;
  private String branch;
  private String commit;
  private String commitMessage;
  private String commitAuthor;

  @OneToMany(mappedBy = "build", fetch = FetchType.LAZY)
  @OrderBy("id")
  private List<TravisJob> jobs = new ArrayList<>();

  TravisBuild() {}

  TravisBuild updateFrom(TravisBuild other) {
    this.number = other.number;
    this.status = other.status;
    this.result = other.result;
    this.startedAt = other.startedAt;
    this.finishedAt = other.finishedAt;
    this.branch = other.branch;
    this.commit = other.commit;
    this.commitMessage = other.commitMessage;
    this.commitAuthor = other.commitAuthor;
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
  }

  public Instant getFinishedAt() {
    return finishedAt;
  }

  void setFinishedAt(Instant finishedAt) {
    this.finishedAt = finishedAt;
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

  public List<TravisJob> getJobs() {
    return jobs;
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
        if ("0".equals(getResult())) {
          map.put("state", "passed");
        } else {
          map.put("state", "failed");
        }
        //map.put("state", "skipped");
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
    map.put("branch", getBranch());
    map.put("commit", getCommit());
    map.put("commitMessage", getCommitMessage());
    map.put("commitAuthor", getCommitAuthor());
    return map;
  }

  public Map<String, Object> toFullJsonMap() {
    Map<String, Object> map = toMinJsonMap();
    map.put("jobs", jobs.stream().map(TravisJob::toMinJsonMap).collect(Collectors.toList()));
    return map;
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
      TravisBuild.this.startedAt = startedAt;
      return this;
    }

    public Builder setFinishedAt(Instant finishedAt) {
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

    public Builder setCommitMessage(String commitMessage) {
      TravisBuild.this.commitMessage = commitMessage;
      return this;
    }

    public Builder setCommitAuthor(String commitAuthor) {
      TravisBuild.this.commitAuthor = commitAuthor;
      return this;
    }

    public TravisBuild build() {
      return TravisBuild.this;
    }
  }

}
