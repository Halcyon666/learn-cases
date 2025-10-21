package com.whalefall.learncases.design.functionalregistry.v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessDto2;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType2;
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
class Business1ControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RegisterEnginV3<BusinessDto2, BusinessType2> registerEnginV3;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        Business1Controller controller = new Business1Controller();
        // inject the mock into the controller's field
        ReflectionTestUtils.setField(controller, "registerEnginV3", registerEnginV3);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testExecuteV1() throws Exception {
        BusinessDto2 mockResponse = new BusinessDto2();
        when(registerEnginV3.run(eq("job1"), any(BusinessDto2.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/run/v3/job1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BusinessDto2())))
                .andExpect(status().isOk());
    }
}
