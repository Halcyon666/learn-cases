package com.whalefall.learncases.spel;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AnTestController {
    @Resource(name = "anTestController")
    private AnTestController controller;

    /**
     * 支持注解切面round通知
     *
     * @return String
     */
    @RequestMapping("/anno/test")
    public String test() {
        return controller.get1("hh");
    }

    @MyAnnotation(value = TypeEnum.TYPE1, spel = "#value.toUpperCase()")
    public String get1(String value) {
        return String.format("%s%s", TypeEnum.TYPE1.getType(), value);
    }
}
