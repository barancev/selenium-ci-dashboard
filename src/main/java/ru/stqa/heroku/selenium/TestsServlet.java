package ru.stqa.heroku.selenium;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("tests")
public class TestsServlet extends ServletBase {

  private Storage db = Storage.getInstance();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String jobId) {
    List<TestClass> jobs = db.getTestClasses(jobId);
    Map<String, Object> map = new HashMap<>();
    map.put("records", jobs.stream().map(TestClass::toJsonMap).collect(Collectors.toList()));
    return gson().toJson(map);
  }

  @GET
  @Path("{id}/{testClass}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String jobId, @PathParam("testClass") String testClass) {
    List<TestRun> testCases = db.getTestCases(jobId, testClass);
    Map<String, Object> map = new HashMap<>();
    map.put("records", testCases.stream().map(TestRun::toJsonMap).collect(Collectors.toList()));
    map.put("testClass", testClass);
    map.put("collapsedTestClass", TestClass.collapse(testClass));
    return gson().toJson(map);
  }

  @GET
  @Path("{id}/{testClass}/{testCase}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String jobId, @PathParam("testClass") String testClass, @PathParam("testCase") String testCase) {
    TestRun testRun = db.getTestCase(jobId, testClass, testCase);
    Map<String, Object> map = testRun.toJsonMap();
    map.put("jobId", jobId);
    map.put("testClass", testClass);
    map.put("collapsedTestClass", TestClass.collapse(testClass));
    return gson().toJson(map);
  }

}
