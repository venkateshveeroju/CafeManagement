package com.restaurant.tablebookingapp.rest;

import jdk.jfr.Registered;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest {
    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody(required=true) Map<String,String> requestMap);

    @PostMapping(path = "/test")
    public ResponseEntity<String> test(@RequestBody(required=true) Map<String,String> requestMap);
}
