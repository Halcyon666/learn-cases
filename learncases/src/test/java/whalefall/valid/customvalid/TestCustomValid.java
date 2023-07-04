package whalefall.valid.customvalid;

import com.whalefall.learncases.LearncasesApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author chenglong
 * @date 2022-05-22 21:44
 */
@SpringBootTest(classes = LearncasesApplication.class)
@AutoConfigureMockMvc
class TestCustomValid {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCustomValid() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addValidatePhone").
                        accept(MediaType.TEXT_HTML).
                        param("phone", "123456789")).
                andExpect(status().isOk()).
                andDo(print());
    }


}
