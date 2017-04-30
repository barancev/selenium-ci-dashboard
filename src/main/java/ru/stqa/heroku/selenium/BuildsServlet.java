package ru.stqa.heroku.selenium;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.stream.Collectors;

@Path("builds")
public class BuildsServlet extends ServletBase {

  private Storage db = Storage.getInstance();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet() {
    return db.inSession((session) -> {
      db.updateBuilds(session);
      return gson().toJson(db.getTravisBuilds(session).stream()
        .sorted(Comparator.comparing(TravisBuild::getId).reversed())
        .map(TravisBuild::toMinJsonMap).collect(Collectors.toList()));
    });
  }

}
