package com.whalefall.valid.customvalid;

import com.whalefall.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author halcyon
 * @date 2022-05-22 21:44
 */

class TestCustomValid extends AbstractTest {

    @Test
    void testCustomValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/addValidatePhone").
                        accept(MediaType.TEXT_HTML).
                        param("phone", "123456789")).
                andExpect(status().isOk()).
                andDo(print());
    }


}
