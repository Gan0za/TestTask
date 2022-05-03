package com.example.camel.demo;

import com.example.camel.demo.dto.PostRequestType;
import com.example.camel.demo.dto.ResponseType;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component
public class PostBean {

    @Handler
    public ResponseType response(PostRequestType input) {
        //получаем запрос и возвращаем объект ResponseType в ответ
        //camel автоматом превратит этот объект в json
        return new ResponseType("Hello, " + input.getName() + "!");
    }
}
