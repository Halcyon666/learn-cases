package com.whalefall.learncases.staticmock;

import org.springframework.stereotype.Service;

/**
 * @author Halcyon
 * @date 2024/6/21 20:06
 * @since 1.0.0
 */
@Service
public class MyService {
    public String process(String input) {
        return Utils.staticMethod(input);
    }
}

