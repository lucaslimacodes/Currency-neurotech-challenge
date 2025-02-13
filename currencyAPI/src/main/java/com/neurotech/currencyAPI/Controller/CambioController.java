package com.neurotech.currencyAPI.Controller;

import com.neurotech.currencyAPI.Infra.ErrorResponse;
import com.neurotech.currencyAPI.Service.CambioService;
import com.neurotech.currencyAPI.model.Cambio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CambioController {

    @Autowired
    private CambioService cambioService;

    @Operation(description = "Gets most recent currency rate in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The most recent currency rate is returned successfully"),
            @ApiResponse(responseCode = "404",
                    description = "Error finding most recent currency rate, probably empty database",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))})
    })
    @GetMapping("/latest")
    public ResponseEntity<Cambio> getLatest() {
        Cambio latest = cambioService.getLatestCambio();
        return new ResponseEntity<>(latest, HttpStatus.OK);
    }

    @Operation(description = "Returns all currency rates between an interval.\n " +
            "If the database from which the data is retrieved has gaps, it is automatically filled with instances " +
            "containing rates with 0's ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "All rates between the interval was returned successfully"),
            @ApiResponse(responseCode = "404",
                    description = "No instances of currency rates between interval in database",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Date format is incorrect, trying to insert future date or order is incorrect",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))})
    })
    @GetMapping("/interval")
    public ResponseEntity<List<Cambio>> getInterval(@Parameter(description = "yyyy-MM-dd") @RequestParam(name =
                                                            "startDate") String startDate,
                                                    @Parameter(description = "yyyy-MM-dd") @RequestParam(name =
                                                            "endDate") String endDate) {
        List<Cambio> cambioList = cambioService.getCambioInterval(startDate, endDate);
        return new ResponseEntity<>(cambioList, HttpStatus.OK);
    }
}
