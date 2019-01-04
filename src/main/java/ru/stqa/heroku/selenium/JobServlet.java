package ru.stqa.heroku.selenium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("job")
public class JobServlet {

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id) {
    return Storage.getInstance().inSession((session) -> {
      session.updateBuilds();
      try {
        return new ObjectMapper().writeValueAsString(session.populateJobHistory(session.getTravisJob(id)).toFullJsonMap());
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        return "";
      }
    });
  }

  @GET
  @Path("{id}/{testClass}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id, @PathParam("testClass") String testClass) {
    return Storage.getInstance().inSession((session) -> {
      session.updateBuilds();
      try {
        return new ObjectMapper().writeValueAsString(session.getTravisJob(id).toFullJsonMap(testClass));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        return "";
      }
    });
  }

  @GET
  @Path("{id}/{testClass}/{testCase}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id, @PathParam("testClass") String testClass, @PathParam("testCase") String testCase) {
    return Storage.getInstance().inSession((session) -> {
      session.updateBuilds();
      try {
        return new ObjectMapper().writeValueAsString(session.getTravisJob(id).toFullJsonMap(testClass, testCase));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        return "";
      }
    });
  }
}
