package com.advdsp.service.receive;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.net.URL;

/**
 */
public class DspReceiveServiceMainShell {

    public static void main(String[] args) throws Exception {
        try {
            final String appDir = new DspReceiveServiceMainShell().getWebAppsPath();
            final Server server = new Server(ServiceConfiguration.getInstance().getConfig().getInt("dsp.receive.service.port", 8310));
            WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setBaseResource(Resource.newResource(appDir));
            server.setHandler(context);
            server.start();
            server.join();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("webapp not found in CLASSPATH.");
            System.exit(2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("start metastore service error.");
            System.exit(1);
        }
    }

    private String getWebAppsPath() throws IOException {
        URL url = getClass().getClassLoader().getResource("webapps");
        if (url == null)
            throw new IOException("webapp not found in CLASSPATH");
        return url.toString();
    }
}
