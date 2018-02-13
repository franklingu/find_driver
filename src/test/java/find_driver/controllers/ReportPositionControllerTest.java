package find_driver.controllers;

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
public class ReportPositionControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testUpdatePositionOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/drivers/20/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longitude\": 9.0, \"latitude\": 9.0, \"accuracy\": 1.0}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void testUpdatePositionEmptyBody() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/drivers/20/location")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdatePositionInvalidParameter() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/drivers/20/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longitude\": 9.0, \"latitude\": -209.0, \"accuracy\": 1.0}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdatePositionNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/drivers/-1/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longitude\": 9.0, \"latitude\": -209.0, \"accuracy\": 1.0}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }
}