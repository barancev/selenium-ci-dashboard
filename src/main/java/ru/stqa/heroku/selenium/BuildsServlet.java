package ru.stqa.heroku.selenium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;

@Path("builds")
public class BuildsServlet {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet() {
    return Storage.getInstance().inSession((session) -> {
      session.updateBuilds();
      try {
        return new ObjectMapper().writeValueAsString(session.getTravisBuilds().stream().map(TravisBuild::toMinJsonMap).collect(Collectors.toList()));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        return "";
      }
    });
  }

}
