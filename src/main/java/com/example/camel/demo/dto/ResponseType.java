package com.example.camel.demo.dto;

public class ResponseType {
    private String greeter;

    public ResponseType(String greeter){
        this.greeter = greeter;
    }

    public String getGreeter() {
        return greeter;
    }

    public void setGreeter(String greeter) {
        this.greeter = greeter;
    }
}
