package com.whalefall.learncases.valid.standvalid;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author WhaleFall
 * @date 2022-05-22 21:41
 */
@RestController
@Slf4j
public class TestController {
    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * {
     * "name": "String",
     * "password": "1234567",
     * "email": "12314@qq.com"
     * }
     */
    @PostMapping(value = "/saveBasicInfo")
    public String saveBasicInfo(@Valid @RequestBody UserAccount useraccount) {
        return "success";
    }

    @GetMapping("/testServiceValid")
    public String testServiceValid() {
        return userService.testValid(new UserAccount());
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World";
    }
}
