package org.example;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    public void configure() {
        from("jetty://http://localhost:8888/greeting")
                .log("Received a request")
                .setBody(simple("${date:now:yyyy-MM-dd'T'HH:mm:ssZ}"));

    }

}
