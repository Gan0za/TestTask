package org.example;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    public void configure() {
        from("jetty://http://localhost:8888/test") //Тестовый запрос
                .log("Received a test request. Time: ${date:now:yyyy-MM-dd' 'HH:mm:ssZ}") //Логирование
                .setBody(simple("\n<h1>Hello world!</h1>\n")); //Вывод на старницу
        from("jetty://http://localhost:8888/echo?httpMethodRestrict=POST") //Пост запрос
                .convertBodyTo(String.class)
                .log("Received a post request. Time: ${date:now:yyyy-MM-dd' 'HH:mm:ssZ}\n" +//Логирование
                        "Message: ${body}\n")// сообщение в логе
                .process(exchange -> exchange.getOut().setBody(exchange.getIn().getBody())); //Ответ клиенту
    }

}
