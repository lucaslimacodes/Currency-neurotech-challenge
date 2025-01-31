package com.neurotech.currencyAPI.Repository;

import com.neurotech.currencyAPI.model.Cambio;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.assertj.core.api.Assertions;

import java.util.*;

@DataJpaTest
@ActiveProfiles("test")
public class CambioRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CambioRepository cambioRepository;

    @Test
    @DisplayName("Should return the newest Cambio object added to the database")
    void getLatestCambioSuccess(){
        Cambio oldCambio = new Cambio(new GregorianCalendar(2024,Calendar.JANUARY,10).getTime(),1,1);
        Cambio newCambio = new Cambio(new GregorianCalendar(2024,Calendar.OCTOBER,1).getTime(),2,2);

        entityManager.persist(oldCambio);
        entityManager.persist(newCambio);

        Assertions.assertThat(cambioRepository.getLatestCambio().isPresent()).isTrue();
        Assertions.assertThat(cambioRepository.getLatestCambio().get()).isEqualTo(newCambio);;

    }

    @Test
    @DisplayName("Should return null when the database is empty")
    void getLatestCambioFail(){
        Assertions.assertThat(cambioRepository.getLatestCambio().isEmpty()).isTrue();
        Assertions.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(()->cambioRepository.getLatestCambio().get());
    }

    @Test
    @DisplayName("Should return at least one instance that is between a certain interval")
    void getCambiosIntervalSuccess(){
        for(int i=1;i<=10;i++){
            Cambio cambio = new Cambio(new GregorianCalendar(2024,Calendar.JANUARY,i).getTime(),1,1);
            entityManager.persist(cambio);
        }

        Date date1 = new GregorianCalendar(2024,Calendar.JANUARY,1).getTime();
        Date date2 = new GregorianCalendar(2024,Calendar.JANUARY,5).getTime();

        Optional<List<Cambio>> result = cambioRepository.findCambioBetweenDates(date1,date2);
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().size()).isEqualTo(5);
        for(int i=1;i<=5;i++){
            Assertions.assertThat(result.get().get(i-1).getDataCambio()).hasDayOfMonth(i);
        }

        result = cambioRepository.findCambioBetweenDates(date1,date1);
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().size()).isEqualTo(1);
        Assertions.assertThat(result.get().get(0).getDataCambio()).hasDayOfMonth(1);

        date1 = new GregorianCalendar(2023,Calendar.DECEMBER,1).getTime();
        date2 = new GregorianCalendar(2024,Calendar.JANUARY,30).getTime();

        result  = cambioRepository.findCambioBetweenDates(date1,date2);
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().size()).isEqualTo(10);
        for(int i=1;i<=10;i++){
            Assertions.assertThat(result.get().get(i-1).getDataCambio()).hasDayOfMonth(i);
        }
    }

    @Test
    @DisplayName("Should return null if there is no instance between the interval")
    void getCambiosIntervalFail(){
        for(int i=1;i<=10;i++){
            Cambio cambio = new Cambio(new GregorianCalendar(2024,Calendar.JANUARY,i).getTime(),1,1);
            entityManager.persist(cambio);
        }
        Date date1 = new GregorianCalendar(2024, Calendar.FEBRUARY,1).getTime();
        Date date2 = new GregorianCalendar(2024,Calendar.FEBRUARY,10).getTime();
        Optional<List<Cambio>> result = cambioRepository.findCambioBetweenDates(date1,date2);
        Assertions.assertThat(result.get().isEmpty()).isTrue();
    }
}
