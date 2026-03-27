package com.whalefall.learncases.valid.standvalid;

import cn.hutool.json.JSONUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


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

    @GetMapping("/api/v1/system/status")
    public String systemStatus() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("status", "200");
        hashMap.put("message", "success");
        return JSONUtil.toJsonStr(hashMap);
    }
}
