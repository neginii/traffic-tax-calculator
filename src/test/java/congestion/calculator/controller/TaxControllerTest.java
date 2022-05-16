package congestion.calculator.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaxControllerTest {

    private final WebApplicationContext wac;
    private MockMvc mockMvc;

    TaxControllerTest(WebApplicationContext wac) {
        this.wac = wac;
    }

    @BeforeAll
    void runBeforeAllTestMethods() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void testTaxFreeVehicleShouldReturnsBadRequestResponse() throws Exception {

        String requestJson = readResource("input-request-00.json");

        this.mockMvc
                .perform(post("/calculateTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tax calculation failed."));
    }

    @Test
    void testTaxFreeVehicleShouldReturnsCorrectTaxCalculation() throws Exception {

        String requestJson = readResource("input-request-01.json");

        this.mockMvc
                .perform(post("/calculateTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.tax.tax").value("60.0"))
                .andExpect(jsonPath("$.message").value("tax calculated successfully"));
    }

    @Test
    void testTaxInJulyShouldReturn0() throws Exception {

        String requestJson = readResource("input-request-02.json");

        this.mockMvc
                .perform(post("/calculateTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.tax.tax").value("0.0"))
                .andExpect(jsonPath("$.message").value("tax calculated successfully"));
    }

    @Test
    void testTaxOnSundayShouldReturn0() throws Exception {

        String requestJson = readResource("input-request-03.json");

        this.mockMvc
                .perform(post("/calculateTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.tax.tax").value("0.0"))
                .andExpect(jsonPath("$.message").value("tax calculated successfully"));
    }

    @Test
    void testTaxOnSaturdayShouldReturn0() throws Exception {

        String requestJson = readResource("input-request-04.json");

        this.mockMvc
                .perform(post("/calculateTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.tax.tax").value("0.0"))
                .andExpect(jsonPath("$.message").value("tax calculated successfully"));
    }

    @Test
    void testTaxOnDayBeforeHolidaysShouldReturn0() throws Exception {

        String requestJson = readResource("input-request-05.json");

        this.mockMvc
                .perform(post("/calculateTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.tax.tax").value("0.0"))
                .andExpect(jsonPath("$.message").value("tax calculated successfully"));
    }

    @Test
    void testMaximumTaxFeePerDayShouldBe60() throws Exception {

        String requestJson = readResource("input-request-06.json");

        this.mockMvc
                .perform(post("/calculateTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.tax.tax").value("60.0"))
                .andExpect(jsonPath("$.message").value("tax calculated successfully"));
    }

    @Test
    void testSingleChargeRule() throws Exception {

        String requestJson = readResource("input-request-07.json");

        this.mockMvc
                .perform(post("/calculateTax")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.tax.tax").value("18.0"))
                .andExpect(jsonPath("$.message").value("tax calculated successfully"));
    }


    private String readResource(String fileName) throws IOException {
        var classLoader = getClass().getClassLoader();
        return new String(Objects.requireNonNull(classLoader.getResourceAsStream(fileName)).readAllBytes());
    }
}