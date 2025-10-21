package com.whalefall.learncases.design.functionalregistry.v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessDto1;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class Business2ControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RegisterEnginV3<BusinessDto1, BusinessType1> registerEnginV3;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        Business2Controller controller = new Business2Controller();
        ReflectionTestUtils.setField(controller, "registerEnginV3", registerEnginV3);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testExecuteV1() throws Exception {
        BusinessDto1 mockResponse = new BusinessDto1();
        when(registerEnginV3.run(eq("type1"), any(BusinessDto1.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/run/v3/type1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BusinessDto1())))
                .andExpect(status().isOk());
    }
}
