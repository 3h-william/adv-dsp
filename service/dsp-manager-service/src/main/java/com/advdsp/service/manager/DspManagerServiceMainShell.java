package com.advdsp.service.manager;

import com.advdsp.monitor.kafka.offset.KafkaMetricsBackend;
import com.advdsp.service.manager.statistic.StatisticLoader;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;


/**
 */
public class DspManagerServiceMainShell {

    private static Logger logger = LoggerFactory.getLogger(DspManagerServiceMainShell.class.getName());

    public static void main(String[] args) throws Exception {
        try {


            if (ServiceConfiguration.getInstance().getConfig().getBoolean("dsp.manager.statistic.open", false)) {
                new Thread(new StatisticLoader()).start();
            }

            if (ServiceConfiguration.getInstance().getConfig().getBoolean("kafka.monitor.open", false)) {
                String[] group_topics = ServiceConfiguration.getInstance().getConfig().getStringArray("kafka.monitor.group.topics");
                int interval = ServiceConfiguration.getInstance().getConfig().getInt("kafka.monitor.interval", 5);
                String bootstrapServers = ServiceConfiguration.getInstance().getConfig().getString("kafka.monitor.bootstrap.servers", "localhost:9020");
                new Thread(new KafkaMetricsBackend(group_topics, interval, bootstrapServers)).start();
            }

            System.out.println("start web server ...");
            final String appDir = new DspManagerServiceMainShell().getWebAppsPath();
            final Server server = new Server(ServiceConfiguration.getInstance().getConfig().getInt("dsp.manager.service.port", 9100));
            WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setBaseResource(Resource.newResource(appDir));
            server.setHandler(context);
            server.start();
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
