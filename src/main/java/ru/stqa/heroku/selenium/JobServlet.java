package ru.stqa.heroku.selenium;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;

@Path("job")
public class JobServlet extends ServletBase {

  private Storage db = Storage.getInstance();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id) {
    TravisJob job = db.getTravisJob(id);
    if (job.getFinishedAt() == null) {
      job.setFinishedAt(Instant.now());
    }
    return gson().toJson(job);
  }

}
