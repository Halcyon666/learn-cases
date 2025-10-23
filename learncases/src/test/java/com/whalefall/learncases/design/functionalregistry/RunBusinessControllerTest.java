// language: Java
package com.whalefall.learncases.design.functionalregistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whalefall.learncases.design.functionalregistry.v1v2.RegisterEnginV1;
import com.whalefall.learncases.design.functionalregistry.v1v2.RegisterEnginV2;
import com.whalefall.learncases.design.functionalregistry.v1v2.RunJobController;
import com.whalefall.learncases.design.functionalregistry.v1v2.pojo.TxData;
import com.whalefall.learncases.design.functionalregistry.v1v2.template.TemplateImpl1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RunBusinessControllerTest {

    private RegisterEnginV1<TxData> enginV1;
    private RegisterEnginV2<TxData> enginV2;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // create generic mocks via cast and suppress the unavoidable unchecked warning
        @SuppressWarnings("unchecked")
        RegisterEnginV1<TxData> e1 = (RegisterEnginV1<TxData>) Mockito.mock(RegisterEnginV1.class);
        @SuppressWarnings("unchecked")
        RegisterEnginV2<TxData> e2 = (RegisterEnginV2<TxData>) Mockito.mock(RegisterEnginV2.class);
        enginV1 = e1;
        enginV2 = e2;

        objectMapper = new ObjectMapper(); // 先创建 ObjectMapper，用于 message converter
        RunJobController controller = new RunJobController(enginV1, enginV2);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private TxData sampleTxData() {
        TxData tx = new TxData();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        list.add(map);
        tx.put("items", list);
        return tx;
    }

    @Test
    void executeV1_shouldCallEngineV1_andReturnResult() throws Exception {
        TxData request = sampleTxData();
        TxData response = sampleTxData();
        response.get("items").get(0).put("returned", true);

        // 使用 anyString() 避免名称不一致导致未命中
        when(enginV1.run(anyString(), any())).thenReturn(response);

        mockMvc.perform(post("/run/v1/business1v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(enginV1, times(1)).run(anyString(), any());
    }

    @Test
    void executeV1_shouldCallEngineV1_forJob2_andReturnResult() throws Exception {
        TxData request = sampleTxData();
        TxData response = sampleTxData();
        response.get("items").get(0).put("returnedBy", "v1-job2");

        // 使用 anyString() 避免名称不一致导致未命中
        when(enginV1.run(anyString(), any())).thenReturn(response);

        mockMvc.perform(post("/run/v1/business2v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(enginV1, times(1)).run(anyString(), any());
    }

    @Test
    void executeV2_shouldCallEngineV2_withTemplateClass_andReturnResult() throws Exception {
        TxData request = sampleTxData();
        TxData response = sampleTxData();
        response.get("items").get(0).put("returnedBy", "v2");

        // 对 v2 也使用 anyString() 来保证 stub 命中
        when(enginV2.run(anyString(), any(), eq(TemplateImpl1.class))).thenReturn(response);

        mockMvc.perform(post("/run/v2/business2v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(enginV2, times(1)).run(anyString(), any(), eq(TemplateImpl1.class));
    }
}
