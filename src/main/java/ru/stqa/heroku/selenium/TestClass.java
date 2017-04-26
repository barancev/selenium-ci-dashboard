package ru.stqa.heroku.selenium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

  public String getCollapsedName() {
    return Arrays.stream(name.split("\\."))
      .reduce(new ArrayList<String>(),
        (list, element) -> {
          if (list.size() > 0) list.set(list.size()-1, list.get(list.size()-1).substring(0,1));
          list.add(element);
          return list;
        },
        (l1, l2) -> l1).stream().collect(Collectors.joining("."));
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

  public Map<String, Object> toJsonMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", getName());
    map.put("collapsedName", getCollapsedName());
    map.put("passed", passed > 0 ? passed : "");
    map.put("failed", failed > 0 ? failed : "");
    map.put("skipped", skipped > 0 ? skipped : "");
    map.put("running", running > 0 ? running : "");
    return map;
  }

}
