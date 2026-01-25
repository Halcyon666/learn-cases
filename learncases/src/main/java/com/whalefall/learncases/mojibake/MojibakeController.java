package com.whalefall.learncases.mojibake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字符编码处理控制器（Mojibake - 乱码处理）
 *
 * @author WhaleFall
 * @date 2022-07-24 6:34
 */
@RestController
@Slf4j
public class MojibakeController {

    /**
     * 处理纯文本请求
     * <p>
     * POST /processTextPlain
     * Content-Type: text/plain; charset=UTF-8
     * <p>
     * 功能说明：接收纯文本格式的请求体，记录请求内容和Content-Type头部信息，
     * 并返回处理成功的响应消息
     *
     * @param requestBody 请求体内容（纯文本格式）
     * @param contentType 请求头中的Content-Type值
     * @return ResponseEntity<String> 包含处理结果的响应实体
     */
    @PostMapping(value = "/processTextPlain", produces = "text/plain; charset=UTF-8")
    public ResponseEntity<String> processTextPlain(@RequestBody String requestBody,
                                                   @RequestHeader("content-type") String contentType) {

        log.info("requestBody: {}, content-type: {}", requestBody, contentType);

        // 返回响应
        return new ResponseEntity<>("Message processed successfully, origin request is %s".formatted(requestBody), HttpStatus.OK);
    }
}
