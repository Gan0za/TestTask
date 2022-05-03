package com.example.camel.demo;

import com.example.camel.demo.dto.PostRequestType;
import com.example.camel.demo.dto.ResponseType;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class DemoRouter extends RouteBuilder {

    @Override
    public void configure() {
        // начало конфигурации реста
        restConfiguration()
                //использование servlet
                //эта настройка говорит camel'у, чтобы он создал сервлет и использовал его как host для RESTful API
                //т.к используется spring boot, то по-умолчанию контейнер сервлетов будет Tomcat
                .component("servlet")
                // позволяет camel'у сериализовывать/десериализовывать Java объекты и JSON между собой
                .bindingMode(RestBindingMode.auto);
        //теперь описание самого REST API
        rest().path("/api") // разворачиваем на http://host:port/camel/api по дефолту с версии 2.19 context-path=/camel
                .consumes("application/json")
                .produces("application/json")
                // HTTP: POST /api
                .post("/processRequest")
                .type(PostRequestType.class) // сериализация json запроса в PostRequestType
                .outType(ResponseType.class) // десериализация ответа в json из ResponseType
                .to("direct:processRequest");// прокидываем вызов дальше в цепочку direct:processRequest

        //описание цепочки direct:processRequest
        //про direct - https://camel.apache.org/components/3.16.x/direct-component.html
        from("direct:processRequest")
                .routeId("directRequest")// просто ИД маршрута (пишется в логи)
                //чтобы не ждать пока завершатися работа с kafka, вызываем ее параллельно в другой ветке(direct:asyncWire) с теми же параметрами
                .wireTap("direct:asyncWire")//https://camel.apache.org/components/3.16.x/eips/wireTap-eip.html
                //а пока отправляем ответ на rest запрос
                //по каким правилам идет вызов методов бина см.здесь: https://camel.apache.org/manual/bean-binding.html
                //также, чтобы вызвать бин через его имя, нужно указать его в контексте для этого PostBean помечен аннотацией @Component
                .to("bean:postBean")//вызов метода класса PostBean, который обработает http ответ
                .log("End processRequest")
                .end();//конец вилки

        //описание цепочки direct:asyncWire для параллельной работы с kafka
        from("direct:asyncWire")
                .routeId("asyncWire")// просто ИД маршрута
                .log("Start asyncWire call")
                // перед отправкой сообщения(exchange) в кафку предварительно обрабатываем input через bean
                .bean(ProcessBeforeKafka.class)// здесь вызов через .class просто для разнообразия
                .to("direct:kafkaProduce")// вызов цепочки kafka producer
                //перед следующим вызовом записи в kafka обработаем сообщения(exchange) через Processor
                // можно создать также отдельный класс для этого, но можно описать как тут через lambda
                .process(exchange -> {
                    //до этого в ProcessBeforeKafka мы прокинули дальше только строку, поэтому можем явно указать тип объекта
                    String obj = (String) exchange.getIn().getBody();
                    //записываем обработанную строку в body
                    exchange.getIn().setBody("Processed " + obj);
                })
                .to("direct:kafkaProduce") // еще один вызов kafka producer
                .end(); //конец вилки

        //описание цепочки direct:kafkaProduce для записи в kafka
        from("direct:kafkaProduce")
                .routeId("kafkaProduce")
                //вызов записи kafka с текущими парамтерами(exchange)
                //параметры берутся из application.properties
                //все параметры и примеры: https://camel.apache.org/components/3.16.x/kafka-component.html
                .to("kafka:{{producer.topic}}?brokers={{kafka.broker}}")
                .log("${headers}");//PRODUCER HEADERS

        //описание listener'а kafka
        //все сообщения, записанные в kafka через producer'ов(или вручную) будут моментально обработаны этим consumer'ом
        from("kafka:{{consumer.topic}}?brokers={{kafka.broker}}" //все параметры и примеры опять же тут: https://camel.apache.org/components/3.16.x/kafka-component.html
                + "&seekTo={{consumer.seekTo}}"
                + "&autoOffsetReset={{consumer.autoOffsetReset}}"
                + "&groupId={{consumer.group}}")
                .routeId("kafkaConsume")
                .log("Message received from Kafka : ${body}") // CONSUMER HEADERS
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .log("    on the partition ${headers[kafka.PARTITION]}")
                .log("    with the offset ${headers[kafka.OFFSET]}")
                .log("    with the key ${headers[kafka.KEY]}");
    }
}
