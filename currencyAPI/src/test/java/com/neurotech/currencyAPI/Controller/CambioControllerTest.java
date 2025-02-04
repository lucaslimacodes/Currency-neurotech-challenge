package com.neurotech.currencyAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neurotech.currencyAPI.Exception.CambioNotFoundException;
import com.neurotech.currencyAPI.Infra.ErrorResponse;
import com.neurotech.currencyAPI.Service.CambioService;
import com.neurotech.currencyAPI.model.Cambio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CambioController.class)
public class CambioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CambioService cambioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should return 200 OK with a Cambio body")
    void getLatestCambioSuccess() throws Exception {

        Cambio cambioFromService = new Cambio(new GregorianCalendar(2024, 1, 1).getTime(), 1, 1);
        Mockito.when(cambioService.getLatestCambio()).thenReturn(cambioFromService);

        String cambioString = objectMapper.writeValueAsString(cambioFromService);

        mockMvc.perform(get("/latest")).andExpect(status().isOk()).andExpect(content().json(cambioString));
    }

    @Test
    @DisplayName("Should return 404 and a Error response inside body")
    void getLatestCambioFail() throws Exception {
        Mockito.when(cambioService.getLatestCambio()).thenThrow(new CambioNotFoundException("latest cambio was not " +
                "found"));

        ErrorResponse expectedResponse = new ErrorResponse("latest cambio was not found", HttpStatus.NOT_FOUND);

        String responseString = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(get("/latest")).andExpect(status().isNotFound()).andExpect(content().json(responseString));
    }

    @Test
    @DisplayName("Should return 200 and a list of Cambio in body")
    void getCambioIntervalSuccess() throws Exception {
        List<Cambio> cambioListFromService = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            cambioListFromService.add(new Cambio(new GregorianCalendar(2024, Calendar.JANUARY, i).getTime(), 1, 1));
        }
        Mockito.when(cambioService.getCambioInterval(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(cambioListFromService);

        String cambioListString = objectMapper.writeValueAsString(cambioListFromService);

        mockMvc.perform(get("/interval?startDate=2024-01-01&endDate=2024-01-10"))
                .andExpect(status().isOk()).andExpect(content().json(cambioListString));
    }

    @Test
    @DisplayName("Should return Bad request the the date format is invalid")
    void getCambioIntervalFail() throws Exception {
        Mockito.when(cambioService.getCambioInterval(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(new CambioNotFoundException("cambio list is empty"));

        ErrorResponse expectedResponse = new ErrorResponse("cambio list is empty", HttpStatus.NOT_FOUND);

        String responseString = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(get("/interval?startDate=2024-01-01&endDate=2024-01-10"))
                .andExpect(status().isNotFound()).andExpect(content().json(responseString));
    }

}
