package com.whalefall.learncases.valid.standvalid;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author WhaleFall
 * @date 2022-05-22 21:41
 */
@RestController
@Slf4j
public class TestController {
    @PostMapping(value = "/saveBasicInfo")
    public String saveBasicInfo(
            @Valid @ModelAttribute("useraccount") UserAccount useraccount
            /*,BindingResult result*/) {
       /* if (result.hasErrors()) {
            return result.getFieldError().getDefaultMessage();
        }*/
        return "success";
    }

    @GetMapping("/")
    public String defaultRequest() {
        return "Hello, World";
    }

}
