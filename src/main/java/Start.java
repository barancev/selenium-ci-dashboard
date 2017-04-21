
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Start {

  public static void main(final String[] args) throws Exception {
    String webPort = System.getenv("PORT");
    if (webPort == null || webPort.isEmpty()) {
      webPort = "8080";
    }

    Server server = new Server(Integer.valueOf(webPort));

    String webappDirLocation = "src/main/webapp/";
    WebAppContext root = new WebAppContext();
    root.setContextPath("/");
    root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
    root.setResourceBase(webappDirLocation);
    root.setParentLoaderPriority(true);

    server.setHandler(root);
    server.start();
    server.join();
  }
}
