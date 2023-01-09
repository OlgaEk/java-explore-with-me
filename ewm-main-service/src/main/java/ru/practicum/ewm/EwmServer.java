package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EwmServer {

    public static void main(String[] args) {
        System.setProperty("server.port", "8080");
        SpringApplication.run(EwmServer.class, args);
    }

}
