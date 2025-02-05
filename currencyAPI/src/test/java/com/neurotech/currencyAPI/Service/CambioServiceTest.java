package com.neurotech.currencyAPI.Service;

import com.neurotech.currencyAPI.Exception.CambioNotFoundException;
import com.neurotech.currencyAPI.Exception.DateNotValidException;
import com.neurotech.currencyAPI.Repository.CambioRepository;
import com.neurotech.currencyAPI.model.Cambio;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CambioServiceTest {

    @Mock
    private CambioRepository cambioRepository;

    @Autowired
    @InjectMocks
    private CambioService cambioService;


    private void setupLatestCambioMock() {
        Cambio mockedCambio = new Cambio(new GregorianCalendar(2024, Calendar.JANUARY, 10).getTime(), 1, 1);
        Mockito.when(cambioRepository.getLatestCambio()).thenReturn(Optional.of(mockedCambio));
    }

    void setupCambioIntervalMock() {
        List<Cambio> mockedList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            mockedList.add(new Cambio(new GregorianCalendar(2024, Calendar.JANUARY, i).getTime(), 1, 1));
        }
        Mockito.when(cambioRepository.findCambioBetweenDates(ArgumentMatchers.any(Date.class),
                ArgumentMatchers.any(Date.class))).thenReturn(Optional.of(mockedList));
    }

    @Test
    @DisplayName("Should retrieve a instance of the newest Cambio")
    void getLatestCambioSuccess() throws CambioNotFoundException {
        setupLatestCambioMock();
        Cambio returned = cambioService.getLatestCambio();
        Assertions.assertThat(returned).isNotNull();
        Assertions.assertThat(returned.getDataCambio()).hasDayOfMonth(10).hasYear(2024);
    }

    @Test
    @DisplayName("Should throw an exception if there is no instance of Cambio")
    void getLatestCambioFail() {
        Mockito.when(cambioRepository.getLatestCambio()).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(CambioNotFoundException.class).isThrownBy(()
                -> cambioService.getLatestCambio()).withMessageContaining("latest cambio was not found");
    }

    @Test
    @DisplayName("Should retrieve a list of Cambio between an interval")
    void getCambioIntervalSuccessNoGaps() throws CambioNotFoundException, DateNotValidException {
        setupCambioIntervalMock();
        List<Cambio> cambios = cambioService.getCambioInterval("2024-01-01", "2024-01-10");

        Assertions.assertThat(cambios).isNotNull();
        Assertions.assertThat(cambios.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should fill empty gaps when returning Cambio instances")
    void getCambioIntervalSuccessWithGapsFilled() throws CambioNotFoundException, DateNotValidException {
        List<Cambio> cambioWithGaps = new ArrayList<>();
        for (int i = 1; i <= 30; i += 2) {
            // filling list with gaps
            cambioWithGaps.add(new Cambio(new GregorianCalendar(2024, Calendar.JANUARY, i).getTime(), 1, 1));
        }
        Mockito.when(cambioRepository.findCambioBetweenDates(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Optional.of(cambioWithGaps));

        // checking if gaps were inserted
        List<Cambio> cambios = cambioService.getCambioInterval("2024-01-01", "2024-01-30");
        Assertions.assertThat(cambios).isNotNull().isNotEmpty();
        Assertions.assertThat(cambios.size()).isEqualTo(30);
        for (int i = 1; i <= 30; i++) {
            Cambio cambio = cambios.get(i - 1);
            if (i % 2 == 0) {
                // inserted gaps
                Assertions.assertThat(cambio.getDataCambio()).hasDayOfMonth(i).hasYear(2024);
                Assertions.assertThat(cambio.getCotacaoVenda()).isZero();
                Assertions.assertThat(cambio.getCotacaoCompra()).isZero();
            } else {
                // original data
                Assertions.assertThat(cambio.getDataCambio()).hasDayOfMonth(i).hasYear(2024);
                Assertions.assertThat(cambio.getCotacaoVenda()).isNotZero();
                Assertions.assertThat(cambio.getCotacaoCompra()).isNotZero();
            }
        }
    }

    @Test
    @DisplayName("Should throw an exception when there are no Cambios in the interval")
    void getCambioIntervalFail() {
        Mockito.when(cambioRepository.findCambioBetweenDates(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(CambioNotFoundException.class).isThrownBy(()
                -> cambioService.getCambioInterval("2024-01-11", "2024-01-20"));
    }

    @Test
    @DisplayName("Should throw an exception when the at least one date is invalid")
    void getCambioIntervalInvalidDate() {
        Assertions.assertThatExceptionOfType(DateNotValidException.class).isThrownBy(()
                -> cambioService.getCambioInterval("10-10-2010", "12-12-2011")); // wrong format

        Assertions.assertThatExceptionOfType(DateNotValidException.class).isThrownBy(()
                -> cambioService.getCambioInterval("2022-01-30", "2022-01-01")); // start > end
    }

}
