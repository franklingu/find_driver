package find_driver.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import find_driver.models.DriverPosition;
import find_driver.repositories.DriverPositionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReportPositionControllerTest {
    private MockMvc mvc;
    @Mock
    private DriverPositionRepository driverPositionRepository;
    @InjectMocks
    private ReportPositionController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testUpdatePositionOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/drivers/20/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longitude\": 9.0, \"latitude\": 0.0, \"accuracy\": 1.0}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(content().string(equalTo("{}")));
        DriverPosition position = new DriverPosition();
        position.setLatitude(0.0);
        position.setLongitude(9.0);
        position.setAccuracy(1.0);
        verify(driverPositionRepository, times(1)).save(position);
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
        ).andExpect(status().is(422));
    }

    @Test
    public void testUpdatePositionInvalidFormat() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/drivers/20/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longitude\": 9.0, \"latitude\": \"real\", \"accuracy\": 1.0}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatePositionNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/drivers/-1/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longitude\": 9.0, \"latitude\": -20.0, \"accuracy\": 1.0}")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(404));
    }
}