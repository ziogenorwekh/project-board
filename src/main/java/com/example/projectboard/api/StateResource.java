package com.example.projectboard.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "서버 상태 확인 API")
@RestController
//@CrossOrigin(origins = "http://localhost", methods = {RequestMethod.GET, RequestMethod.PUT,
//        RequestMethod.DELETE,RequestMethod.POST}, allowedHeaders = "*",allowCredentials = "true")
public class StateResource {

    @RequestMapping(method = RequestMethod.GET, value = "/api/health-check")
    public ResponseEntity<String> checkServerState() {
        return ResponseEntity.ok("is-connected");
    }
}
