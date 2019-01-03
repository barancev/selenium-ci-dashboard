package ru.stqa.heroku.selenium;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("build")
public class BuildServlet extends ServletBase {

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id) {
    return Storage.getInstance().inSession((session) -> {
      session.updateBuilds();
      return gson().toJson(session.getTravisBuild(id).toFullJsonMap());
    });
  }

}
