package com.neurotech.currencyAPI.Repository;

import com.neurotech.currencyAPI.model.Cambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CambioRepository extends JpaRepository<Cambio, Date> {

    @Query("SELECT c FROM Cambio c WHERE c.dataCambio= ?1")
    Optional<Cambio> getCambioByDate(Date date);

    @Query("SELECT c FROM Cambio c WHERE c.dataCambio >= ?1 AND c.dataCambio <= ?2")
    Optional<List<Cambio>> findCambioBetweenDates(Date startDate, Date endDate);
}
