package find_driver.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FindCloseDriversTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetDriversEmptyResult() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/drivers")
                .param("longitude", "0.0")
                .param("latitude", "1.0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));
    }

    @Test
    public void testGetDriversOK() throws Exception {
        String content = "[{\"id\":1,\"latitude\":45.0,\"longitude\":108.0,\"distance\":0.0}]";
        mvc.perform(MockMvcRequestBuilders.get("/drivers")
                .param("longitude", "108.0")
                .param("latitude", "45.0")
                .param("radius", "10")
                .param("limit", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(content)));
    }

    @Test
    public void testGetDriversInvalidParameterType() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/drivers")
                .param("latitude", "1.0")
                .param("longitude", "random")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDriversMissingParameter() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/drivers")
                .param("latitude", "0.0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void testGetDriversInvalidParameter() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/drivers")
                .param("latitude", "1.0")
                .param("longitude", "-200")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(422));
    }
}
