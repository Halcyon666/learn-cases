package com.whalefall.learncases.valid.standvalid;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Halcyon
 * @since 1.0.0
 */
@Service
@Validated
public class UserService {
    public String testValid(@Valid @RequestBody UserAccount userAccount) {
        return "ok"+userAccount;
    }
}
