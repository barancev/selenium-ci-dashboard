package ru.stqa.heroku.selenium;

import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("build")
public class BuildServlet extends ServletBase {

  private static Logger log = Logger.getLogger(" build");

  private Storage db = Storage.getInstance();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id) {
    try (Session session = db.createSession()) {
      return gson().toJson(db.getTravisBuild(id, session).toFullJsonMap());
    }
  }

}
