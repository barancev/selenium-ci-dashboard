package ru.stqa.heroku.selenium;

import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("job")
public class JobServlet extends ServletBase {

  private Storage db = Storage.getInstance();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id) {
    try (Session session = db.createSession()) {
      return gson().toJson(db.getTravisJob(id, session).toFullJsonMap());
    }
  }

  @GET
  @Path("{id}/{testClass}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id, @PathParam("testClass") String testClass) {
    try (Session session = db.createSession()) {
      return gson().toJson(db.getTravisJob(id, session).toFullJsonMap(testClass));
    }
  }

  @GET
  @Path("{id}/{testClass}/{testCase}")
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet(@PathParam("id") String id, @PathParam("testClass") String testClass, @PathParam("testCase") String testCase) {
    try (Session session = db.createSession()) {
      return gson().toJson(db.getTravisJob(id, session).toFullJsonMap(testClass, testCase));
    }
  }

}
