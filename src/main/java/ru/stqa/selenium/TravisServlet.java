package ru.stqa.selenium;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

@Path("travis")
public class TravisServlet {

  private static Logger log = Logger.getLogger("travis");

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void doPost(@FormParam("payload") String payload) throws ClassNotFoundException, SQLException {
    log.info(payload);
//    JsonObject json = new JsonParser().parse(payload).getAsJsonObject();
//    TravisBuild travisBuild = TravisBuild.newBuilder()
//      .setId(json.get("id").getAsString())
//      .setStatus(json.get("status").getAsString())
//      .setStartedAt(json.get("started_at").getAsString())
//      .setFinishedAt(json.get("finished_at").getAsString())
//      .setBranch(json.get("branch").getAsString())
//      .setCommit(json.get("commit").getAsString())
//      .setUrl(json.get("build_url").getAsString())
//      .build();
  }

}
