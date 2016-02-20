package com.byteme.lima;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@SpringBootApplication
public class JavaApp extends SpringBootServletInitializer {
    private static Log LOG = LogFactory.getLog(JavaApp.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JavaApp.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder app = new JavaApp().configure(new SpringApplicationBuilder(JavaApp.class));
        Environment env = app.run(args).getEnvironment();
        LOG.info(
                "\n" +
                        "Access URLs:\n" +
                        "----------------------------------------------------------\n" +
                        "\tExternal: \thttp://" + InetAddress.getLocalHost().getHostAddress() + ":" + env.getProperty("server.port" ) + env.getProperty("server.context-path" ) + "\n" +
                        "\tInternal: \thttp://localhost:" + env.getProperty("server.port" ) + env.getProperty("server.context-path" ) + "\n" +
                        "----------------------------------------------------------"
        );
    }
}
