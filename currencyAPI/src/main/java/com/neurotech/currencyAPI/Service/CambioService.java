package com.neurotech.currencyAPI.Service;

import com.neurotech.currencyAPI.Exception.CambioNotFoundException;
import com.neurotech.currencyAPI.Repository.CambioRepository;
import com.neurotech.currencyAPI.Utils.DateUtils;
import com.neurotech.currencyAPI.model.Cambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CambioService {

    @Autowired
    private CambioRepository cambioRepository;

    public Cambio getLatestCambio() throws CambioNotFoundException {
        Optional<Cambio> latestCambio = cambioRepository.getLatestCambio();
        if(latestCambio.isEmpty()){
            throw new CambioNotFoundException("latest cambio was not found");
        }
        else{
            return latestCambio.get();
        }
    }

    public List<Cambio> getCambioInterval(Date startDate, Date endDate) throws CambioNotFoundException {
        Optional<List<Cambio>> cambioList = cambioRepository.findCambioBetweenDates(startDate, endDate);
        if(cambioList.isEmpty() || cambioList.get().isEmpty()){
            throw new CambioNotFoundException("cambio list is empty");
        }
        else{
            List<Cambio> cambios =  cambioList.get();
            return fillGapsOfInterval(cambios, startDate, endDate);
        }
    }

    private List<Cambio> fillGapsOfInterval(List<Cambio> cambios, Date startDate, Date endDate){

        // checking boundaries

        // beginning of list
        while(DateUtils.isDifferenceBiggerOrEqualOneDay(startDate, cambios.get(0).getDataCambio())){
            Date currentInitialDate = cambios.get(0).getDataCambio();
            cambios.addFirst(new Cambio(DateUtils.yesterday(currentInitialDate), 0,0));
        }

        // end of the list
        while(DateUtils.isDifferenceBiggerOrEqualOneDay(cambios.getLast().getDataCambio(), endDate)){
            Date currentFinalDate = cambios.getLast().getDataCambio();
            cambios.addLast(new Cambio(DateUtils.tomorrow(currentFinalDate), 0,0));
        }

        // checking middle of list

        for(int i=0;i<cambios.size()-1;i++){
            Date date1 = cambios.get(i).getDataCambio();
            Date date2 = cambios.get(i+1).getDataCambio();
            if(DateUtils.isDifferenceBiggerThanOneDay(date1, date2)){
                cambios.add(i+1, new Cambio(DateUtils.tomorrow(date1), 0, 0));
            }
        }

        return cambios;
    }
}
