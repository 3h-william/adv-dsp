package com.advdsp.service.send;

import com.advdsp.service.dsp.common.kafka.consumer.KafkaMultiThread;
import com.advdsp.service.dsp.processor.kafka.KafkaProcessor;
import com.advdsp.service.send.memory.AdxConfigInfo;
import com.advdsp.service.send.memory.AppBaseConfigInfo;
import com.advdsp.service.send.memory.SystemConfigInfo;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 */
public class DspSendServiceMainShell {
    private static Logger logger = LoggerFactory.getLogger(DspSendServiceMainShell.class.getName());


    public static void main(String[] args) throws Exception {
        // env check ，防止producer文件没有放
        KafkaProcessor.getKafkaProcessorInstance();

        // 启动定时元数据刷新
        System.out.println("start metadata refresh ...");
        try {
            // start thread TODO backend update configuration
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            AppBaseConfigInfo.getAppConfigInfoInstance().loadAppConfig();
                            SystemConfigInfo.getSystemConfigInfoInstance().loadSystemConfig();
                            AdxConfigInfo.getAdxConfigInfoInstance().loadAdxConfig();
                            logger.info("update config finished ... ");
                        } catch (Throwable t) {
                            logger.error("[ConfigUpdate] load failed", t);
                        }
                        try {
                            // TODO 时间可配置
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            logger.error("[ConfigUpdate] sleep failed");
                        }
                    }

                }
            }).start();

            System.out.println("start web server ...");
            final String appDir = new DspSendServiceMainShell().getWebAppsPath();
            final Server server = new Server(ServiceConfiguration.getInstance().getConfig().getInt("dsp.send.service.port", 8311));
            WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setBaseResource(Resource.newResource(appDir));
            server.setHandler(context);
            server.start();
            System.out.println("start kafka consumer ... ");
            String topics = ServiceConfiguration.getInstance().getConfig().getString("dsp.data.topic");
            System.out.println("listener topic is : " + topics);
            if ("copy".equalsIgnoreCase(ServiceConfiguration.getInstance().getConfig().getString("dsp.send.model"))) {
                System.out.println("start with copy mode ");
                KafkaMultiThread kafkaMultiThread = new KafkaMultiThread("com.advdsp.service.send.consumer.DspCopyDataConsumer", topics);
                kafkaMultiThread.start();
            }
            else if ("resend".equalsIgnoreCase(ServiceConfiguration.getInstance().getConfig().getString("dsp.send.model"))) {
                System.out.println("start with resend mode ");
                KafkaMultiThread kafkaMultiThread = new KafkaMultiThread("com.advdsp.service.send.consumer.DspErrorDataConsumer", topics);
                kafkaMultiThread.start();
            } else {
                System.out.println("start with normal mode ");
                KafkaMultiThread kafkaMultiThread = new KafkaMultiThread("com.advdsp.service.send.consumer.DspDataConsumer", topics);
                kafkaMultiThread.start();
            }
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
