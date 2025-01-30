package com.neurotech.currencyAPI.DBPopulator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class Cotacao {
    @JsonProperty("cotacaoCompra")
    private float cotacaoCompra;

    @JsonProperty("cotacaoVenda")
    private float cotacaoVenda;

    @JsonProperty("dataHoraCotacao")
    private String dataCotacao;
}
