package ru.stqa.heroku.selenium;

public class TestClass {

  private String name;
  private int passed;
  private int failed;
  private int skipped;
  private int running;

  public TestClass(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void incPassed() {
    passed++;
  }

  public void incFailed() {
    failed++;
  }

  public void incSkipped() {
    skipped++;
  }

  public void incRunning() {
    running++;
  }

}
