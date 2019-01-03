package ru.stqa.heroku.selenium;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;

@Path("builds")
public class BuildsServlet extends ServletBase {

  private HibernateStorage db = HibernateStorage.getInstance();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet() {
    return db.inSession((session) -> {
      session.updateBuilds();
      return gson().toJson(session.getTravisBuilds().stream().map(TravisBuild::toMinJsonMap).collect(Collectors.toList()));
    });
  }

}
