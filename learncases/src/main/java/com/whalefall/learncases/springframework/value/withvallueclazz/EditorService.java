package com.whalefall.learncases.springframework.value.withvallueclazz;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @date 2024/6/24 19:01
 * @since 1.0.0
 */
@Getter
@Component
public class EditorService {
    @Value("${org.xxx}")
    private String org;

    @Value("${org.number}")
    private int number;

    @Value("${org.flag}")
    private boolean flag;

    @Value("${org.b}")
    private byte b;

    @Value("${org.xxx1:}")
    private String org1;

    @Value("${org.xxx2:}")
    private String org2;

}
