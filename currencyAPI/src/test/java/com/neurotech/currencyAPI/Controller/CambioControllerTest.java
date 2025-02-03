package com.neurotech.currencyAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neurotech.currencyAPI.Exception.CambioNotFoundException;
import com.neurotech.currencyAPI.Infra.ErrorResponse;
import com.neurotech.currencyAPI.Service.CambioService;
import com.neurotech.currencyAPI.model.Cambio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.GregorianCalendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void getLatestCambioSucces () throws Exception{

        Cambio cambioFromervice = new Cambio(new GregorianCalendar(2024,1,1).getTime(),1,1);
        Mockito.when(cambioService.getLatestCambio()).thenReturn(cambioFromervice);

        String cambioString = objectMapper.writeValueAsString(cambioFromervice);

        mockMvc.perform(get("/latest")).andExpect(status().isOk()).andExpect(content().json(cambioString));
    }

    @Test
    @DisplayName("Should return 404 and a Error response inside body")
    void getLatestCambioFail() throws Exception {
        Mockito.when(cambioService.getLatestCambio()).thenThrow(new CambioNotFoundException("latest cambio was not found"));

        ErrorResponse expectedResponse = new ErrorResponse("latest cambio was not found",  HttpStatus.NOT_FOUND);

        String responseString = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(get("/latest")).andExpect(status().isNotFound()).andExpect(content().json(responseString));
    }

}
