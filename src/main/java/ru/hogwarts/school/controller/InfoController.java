package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/getPort")
public class InfoController {

    @Value("${server.port}")
    Integer serverPort;

    @GetMapping
    public Integer getServerPort() {
        return serverPort;
    }
}
