package com.example.camel.demo;

import com.example.camel.demo.dto.PostRequestType;
import org.apache.camel.Handler;

public class ProcessBeforeKafka {

    @Handler
    public String response(PostRequestType input) {
        //из запроса просто берем поле name
        return input.getName();
    }
}
