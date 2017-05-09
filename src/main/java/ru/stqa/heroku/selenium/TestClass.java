package ru.stqa.heroku.selenium;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TestClass {

  private static Logger log = Logger.getLogger(TestClass.class.getName());

  private String name;
  private int passed;
  private int failed;
  private int skipped;
  private int running;

  private List<TestRun> testCases = new ArrayList<>();

  public TestClass(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void addTestCase(TestRun testCase) {
    testCases.add(testCase);
    if (testCase.getFinishedAt() ==  null) {
      running++;
    } else {
      switch (testCase.getResult()) {
        case "passed":
          passed++;
          break;
        case "failed":
          failed++;
          break;
        case "skipped":
          skipped++;
          break;
        default:
          log.info("Unknown test case result " + testCase.getResult());
      }
    }
  }

  static String collapse(String fullName) {
    return Arrays.stream(fullName.split("\\."))
      .reduce(new ArrayList<String>(),
        (list, element) -> {
          if (list.size() > 0) list.set(list.size()-1, list.get(list.size()-1).substring(0,1));
          list.add(element);
          return list;
        },
        (l1, l2) -> l1).stream().collect(Collectors.joining("."));
  }

  public Map<String, Object> toMinJsonMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", getName());
    map.put("collapsedName", collapse(getName()));
    map.put("passed", passed > 0 ? passed : "");
    map.put("failed", failed > 0 ? failed : "");
    map.put("skipped", skipped > 0 ? skipped : "");
    map.put("total", passed + failed + skipped);
    map.put("running", running > 0 ? running : "");
    return map;
  }

  public Map<String, Object> toFullJsonMap() {
    Map<String, Object> map = toMinJsonMap();
    testCases.sort(Comparator.comparing(TestRun::getId));
    map.put("testCases", testCases.stream()
      .sorted(Comparator.comparing(TestRun::getId))
      .map(TestRun::toJsonMap).collect(Collectors.toList()));
    return map;
  }

}
