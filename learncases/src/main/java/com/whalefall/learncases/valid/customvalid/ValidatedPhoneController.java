package com.whalefall.learncases.valid.customvalid;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author WhaleFall
 * @date 2022-06-04 22:30
 */
@RestController
public class ValidatedPhoneController {

    @PostMapping("/addValidatePhone")
    @SuppressWarnings("all")
    public String submitForm(@Valid ValidatedPhone validatedPhone/*, BindingResult result*/) {
       /* if (result.hasErrors()) {
            return result.getFieldError().getDefaultMessage();
        }*/
        return "OK";
    }
}
