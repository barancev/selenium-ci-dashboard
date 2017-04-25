package ru.stqa.heroku.selenium;

import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("jobs")
public class JobsServlet extends ServletBase {

  private Storage db = Storage.getInstance();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String buildId) {
    List<TravisJob> jobs = db.getTravisJobs(buildId);
    Map<String, Object> map = new HashMap<>();
    map.put("records", jobs);
    map.put("queryRecordCount", jobs.size());
    map.put("totalRecordCount", jobs.size());
    return gson().toJson(map);
  }

}
