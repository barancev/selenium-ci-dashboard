package ru.stqa.heroku.selenium;

import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;

@Path("builds")
public class BuildsServlet extends ServletBase {

  private Storage db = Storage.getInstance();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet() {
    try (Session session = db.createSession()) {
      return gson().toJson(db.getTravisBuilds(session).stream().map(TravisBuild::toMinJsonMap).collect(Collectors.toList()));
    }
  }

}
