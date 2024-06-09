package com.whalefall.learncases.mojibake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MojibakeController {

    @PostMapping(value = "/processTextPlain", produces = "text/plain; charset=UTF-8")
    public ResponseEntity<String> processTextPlain(@RequestBody String requestBody,
                                                   @RequestHeader("content-type") String contentType) {

        log.info("requestBody: {}, content-type: {}", requestBody, contentType);

        // 返回响应
        return new ResponseEntity<>("Message processed successfully, origin request is %s".formatted(requestBody), HttpStatus.OK);
    }
}
