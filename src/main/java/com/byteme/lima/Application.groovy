package com.byteme.lima

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.core.env.Environment

@SpringBootApplication
class Application extends SpringBootServletInitializer {
    static Log LOG = LogFactory.getLog(Application.class)

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.sources(Application.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder app = new Application().configure(new SpringApplicationBuilder(Application.class));
        Environment env = app.run(args).getEnvironment();
        LOG.info(
"""
============================================================
ByteMe - LIMA
============================================================
\tExternal:\thttp://${InetAddress.getLocalHost().getHostAddress()}:${env.getProperty("server.port")}${env.getProperty("server.context-path")}
\tInternal:\thttp://localhost:${env.getProperty("server.port")}${env.getProperty("server.context-path")}
============================================================
"""
        )
    }
}
