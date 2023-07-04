package com.whalefall.learncases.bytes;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RareWold {
    public static void main(String[] args) {

        log.info("\uE863䶮 " + "\uE863䶮".length());
        log.info("我a " + "我a".getBytes(Charset.forName("GBK")).length);
        log.info("䶮 " + "䶮".getBytes(Charset.forName("GBK")).length);
        log.info("\uE863 " + "\uE863".getBytes(Charset.forName("GBK")).length);
        log.info("\uE863䶮 " + "\uE863䶮".getBytes(StandardCharsets.UTF_8).length);
    }
}
