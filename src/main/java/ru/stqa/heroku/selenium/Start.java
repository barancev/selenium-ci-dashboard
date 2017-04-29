package ru.stqa.heroku.selenium;

import org.eclipse.jetty.quickstart.QuickStartWebApp;
import org.eclipse.jetty.server.Server;

public class Start {

  public static void main(final String[] args) throws Exception {
    String webPort = System.getenv("PORT");
    if (webPort == null || webPort.isEmpty()) {
      webPort = "8080";
    }

    Server server = new Server(Integer.valueOf(webPort));

    String webappDirLocation = "src/main/webapp/";
    QuickStartWebApp root = new QuickStartWebApp();
    root.setAutoPreconfigure(true);
    root.setContextPath("/");
    root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
    root.setResourceBase(webappDirLocation);
    root.setParentLoaderPriority(true);

    server.setHandler(root);
    server.start();
    server.join();
  }
}
