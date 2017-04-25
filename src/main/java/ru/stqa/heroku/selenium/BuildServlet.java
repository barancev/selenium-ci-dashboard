package ru.stqa.heroku.selenium;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.logging.Logger;

@Path("build")
public class BuildServlet extends ServletBase {

  private static Logger log = Logger.getLogger(" build");

  private Storage db = Storage.getInstance();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id) {
    TravisBuild build = db.getTravisBuild(id);
    if (build.getFinishedAt() == null) {
      build.setFinishedAt(Instant.now());
    }
    return gson().toJson(build);
  }

}
