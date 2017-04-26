package ru.stqa.heroku.selenium;

import com.google.gson.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("builds")
public class BuildsServlet extends ServletBase {

  private Storage db = Storage.getInstance();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String doGet() {
    List<Map<String, Object>> builds = db.getTravisBuilds().stream().map(TravisBuild::toJsonMap).collect(Collectors.toList());
    Map<String, Object> map = new HashMap<>();
    map.put("records", builds);
    map.put("queryRecordCount", builds.size());
    map.put("totalRecordCount", builds.size());
    return gson().toJson(map);
  }

}
