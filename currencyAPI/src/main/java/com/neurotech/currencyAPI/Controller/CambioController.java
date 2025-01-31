package com.neurotech.currencyAPI.Controller;

import com.neurotech.currencyAPI.Exception.CambioNotFoundException;
import com.neurotech.currencyAPI.Service.CambioService;
import com.neurotech.currencyAPI.model.Cambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
public class CambioController {

    @Autowired
    private CambioService cambioService;

    @GetMapping("/latest")
    public ResponseEntity<Cambio> getLatest(){
        Cambio cambio = null;
        try{
            cambio = cambioService.getLatestCambio();
        }
        catch (CambioNotFoundException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cambio, HttpStatus.OK);

    }

    @GetMapping("/interval")
    public ResponseEntity<List<Cambio>> getInterval(@RequestParam(name = "start date(yyyy-MM-dd)") String startDate, @RequestParam(name = "end date(yyyy-MM-dd)") String endDate){
        List<Cambio> cambioList = null;

        Date start = null;
        Date end = null;

        try{
            start = Date.valueOf(startDate);
            end = Date.valueOf(endDate);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

        try{
            cambioList = cambioService.getCambioInterval(start, end);
        }catch (CambioNotFoundException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cambioList, HttpStatus.OK);
    }
}
