package com.whalefall.learncases.design.functionalregistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whalefall.learncases.design.functionalregistry.pojo.TxData;
import com.whalefall.learncases.design.functionalregistry.template.TemplateImpl1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RunJobControllerTest {

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
        RunJobController controller = new RunJobController(enginV1, enginV2);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
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

        when(enginV1.run(eq("job1"), any())).thenReturn(response);

        mockMvc.perform(post("/run/v1/job1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(enginV1, times(1)).run(eq("job1"), any());
    }

    @Test
    void executeV1_shouldCallEngineV1_forJob2_andReturnResult() throws Exception {
        TxData request = sampleTxData();
        TxData response = sampleTxData();
        response.get("items").get(0).put("returnedBy", "v1-job2");

        when(enginV1.run(eq("job2"), any())).thenReturn(response);

        mockMvc.perform(post("/run/v1/job2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(enginV1, times(1)).run(eq("job2"), any());
    }

    @Test
    void executeV2_shouldCallEngineV2_withTemplateClass_andReturnResult() throws Exception {
        TxData request = sampleTxData();
        TxData response = sampleTxData();
        response.get("items").get(0).put("returnedBy", "v2");

        when(enginV2.run(eq("job2"), any(), eq(TemplateImpl1.class))).thenReturn(response);

        mockMvc.perform(post("/run/v2/job2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(enginV2, times(1)).run(eq("job2"), any(), eq(TemplateImpl1.class));
    }
}
