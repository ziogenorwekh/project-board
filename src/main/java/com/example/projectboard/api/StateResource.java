package com.example.projectboard.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.PUT,RequestMethod.DELETE,RequestMethod.POST}, allowedHeaders = "*")
public class StateResource {

    @RequestMapping(method = RequestMethod.GET, value = "/health-check")
    public ResponseEntity<String> checkServerState() {
        return ResponseEntity.ok("connected");
    }
}
