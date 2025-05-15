package com.vasilev.news.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HelloController {

    @GetMapping(value = "/hello", produces = "application/json")
    @ResponseBody()
    public ResponseEntity<Map<String, String>> hello(
            @RequestParam(name = "name", required = false, defaultValue = "world")
            String name
    ) {
        return new ResponseEntity<>(Map.of("name", name), HttpStatus.OK);
    }
}
