package ru.stqa.heroku.selenium;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

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
  // started, cancelled
  private String state;
  private Instant startedAt;
  private Instant finishedAt;
  private Instant checkedAt;
  private String branch;
  private String commit;
  private String commitMessage;
  private String commitAuthor;

  @OneToMany(mappedBy = "build", fetch = FetchType.LAZY)
  @OrderBy("id")
  private List<TravisJob> jobs = new ArrayList<>();

  TravisBuild() {}

  TravisBuild updateFrom(JsonObject json) {
    if (json.get("status").getAsInt() == 0) {
      this.state = "passed";
    } else {
      this.state = "failed";
      // when this.state = cancelled ?
    }
    this.state = stringOrNull(json.get("state"));
    this.startedAt = instantOrNull(json.get("started_at"));
    this.finishedAt = instantOrNull(json.get("finished_at"));
    this.checkedAt = Instant.now();
    return this;
  }

  private String stringOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : json.getAsString();
  }

  private Instant instantOrNull(JsonElement json) {
    return json == null || json instanceof JsonNull ? null : Instant.parse(json.getAsString());
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

  void setState(String state) {
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

  public Instant getCheckedAt() {
    return checkedAt;
  }

  void setCheckedAt(Instant checkedAt) {
    this.checkedAt = checkedAt;
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
        map.put("state", state);
      } else {
        map.put("finished", "-");
        map.put("duration", Duration.between(getStartedAt(), Instant.now()));
        map.put("state", "running");
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

    public Builder setState(String state) {
      TravisBuild.this.state = state;
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
      TravisBuild.this.checkedAt = Instant.now();
      return TravisBuild.this;
    }
  }

}
