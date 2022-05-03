# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.6/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#using-boot-devtools)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.6.6/reference/htmlsingle/#production-ready)

### Guides
The following guides illustrate how to use some features concretely:

* [Using Apache Camel with Spring Boot](https://camel.apache.org/camel-spring-boot/latest/spring-boot.html)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Test queries

Test post quire

    curl -X POST -H "Content-Type: application/json" -d '{"name": "test"}' http://localhost:8080/camel/api/processRequest

### Task

Написать приложение используя в основе Apache Camel<br>
Приложение реализует маршрут:<br>
* Приём http post запроса (сервер) -> <br>
* ответ клиенту 200 ok и отправка запроса в брокер (activemq, rabitmq или kafka) -> <br>
* processor (изменение содержимого) -> <br>
* отправка запроса в брокер (activemq, rabitmq или kafka) -> <br>
* вывод в log результата.

Приложение может запускаться через (вариант на выбор):<br>
* Karaf (или ServiceMix), Bundle<br>
* Camel Main, Standalone Java<br>
* Spring Boot + Camel, Standalone Java<br>