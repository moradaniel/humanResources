package web;

import java.io.File;

import org.apache.catalina.startup.Tomcat;

public class EmbeddedTomcatLauncher {

    public static void main(String[] args) throws Exception {

        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "58080";
        }

        tomcat.setPort(Integer.valueOf(webPort));
        
        String contextPath = "/creditos"; 
        //String appBase = "/opt/tomcat7/webapps/examples";
        String webappDirLocation = "src/main/webapp/";
        tomcat.addWebapp(contextPath, new File(webappDirLocation).getAbsolutePath());

        /*tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());*/

        tomcat.start();
        tomcat.getServer().await();  
    }
}