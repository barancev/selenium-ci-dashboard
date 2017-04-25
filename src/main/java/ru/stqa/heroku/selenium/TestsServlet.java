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

@Path("tests")
public class TestsServlet {

  private Storage db = Storage.getInstance();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String jobId) {
    List<TestRun> jobs = db.getTestRuns(jobId);
    Map<String, Object> map = new HashMap<>();
    map.put("records", jobs);
    map.put("queryRecordCount", jobs.size());
    map.put("totalRecordCount", jobs.size());
    return new Gson().toJson(map);
  }

}
