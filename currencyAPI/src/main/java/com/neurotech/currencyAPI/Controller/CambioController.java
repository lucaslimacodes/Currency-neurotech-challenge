package com.neurotech.currencyAPI.Controller;

import com.neurotech.currencyAPI.Exception.CambioNotFoundException;
import com.neurotech.currencyAPI.Service.CambioService;
import com.neurotech.currencyAPI.model.Cambio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description = "Gets most recent currency rate in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The most recent currency rate is returned successfully"),
            @ApiResponse(responseCode = "404", description = "Error finding most recent currency rate, probably empty bd")
    })
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

    @Operation(description = "Returns all currency rates between an interval.\n " +
            "If the database from which the data is retrieved has gaps, it is automatically filled with instances containing rates with 0's ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All rates between the interval was returned  successfully"),
            @ApiResponse(responseCode = "404", description = "No instances of currency rates between interval in database"),
            @ApiResponse(responseCode = "400", description = "Date format is incorrect, trying to insert future date or order is incorrect")
    })
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

        Date today = new Date(System.currentTimeMillis());
        if(start.compareTo(today) > 0 || end.compareTo(today) > 0 || start.compareTo(end) > 0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try{
            cambioList = cambioService.getCambioInterval(start, end);
        }catch (CambioNotFoundException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cambioList, HttpStatus.OK);
    }
}
