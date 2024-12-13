package com.whalefall.learncases.ftp;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Halcyon
 * @date 2024/12/13 21:28
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("ftp")
@Component
public class Vfs2Configuration {
    /**
     * url may be like "ftp://user:password@ftp.example.com/"
     * and the password may be need {@link URLEncoder#encode(String, String)}
     */
    private String baseUrl1;
    private String baseUrl2;
    private Set<String> baseUrls;

    @PostConstruct
    public void init(){
        baseUrls = new HashSet<>(Arrays.asList(baseUrl1, baseUrl2));
    }
}
