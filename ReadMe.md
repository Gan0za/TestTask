# Camel Java Router Project

## === How to build

To build this project use

    mvn install

## === How to run

You can run this example using

    mvn camel:run

## === More information

You can find more information about Apache Camel at the website: http://camel.apache.org/

## === Test queries

Test post querie

    curl -X POST -H "Content-Type: application/json" -d '{"text": "test"}' http://localhost:8888/echo

Test simple querie

    curl http://localhost:8888/time
