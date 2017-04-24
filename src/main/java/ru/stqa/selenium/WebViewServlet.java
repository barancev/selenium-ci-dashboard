package ru.stqa.selenium;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

public class WebViewServlet extends HttpServlet {

  private static Logger log = Logger.getLogger("webview");

  private Storage db = Storage.getInstance();

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    printHeader(response.getWriter());
    if ("/".equals(request.getPathInfo())) {
      printBuildList(response.getWriter());
    } else {
      String[] parts = request.getPathInfo().substring(1).split("/");
      if (parts.length == 1) {
        String buildId = parts[0].substring("build-".length());
        printJobList(buildId, response.getWriter());
      }
    }
    printFooter(response.getWriter());
    response.getWriter().close();
  }

  private void printBuildList(PrintWriter out) {
    List<TravisBuild> builds = db.getTravisBuilds();
    out.println("<h1>Selenium Travis CI Dashboard</h1>");
    out.println("<table id=\"table-builds\"><thead><tr><th>Id</th><th>Number</th><th>Status</th><th>Result</th>" +
      "<th data-dynatable-column=\"startedAt\">Started At</th><th data-dynatable-column=\"finishedAt\">Finished At</th>" +
      "<th>Branch</th><th>Commit</th></tr></thead><tbody></tbody></table>");
    out.println("<script>");
    out.println("var builds = " + new Gson().toJson(builds) + ";");
    out.println("$('#table-builds').dynatable({ dataset: { records: builds }, features: { sorting: false } });");
    out.println("$('#table-builds').dynatable().on('click', 'tbody tr', function() {" +
      "window.location = 'build-' + $(this).find('td').first().text();" +
      "});");
    out.println("</script>");
  }

  private void printJobList(String buildId, PrintWriter out) {
    TravisBuild build = db.getTravisBuild(buildId);
    List<TravisJob> jobs = db.getTravisJobs(buildId);
    out.println("<a class=\"link-back\" href=\"..\">Back to build list</a>");
    out.println(String.format("<h1>Build %s</h1>", build.getNumber()));
    out.println("<table id=\"table-builds\"><thead><tr><th>Id</th><th>Number</th><th>Status</th><th>Result</th>" +
      "<th data-dynatable-column=\"startedAt\">Started At</th><th data-dynatable-column=\"finishedAt\">Finished At</th>" +
      "<th>OS</th><th>Language</th><th>Env</th></tr></thead><tbody></tbody></table>");
    out.println("<script>");
    out.println("var jobs = " + new Gson().toJson(jobs) + ";");
    out.println("$('#table-builds').dynatable({ dataset: { records: jobs }, features: { sorting: false } });");
    out.println("$('#table-builds').dynatable().on('click', 'tbody tr', function() {" +
      "window.location = 'job-' + $(this).find('td').first().text();" +
      "});");
    out.println("</script>");
  }

  public void printHeader(PrintWriter out) {
    out.println("<html><head>");
    out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>");
    out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Dynatable/0.3.1/jquery.dynatable.min.js\"></script>");
    out.println("<link rel=\"stylesheet\" href=\"//cdnjs.cloudflare.com/ajax/libs/Dynatable/0.3.1/jquery.dynatable.min.css\" />");
    out.println("</head><body>");
  }

  public void printFooter(PrintWriter out) {
    out.println("</body></html>");
  }

}
