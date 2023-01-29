package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BreweryControllerIT extends BaseIT {

    public static final String URL_TEMPLATE = "/brewery/breweries";
    public static final String REST_API_TEMPLATE = "/brewery/api/v1/breweries";

    @Test
    void listBreweriesCUSTOMER() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesADMIN() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesUSER() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBreweriesNOAUTH() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getBreweriesJsonCUSTOMER() throws Exception {
        mockMvc.perform(get(REST_API_TEMPLATE)
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getBreweriesJsonADMIN() throws Exception {
        mockMvc.perform(get(REST_API_TEMPLATE)
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getBreweriesJsonUSER() throws Exception {
        mockMvc.perform(get(REST_API_TEMPLATE)
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBreweriesJsonNOAUTH() throws Exception {
        mockMvc.perform(get(REST_API_TEMPLATE))
                .andExpect(status().isUnauthorized());
    }
}
