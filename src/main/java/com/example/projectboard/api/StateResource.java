package com.example.projectboard.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
//@CrossOrigin(origins = "http://172.17.0.1:3000", methods = RequestMethod.GET, allowedHeaders = "application/json")
public class StateResource {

    @RequestMapping(method = RequestMethod.GET,value = "/health-check")
    public ResponseEntity<String> checkServerState() {
        return ResponseEntity.ok("connected");
    }
}
