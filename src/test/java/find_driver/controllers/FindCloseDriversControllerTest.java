package find_driver.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import find_driver.models.DriverPosition;
import find_driver.services.DriverPositionSearchService;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FindCloseDriversControllerTest {

    private MockMvc mvc;
    @Mock
    private DriverPositionSearchService searchService;
    @InjectMocks
    private FindCloseDriversController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        List<DriverPosition> driverPositions = new ArrayList<DriverPosition>();
        DriverPosition driverPosition = new DriverPosition();
        driverPosition.setId(1L);
        driverPosition.setDriverId(1L);
        driverPosition.setLatitude(1.000000000001);
        driverPosition.setLongitude(0.0);
        driverPosition.setAccuracy(0.7);
        driverPositions.add(driverPosition);
        when(searchService.searchByLocation(1.0, 0.0, 500, 10))
                .thenReturn(driverPositions);
    }

    @Test
    public void testGetDriversOK() throws Exception {
        String response = "[{\"id\":1,\"" +
                "latitude\":1.000000000001,\"longitude\":" +
                "0.0,\"distance\":1.112048119382908E-7}]";
        mvc.perform(MockMvcRequestBuilders.get("/drivers")
                .param("longitude", "0.0")
                .param("latitude", "1.0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(response)));
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