package ru.stqa.heroku.selenium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("build")
public class BuildServlet {

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id) {
    return Storage.getInstance().inSession((session) -> {
      session.updateBuilds();
      try {
        return new ObjectMapper().writeValueAsString(session.getTravisBuild(id).toFullJsonMap());
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        return "";
      }
    });
  }

}
