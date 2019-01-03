package ru.stqa.heroku.selenium;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("job")
public class JobServlet extends ServletBase {

  private HibernateStorage db = HibernateStorage.getInstance();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id) {
    return db.inSession((session) -> {
      session.updateBuilds();
      return gson().toJson(session.populateJobHistory(session.getTravisJob(id)).toFullJsonMap());
    });
  }

  @GET
  @Path("{id}/{testClass}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id, @PathParam("testClass") String testClass) {
    return db.inSession((session) -> {
      session.updateBuilds();
      return gson().toJson(session.getTravisJob(id).toFullJsonMap(testClass));
    });
  }

  @GET
  @Path("{id}/{testClass}/{testCase}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id, @PathParam("testClass") String testClass, @PathParam("testCase") String testCase) {
    return db.inSession((session) -> {
      session.updateBuilds();
      return gson().toJson(session.getTravisJob(id).toFullJsonMap(testClass, testCase));
    });
  }
}
