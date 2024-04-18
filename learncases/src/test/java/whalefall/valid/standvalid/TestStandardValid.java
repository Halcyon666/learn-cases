package whalefall.valid.standvalid;

import com.whalefall.learncases.LearncasesApplication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author chenglong
 * @date 2022-05-22 21:44
 */
@SpringBootTest(classes = LearncasesApplication.class)
@AutoConfigureMockMvc
class TestStandardValid {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenSaveBasicInfo_whenCorrectInput_thenSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/saveBasicInfo")
                        .accept(MediaType.TEXT_HTML)
                        .param("name", "test123")
                        .param("email", "example@gmail.com")
                        .param("password", "pass12"))
//                .andExpect(view().name("success"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Disabled
    void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World")));
    }
}
