package ru.stqa.selenium;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.Response;

@Path("travis")
public class TravisServlet {

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response doPost(@FormParam("payload") String payload) {
    return null;
  }

}
