package com.neurotech.currencyAPI.DBPopulator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CotacaoResponse {

    @JsonProperty("value")
    private List<Cotacao> cotacoes;
}
