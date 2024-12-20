package com.whalefall;

import com.whalefall.learncases.LearncasesApplication;
import com.whalefall.learncases.netty.server.task.NettyServerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = LearncasesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public abstract class AbstractTest {
    @Autowired
    public MockMvc mockMvc;

    @MockitoBean
    public NettyServerTask nettyServerTask;
}
