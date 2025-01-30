package com.neurotech.currencyAPI.DBPopulator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.print.DocFlavor;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class ResponseDTO {
    @JsonProperty("date")
    private Date date;
    @JsonProperty("eur")
    private Map<String,Float> rates;
}
